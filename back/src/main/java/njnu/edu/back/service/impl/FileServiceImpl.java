package njnu.edu.back.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import lombok.SneakyThrows;
import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.result.ResultEnum;
import njnu.edu.back.common.utils.LocalUploadUtil;
import njnu.edu.back.common.utils.ZipOperate;
import njnu.edu.back.dao.FileMapper;
import njnu.edu.back.dao.UploadRecordMapper;
import njnu.edu.back.pojo.File;
import njnu.edu.back.pojo.UploadRecord;
import njnu.edu.back.pojo.dto.AddFileDTO;
import njnu.edu.back.service.FileService;
import njnu.edu.back.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/05/02/15:45
 * @Description:
 */
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    FileMapper fileMapper;

    @Value("${basedir}")
    String basedir;

    @Autowired
    RedisService redisService;

    @Autowired
    UploadRecordMapper uploadRecordMapper;
    

    @Override
    public String addFile(AddFileDTO addFileDTO, String email) {
        addFileDTO.setUploader(email);
        String uuid = UUID.randomUUID().toString();
        addFileDTO.setId(uuid);
        fileMapper.addFile(addFileDTO);
        return uuid;
    }

    @Override
    public List<Map<String, Object>> findByLevel(int level, String email) {
        return fileMapper.findByLevel(level, email);
    }

    @Override
    public List<Map<String, Object>> findByParentId(String parentId, String email) {
        return fileMapper.findByParentId(parentId, email);
    }

    @Override
    public List<String> getNoUpload(String MD5, String email, int total, JSONObject metaData) {
        List<String> result = LocalUploadUtil.getNoUploadChunk(MD5, basedir + email, total);
        if(result.size() == total) {
            redisService.set(MD5, metaData);
        }
        return result;
    }

    @Override
    public void uploadFile(MultipartFile multipartFile, String MD5, String email, String name) {
        String dir = basedir + email;
        LocalUploadUtil.UploadFile(multipartFile, name, dir, MD5);
    }

    @Override
    public String mergeFile(String email, String MD5, String uid) {
        String uuid = UUID.randomUUID().toString();
        JSONObject metaData = (JSONObject) redisService.get(MD5);
        if(metaData == null) {
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        }
        redisService.del(MD5);
        String name = metaData.getStr("name");
        int total = metaData.getInt("total");
        int level = metaData.getInt("level");
        String parentId = metaData.getStr("parentId");
        String meta = metaData.getStr("meta");
        String size = metaData.getStr("size");
        String suffix = name.substring(name.lastIndexOf(".") + 1);
        String from = basedir + email + "\\temp\\" + MD5;
        String to = basedir + email + "\\upload\\" + uuid + "." + suffix;
        String key = UUID.randomUUID().toString();
        redisService.set(key, 0, 24*60*3l);
        new Thread() {
            @Override
            @SneakyThrows
            public void run() {
                int state = LocalUploadUtil.merge(from, to, total);
                if(state == 1) {
                    redisService.set(key, 1, 24*60*3l);
                    AddFileDTO addFileDTO = new AddFileDTO(uuid, name, to, uuid + "." + suffix, level, parentId, email, meta, false, size);
                    fileMapper.addFile(addFileDTO);
                    uploadRecordMapper.addUploadRecord(new UploadRecord(uid, name, email, 1, null));
                } else {
                    redisService.set(key, -1, 24*60*3l);
                    uploadRecordMapper.addUploadRecord(new UploadRecord(uid, name, email, -1, null));
                }
                LocalUploadUtil.deleteFolder(from);
            }
        }.start();
        return key;
    }

    @Override
    public int checkMergeState(String key) {
        int state = (int) redisService.get(key);
        if(state == 1 || state == -1) {
            redisService.del(key);
        }
        return state;
    }

    @Override
    public void rename(String id, String name) {
        fileMapper.rename(id, name);
    }

    @Override
    public void deleteFilesOrFolders(JSONObject jsonObject) {
        List<String> files = (List<String>) jsonObject.get("files");
        List<String> folders = (List<String>) jsonObject.get("folders");
        if(files.size() > 0) {
            List<Map<String, Object>> fileMaps = fileMapper.findDeleteById(files);
            fileMapper.batchDelete(files);
            for(Map<String, Object> map : fileMaps) {
                LocalUploadUtil.deleteFolder((String) map.get("address"));
            }
        }
        if(folders.size() > 0) {
            List<Map<String, Object>> folderMaps = fileMapper.recursionFindFiles(folders);
            fileMapper.batchDeleteFolder(folders);
            for(Map<String, Object> map : folderMaps) {
                LocalUploadUtil.deleteFolder((String) map.get("address"));
            }
        }

    }

    @Override
    public void getAvatar(String pictureName, HttpServletResponse response) {
        String path = basedir + "other\\avatar\\" + pictureName;
        java.io.File file = new java.io.File(path);
        if(!file.exists()) {
            return;
        }
        InputStream in = null;
        ServletOutputStream sos = null;
        try {
            in = new FileInputStream(file);
            sos = response.getOutputStream();
            byte[] b = new byte[1024];
            while(in.read(b) != -1) {
                sos.write(b);
            }
            sos.flush();
            in.close();
            sos.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        } finally {
            try {
                if(in != null) {
                    in.close();
                }
                if(sos != null) {
                    sos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
            }
        }

    }



    /**
    * @Description:在线解压操作
    * @Author: Yiming
    * @Date: 2022/6/8
    */

    @Override
    public void unPack(String id, String parentId, int level, String email) {
        Map<String, Object> fileMap = fileMapper.findById(id);
        String filePath = (String) fileMap.get("address");
        String destinationPath = basedir + email + "\\upload";
        List<Map<String, String>> folderList = new ArrayList<>();
        List<Map<String, String>> fileList = new ArrayList<>();
        ZipOperate.getPath(filePath, destinationPath, folderList, fileList);
        List<File> files = new ArrayList<>();
        for(Map<String, String> map : folderList) {
            String name = map.get("name");
            String[] strs = name.split("/");
            if(strs.length == 1) {
                File temp = new File(map.get("id"), strs[0], "", "", level, parentId, null, email, "", true, "");
                files.add(temp);
            } else {
                for(Map<String, String> mapParent : folderList) {
                    if(mapParent.get("name").equals(name.substring(0, name.length() - strs[strs.length - 1].length()))) {
                        File temp = new File(map.get("id"), strs[strs.length - 1], "", "", level + strs.length - 1, mapParent.get("id"), null, email, "", true, "");
                        files.add(temp);
                    }
                }
            }
        }
        for(Map<String, String> map : fileList) {
            String name = map.get("name");
            String[] strs = name.split("/");
            String suffix = strs[strs.length - 1].substring(strs[strs.length - 1].lastIndexOf(".") + 1);
            java.io.File file = new java.io.File(destinationPath + "\\" + map.get("id") + "." + suffix);
            if(!file.exists()) {
                ZipOperate.delUnPackFile(fileList, destinationPath);
                throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
            }
            if(strs.length == 1) {
                File temp = new File(map.get("id"), strs[0], destinationPath + "\\" + map.get("id") + "." + suffix, map.get("id") + "." + suffix, level, parentId, null, email, "", false, LocalUploadUtil.getFileSize(file.length()));
                files.add(temp);
            } else {
                for(Map<String, String> mapParent : folderList) {
                    if(mapParent.get("name").equals(name.substring(0, name.length() - strs[strs.length - 1].length()))) {
                        File temp = new File(map.get("id"), strs[strs.length - 1], destinationPath + "\\" + map.get("id") + "." + suffix, map.get("id") + "." + suffix, level + strs.length - 1, mapParent.get("id"), null, email, "", false, LocalUploadUtil.getFileSize(file.length()));
                        files.add(temp);
                    }
                }
            }
        }

        try {
            int state = fileMapper.batchInsert(files);
            if(state == 0) {
                ZipOperate.delUnPackFile(fileList, destinationPath);
                throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
            }
        } catch (Exception e) {
            ZipOperate.delUnPackFile(fileList, destinationPath);
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        }
    }

    @Override
    public List<Map<String, Object>> getFolderTree(String email) {
        List<Map<String, Object>> folderList = fileMapper.selectFolder(email);
        List<Map<String, Object>> nodeTree = new ArrayList<>();
        List<Map<String, Object>> tempList = nodeTree;
        List<Map<String, Object>> children = new ArrayList<>();
        int level = 0;
        for(int i = 0; i < folderList.size(); i++) {
            Map<String, Object> map = folderList.get(i);
            if((int) map.get("level") == level) {
                Map<String, Object> tempMap = new HashMap<>();
                tempMap.put("id", map.get("id").toString());
                tempMap.put("name", map.get("name"));
                tempMap.put("level", map.get("level"));
                tempMap.put("children", new ArrayList<>());
                if(level == 0) {
                    nodeTree.add(tempMap);
                } else {
                    for(int j = 0; j < tempList.size(); j++) {
                        String str1 = tempList.get(j).get("id").toString();
                        String str2 = (String) map.get("parent_id");
                        if(str1.equals(str2)) {
                            ((List<Map<String, Object>>) tempList.get(j).get("children")).add(tempMap);
                        }
                    }
                }
            } else {
                if(level != 0) {
                    int size = tempList.size();
                    children.clear();
                    for (int j = 0; j < size; j++) {
                        Map<String, Object> temp = tempList.get(j);
                        for(Map<String, Object> child : (List<Map<String, Object>>) temp.get("children")) {
                            children.add(child);
                        }
                    }
                    tempList = children;

                }
                i = i - 1;
                level = level + 1;
            }
        }
        return nodeTree;
    }

    /**
    * @Description:移动文件操作
    * @Author: Yiming
    * @Date: 2022/6/10
    */

    @Override
    public void updateParentIdAndLevel(JSONObject jsonObject) {
        String parentId = jsonObject.getStr("parentId");
        int levelFrom = jsonObject.getInt("levelFrom");
        int levelTo = jsonObject.getInt("levelTo");
        int levelDifference = levelTo - levelFrom;
        List<String> fileList = (List<String>) jsonObject.get("files");
        List<String> folderList = (List<String>) jsonObject.get("folders");
        if(fileList.size() > 0) {
            fileMapper.updateFileParentIdAndLevel(fileList, parentId, levelDifference);
        }
        if(folderList.size() > 0) {
            fileMapper.updateFolderParentIdAndLevel(folderList, parentId, levelDifference);
        }
    }

    /**
    * @Description:文件压缩
    * @Author: Yiming
    * @Date: 2022/6/10
    */

    @Override
    public void compressFile(JSONObject jsonObject, String email) {
        String destination = basedir + email + "\\upload";
        List<String> fileList = (List<String>) jsonObject.get("files");
        List<String> folderList = (List<String>) jsonObject.get("folders");
        List<Map<String, Object>> fileMaps = null;
        List<Map<String, Object>> folderMaps = null;
        if(fileList.size() > 0) {
            fileMaps = fileMapper.selectFilePath(fileList);
        }
        if(folderList.size() > 0) {
            folderMaps = fileMapper.selectFolderPath(folderList);
        }
        if(fileMaps != null) {
            ZipOperate.compressFile(destination, fileMaps);
        }
        if(folderMaps != null) {
            ZipOperate.compressFile(destination, folderMaps);
        }
    }
}
