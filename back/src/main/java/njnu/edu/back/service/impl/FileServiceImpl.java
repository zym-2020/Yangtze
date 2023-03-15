package njnu.edu.back.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.result.ResultEnum;
import njnu.edu.back.common.utils.*;
import njnu.edu.back.dao.main.*;
import njnu.edu.back.pojo.DownloadHistory;
import njnu.edu.back.pojo.File;
import njnu.edu.back.pojo.UploadRecord;
import njnu.edu.back.pojo.VisualFile;
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

    @Value("${tempAddress}")
    String tempAddress;

    @Value("${pgpath}")
    String pgPath;

    @Value("${visualAddress}")
    String visualAddress;

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
    public String bindVisualData(JSONObject jsonObject) {
        String fileId = jsonObject.getStr("id");
        String fileName = jsonObject.getStr("fileName");
        String type = jsonObject.getStr("type");
        JSONArray jsonArray = jsonObject.getJSONArray("coordinates");
        String srid = jsonObject.getStr("srid");
        String content = "";
        if (type.equals("png") || type.equals("movePng")) {
            JSONObject json = new JSONObject();
            json.append("address", "png/" + fileName);
            json.append("coordinates", jsonArray);
            content = JSON.toJSONString(json);
        } else if (type.equals("rateDirection") || type.equals("sandContent") || type.equals("salinity") || type.equals("suspension") || type.equals("flowSand_Z") || type.equals("tide")) {
            if (type.equals("flowSand_Z")) {
                content = "flowSand/" + fileName;
            } else {
                content = type + "/" + fileName;
            }
        } else if (type.equals("rasterTile")) {
            String folderName = fileName.substring(0, fileName.lastIndexOf("."));
            content = "rasterTile/" + folderName + "/tiles";
            ZipOperate.unpack(tempAddress + fileName, visualAddress + "rasterTile/" + folderName + "/tiles");
            fileName = folderName;
        } else if (type.equals("pointVectorTile") || type.equals("pointVectorTile3D") || type.equals("lineVectorTile") || type.equals("lineVectorTile3D") || type.equals("polygonVectorTile") || type.equals("polygonVectorTile3D")) {
            content = fileName.substring(0, fileName.lastIndexOf("."));
            ZipOperate.unpack(tempAddress + content + ".zip", tempAddress + content);
            String shpName = "";
            java.io.File folder = new java.io.File(tempAddress + content);
            String files[] = folder.list();
            for (String name : files) {
                String suffix = name.substring(name.lastIndexOf("."));
                if (suffix.equals(".shp")) {
                    shpName = name;
                    break;
                }
            }
            if (shpName.equals("")) {
                throw new MyException(-99, "文件内容错误");
            }
            List<String> commands = new ArrayList<>();
            String command = "D: & cd " + pgPath + " & shp2pgsql -s " + srid + " -d " + tempAddress + content + "/" + shpName + " " + content + " | psql -h localhost -U postgres -d shp_dataset";
            commands.add("cmd");
            commands.add("/c");
            commands.add(command);
            try {
                Process process = ProcessUtil.cmdShp2Pgsql(commands);
                ProcessUtil.readProcessOutput(process.getInputStream(), System.out);
                int state = process.exitValue();
                LocalUploadUtil.deleteFolder(tempAddress + content);
                LocalUploadUtil.deleteFolder(tempAddress + content + ".zip");
                if (state != 0) {
                    throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
            }
        } else if (type.equals("photo")) {
            fileMapper.updateVisualIdAndType(fileId, "", "photo");
            return "";
        } else {
            throw new MyException(ResultEnum.QUERY_TYPE_ERROR);
        }
        VisualFile visualFile = new VisualFile(null, fileName, type, content);
        Map<String, Object> map = visualFileMapper.addVisualFile(visualFile);
        fileMapper.updateVisualIdAndType(fileId, map.get("id").toString(), type);
        return map.get("id").toString();
    }

    @Override
    public void cancelVisualBind(String id) {
        fileMapper.updateVisualTypeAndVisualId(id, "", "");
    }
}
