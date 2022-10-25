package njnu.edu.back.service.impl;

import cn.hutool.json.JSONObject;
import lombok.SneakyThrows;
import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.result.ResultEnum;
import njnu.edu.back.common.utils.CommonUtils;
import njnu.edu.back.common.utils.Encrypt;
import njnu.edu.back.common.utils.LocalUploadUtil;
import njnu.edu.back.common.utils.ZipOperate;
import njnu.edu.back.dao.main.*;
import njnu.edu.back.pojo.DownloadHistory;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
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

    @Value("${basePath}")
    String basePath;

    @Value("${encrypt.key}")
    String key;

    @Autowired
    RedisService redisService;

    @Autowired
    UploadRecordMapper uploadRecordMapper;

    @Autowired
    DataRelationalMapper dataRelationalMapper;

    @Autowired
    FolderMapper folderMapper;

    @Autowired
    DataListMapper dataListMapper;

    @Autowired
    DownloadHistoryMapper downloadHistoryMapper;

    @Autowired
    VisualFileMapper visualFileMapper;

    @Override
    public String addFile(File file, String email) {
        file.setUploader(email);
        String uuid = UUID.randomUUID().toString();
        file.setId(uuid);
        fileMapper.addFile(file);
        return uuid;
    }


    @Override
    public List<Map<String, Object>> findByFolderId(String folderId, String email) {
        List<Map<String, Object>> result = new ArrayList<>();
        if(folderId.equals("-1")) {
            folderId = "";
        }
        result.addAll(folderMapper.findByParentId(folderId, email));
        result.addAll(fileMapper.findByFolderId(folderId, email));
        return result;
    }

    @Override
    public List<String> getNoUpload(String MD5, String email, int total) {
        List<String> result = LocalUploadUtil.getNoUploadChunk(MD5, basePath + email, total);
        return result;
    }

    @Override
    public void uploadFile(MultipartFile multipartFile, String MD5, String email, String name) {
        String dir = basePath + email;
        LocalUploadUtil.UploadFile(multipartFile, name, dir, MD5);
    }

    @Override
    public String mergeFile(String email, String MD5, String uid, int total, String name, String folderId) {
        Map<String, Object> map = folderMapper.findById(folderId);
        String address = (String) map.get("address");
        String fileName = name.substring(0, name.lastIndexOf(".")) + CommonUtils.getRandomCharStr(6);
        String suffix = name.substring(name.lastIndexOf(".") + 1);
        String from = basePath + email + "\\temp\\" + MD5;
        String to = basePath + address + "\\" + fileName + "." + suffix;
        String key = UUID.randomUUID().toString();
        redisService.set(key, 0, 24*60*3l);
        new Thread() {
            @Override
            @SneakyThrows
            public void run() {
                int state = LocalUploadUtil.merge(from, to, total);
                if(state == 1) {
                    java.io.File f = new java.io.File(to);
                    if(f.exists()) {
                        String size = CommonUtils.getFileSize(f.length());
                        redisService.set(key, 1, 24*60*3l);
                        File file = new File(null, name, size, to, "", "", email, "", "", folderId);
                        fileMapper.addFile(file);
                        uploadRecordMapper.addUploadRecord(new UploadRecord(uid, name, email, 1, null));
                    }
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
        Integer state = (Integer) redisService.get(key);
        if(state == null) {
            return -1;
        }
        if(state == 1 || state == -1) {
            redisService.del(key);
        }
        return state;
    }

    @Override
    public void rename(String id, String fileName) {
        fileMapper.rename(id, fileName);
    }

    @Override
    public void deleteFilesOrFolders(JSONObject jsonObject) {
        List<String> files = (List<String>) jsonObject.get("files");
        List<String> folders = (List<String>) jsonObject.get("folders");
        if(files.size() > 0) {
            List<Map<String, Object>> fileMaps = fileMapper.findListById(files);
            fileMapper.batchDelete(files);
            dataRelationalMapper.batchDeleteByDataListId(files);
            for(Map<String, Object> map : fileMaps) {
                LocalUploadUtil.deleteFolder(basePath + map.get("address"));
            }
        }
        if(folders.size() > 0) {
            List<Map<String, Object>> folderMaps = folderMapper.findListById(folders);
            fileMapper.batchDeleteFolder(folders);
            dataRelationalMapper.batchDeleteFolder(folders);
            folderMapper.batchDelete(folders);
            for(Map<String, Object> map : folderMaps) {
                LocalUploadUtil.deleteFolder(basePath + map.get("address"));
            }
        }

    }

    @Override
    public String getDownloadURL(String id, String userId) {
        String uuid = UUID.randomUUID().toString();
        redisService.set(uuid, id, 30l);
        return Encrypt.encryptByUserId(uuid, userId, key.toCharArray());
    }

    @Override
    public void downloadInList(String userId, String id, String dataListId, HttpServletResponse response, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String tempId = (String) redisService.get(id);
        if(tempId == null) {
            throw new MyException(-1, "链接已失效");
        } else {
            redisService.del(id);
            id = tempId;
        }
        Map<String, Object> fileInfo = fileMapper.findById(id);
        String fileName = (String) fileInfo.get("file_name");
        String address = basePath + fileInfo.get("address");
        java.io.File file = new java.io.File(address);
        if(!file.exists()) {
            throw new MyException(ResultEnum.NO_OBJECT);
        }
        InputStream in = null;
        ServletOutputStream sos = null;
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.addHeader("Content-Length", "" + file.length());
            in = new FileInputStream(file);
            sos = response.getOutputStream();
            byte[] b = new byte[1024];
            int len;
            while((len = in.read(b)) > 0) {
                sos.write(b, 0, len);
            }
            sos.flush();
            sos.close();
            in.close();
            dataListMapper.addDownloadCount(dataListId);
            downloadHistoryMapper.addHistory(new DownloadHistory(null, userId, null, ip, dataListId, id));
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

    @Override
    public void downloadLocalFile(String userId, String id, HttpServletResponse response) {
        String tempId = (String) redisService.get(id);
        if(tempId == null) {
            throw new MyException(-1, "链接已失效");
        } else {
            redisService.del(id);
            id = tempId;
        }
        Map<String, Object> fileInfo = fileMapper.findById(id);
        String fileName = (String) fileInfo.get("file_name");
        String address = basePath + fileInfo.get("address");
        java.io.File file = new java.io.File(address);
        if(!file.exists()) {
            throw new MyException(ResultEnum.NO_OBJECT);
        }
        InputStream in = null;
        ServletOutputStream sos = null;
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.addHeader("Content-Length", "" + file.length());
            in = new FileInputStream(file);
            sos = response.getOutputStream();
            byte[] b = new byte[1024];
            int len;
            while((len = in.read(b)) > 0) {
                sos.write(b, 0, len);
            }
            sos.flush();
            sos.close();
            in.close();

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

    //    /**
//    * @Description:在线解压操作
//    * @Author: Yiming
//    * @Date: 2022/6/8
//    */
//
//    @Override
//    public void unPack(String id, String parentId, int level, String email) {
//        Map<String, Object> fileMap = fileMapper.findById(id);
//        String filePath = (String) fileMap.get("address");
//        String destinationPath = basedir + email + "\\upload";
//        List<Map<String, String>> folderList = new ArrayList<>();
//        List<Map<String, String>> fileList = new ArrayList<>();
//        ZipOperate.getPath(filePath, destinationPath, folderList, fileList);
//        List<File> files = new ArrayList<>();
//        for(Map<String, String> map : folderList) {
//            String name = map.get("name");
//            String[] strs = name.split("/");
//            if(strs.length == 1) {
//                File temp = new File(map.get("id"), strs[0], "", "", level, parentId, null, email, "", true, "");
//                files.add(temp);
//            } else {
//                for(Map<String, String> mapParent : folderList) {
//                    if(mapParent.get("name").equals(name.substring(0, name.length() - strs[strs.length - 1].length()))) {
//                        File temp = new File(map.get("id"), strs[strs.length - 1], "", "", level + strs.length - 1, mapParent.get("id"), null, email, "", true, "");
//                        files.add(temp);
//                    }
//                }
//            }
//        }
//        for(Map<String, String> map : fileList) {
//            String name = map.get("name");
//            String[] strs = name.split("/");
//            String suffix = strs[strs.length - 1].substring(strs[strs.length - 1].lastIndexOf(".") + 1);
//            java.io.File file = new java.io.File(destinationPath + "\\" + map.get("id") + "." + suffix);
//            if(!file.exists()) {
//                ZipOperate.delUnPackFile(fileList, destinationPath);
//                throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
//            }
//            if(strs.length == 1) {
//                File temp = new File(map.get("id"), strs[0], destinationPath + "\\" + map.get("id") + "." + suffix, map.get("id") + "." + suffix, level, parentId, null, email, "", false, LocalUploadUtil.getFileSize(file.length()));
//                files.add(temp);
//            } else {
//                for(Map<String, String> mapParent : folderList) {
//                    if(mapParent.get("name").equals(name.substring(0, name.length() - strs[strs.length - 1].length()))) {
//                        File temp = new File(map.get("id"), strs[strs.length - 1], destinationPath + "\\" + map.get("id") + "." + suffix, map.get("id") + "." + suffix, level + strs.length - 1, mapParent.get("id"), null, email, "", false, LocalUploadUtil.getFileSize(file.length()));
//                        files.add(temp);
//                    }
//                }
//            }
//        }
//
//        try {
//            int state = fileMapper.batchInsert(files);
//            if(state == 0) {
//                ZipOperate.delUnPackFile(fileList, destinationPath);
//                throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
//            }
//        } catch (Exception e) {
//            ZipOperate.delUnPackFile(fileList, destinationPath);
//            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
//        }
//    }
//
//    @Override
//    public List<Map<String, Object>> getFolderTree(String email) {
//        List<Map<String, Object>> folderList = fileMapper.selectFolder(email);
//        List<Map<String, Object>> nodeTree = new ArrayList<>();
//        List<Map<String, Object>> tempList = nodeTree;
//        List<Map<String, Object>> children = new ArrayList<>();
//        int level = 0;
//        for(int i = 0; i < folderList.size(); i++) {
//            Map<String, Object> map = folderList.get(i);
//            if((int) map.get("level") == level) {
//                Map<String, Object> tempMap = new HashMap<>();
//                tempMap.put("id", map.get("id").toString());
//                tempMap.put("name", map.get("name"));
//                tempMap.put("level", map.get("level"));
//                tempMap.put("children", new ArrayList<>());
//                if(level == 0) {
//                    nodeTree.add(tempMap);
//                } else {
//                    for(int j = 0; j < tempList.size(); j++) {
//                        String str1 = tempList.get(j).get("id").toString();
//                        String str2 = (String) map.get("parent_id");
//                        if(str1.equals(str2)) {
//                            ((List<Map<String, Object>>) tempList.get(j).get("children")).add(tempMap);
//                        }
//                    }
//                }
//            } else {
//                if(level != 0) {
//                    int size = tempList.size();
//                    children.clear();
//                    for (int j = 0; j < size; j++) {
//                        Map<String, Object> temp = tempList.get(j);
//                        for(Map<String, Object> child : (List<Map<String, Object>>) temp.get("children")) {
//                            children.add(child);
//                        }
//                    }
//                    tempList = children;
//
//                }
//                i = i - 1;
//                level = level + 1;
//            }
//        }
//        return nodeTree;
//    }
//
//    /**
//    * @Description:移动文件操作
//    * @Author: Yiming
//    * @Date: 2022/6/10
//    */
//
//    @Override
//    public void updateParentIdAndLevel(JSONObject jsonObject) {
//        String parentId = jsonObject.getStr("parentId");
//        int levelFrom = jsonObject.getInt("levelFrom");
//        int levelTo = jsonObject.getInt("levelTo");
//        int levelDifference = levelTo - levelFrom;
//        List<String> fileList = (List<String>) jsonObject.get("files");
//        List<String> folderList = (List<String>) jsonObject.get("folders");
//        if(fileList.size() > 0) {
//            fileMapper.updateFileParentIdAndLevel(fileList, parentId, levelDifference);
//        }
//        if(folderList.size() > 0) {
//            fileMapper.updateFolderParentIdAndLevel(folderList, parentId, levelDifference);
//        }
//    }
//
//    /**
//    * @Description:文件压缩
//    * @Author: Yiming
//    * @Date: 2022/6/10
//    */
//
//    @Override
//    public void compressFile(JSONObject jsonObject, String email) {
//        String uuid = UUID.randomUUID().toString();
//        String compressName = jsonObject.getStr("compressName");
//        String parentId = jsonObject.getStr("parentId");
//        int level = jsonObject.getInt("level");
//        String destination = basedir + email + "\\upload\\" + uuid + ".zip";
//        List<String> fileList = (List<String>) jsonObject.get("files");
//        List<String> folderList = (List<String>) jsonObject.get("folders");
//        List<Map<String, Object>> fileMaps = null;
//        List<Map<String, Object>> folderMaps = null;
//        if(fileList.size() > 0) {
//            fileMaps = fileMapper.selectFilePath(fileList);
//        }
//        if(folderList.size() > 0) {
//            folderMaps = fileMapper.selectFolderPath(folderList);
//        }
//        if(fileMaps != null) {
//            ZipOperate.compressFile(destination, fileMaps);
//        }
//        if(folderMaps != null) {
//            ZipOperate.compressFile(destination, folderMaps);
//        }
//        java.io.File file = new java.io.File(destination);
//        if(file.exists()) {
//            fileMapper.addFile(new AddFileDTO(null, compressName, destination, uuid + ".zip", level, parentId, email, "", false, LocalUploadUtil.getFileSize(file.length())));
//        } else {
//            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
//        }
//
//    }


    @Override
    public void importData(String folderPath, String email, String time, String visualType, String visualId) {
        java.io.File file = new java.io.File(folderPath);
        if(!file.exists()) {
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        }
        java.io.File[] files = file.listFiles();
        for(java.io.File f : files) {
            Map<String, Object> map = fileMapper.findByFileName(f.getName());
            if(map == null) {
                String path = f.getAbsolutePath();
                String address = path.substring(basePath.length());
                fileMapper.addFile(new File(null, f.getName(), address, CommonUtils.getFileSize(f.length()), time, "", email, visualType, visualId, ""));
            }
        }
    }

    @Override
    public void importGrid() {
        List<Map<String, Object>> maps = fileMapper.findListByVisualType("movePng");
        for(Map<String, Object> map : maps) {
            String fileName = ((String) map.get("file_name")).substring(0, 15) + "-" + ((String) map.get("address")).charAt(53) + ".png";
//            System.out.println(fileName);
            Map<String, Object> visualMap = visualFileMapper.findByFileName(fileName);
            fileMapper.updateVisualId(map.get("id").toString(), visualMap.get("id").toString());
        }
    }

    @Override
    public List<String> check(String path) {
        java.io.File file = new java.io.File(path);
        String[] fileList = file.list();
        for(int i = 0; i < fileList.length; i++) {
//            System.out.println(path.substring(16) + "\\" + fileList[i]);
            Map<String, Object> map = fileMapper.findByAddress(path.substring(16) + "\\" + fileList[i]);
            if(map == null) {
                System.out.println(fileList[i]);
            }
        }
        return null;
    }
}
