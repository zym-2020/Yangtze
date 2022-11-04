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
    public void uploadFile(MultipartFile multipartFile, String key, String email, String name) {
        String dir = basePath + email;
        LocalUploadUtil.UploadFile(multipartFile, name, dir, key);
    }

    @Override
    public String mergeFile(String email, String tempFolderName, int total, String name, String folderId) {
        String fileName = name.substring(0, name.lastIndexOf(".")) + CommonUtils.getRandomCharStr(6);
        String suffix = name.substring(name.lastIndexOf(".") + 1);

        String address;
        if(!folderId.equals("")) {
            Map<String, Object> map = folderMapper.findById(folderId);
            address = map.get("address") + "/" + fileName + "." + suffix;
        } else {
            address = email +"/upload/" + fileName + "." + suffix;
        }
        String to = basePath + address;
        String from = basePath + email + "/temp/" + tempFolderName;
        String key = UUID.randomUUID().toString();
        redisService.set(key, 0, 3*60l);
        new Thread() {
            @Override
            @SneakyThrows
            public void run() {
                int state = LocalUploadUtil.merge(from, to, total);
                if(state == 1) {
                    java.io.File f = new java.io.File(to);
                    if(f.exists()) {
                        String size = CommonUtils.getFileSize(f.length());
                        redisService.set(key, 1, 60*3l);
                        File file = new File(key, name, address, size, email, "", "", folderId);
                        fileMapper.addFile(file);
                        uploadRecordMapper.addUploadRecord(new UploadRecord(key, name, email,  null, size));
                    }
                } else {
                    redisService.set(key, -1, 60*3l);
                }
                LocalUploadUtil.deleteFolder(from);
            }
        }.start();
        return key;
    }

    @Override
    public int checkMergeState(String key) {
        Integer state = (Integer) redisService.get(key);
        if(state == null || state == -1) {
            redisService.del(key);
            throw new MyException(-99, "合并错误");

        } else {
            if(state == 1) {
                redisService.del(key);
            }
            return state;
        }

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
        Map<String, Object> fileInfo = fileMapper.findInfoById(id);
        String fileName = (String) fileInfo.get("fileName");
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
        Map<String, Object> fileInfo = fileMapper.findInfoById(id);
        String fileName = (String) fileInfo.get("fileName");
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
                fileMapper.addFile(new File(null, f.getName(), address, CommonUtils.getFileSize(f.length()), email, visualType, visualId, ""));
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
            Map<String, Object> map = fileMapper.findByAddress(path.substring(16) + "/" + fileList[i]);
            if(map == null) {
                System.out.println(fileList[i]);
            }
        }
        return null;
    }
}
