package njnu.edu.back.service.impl;

import com.alibaba.fastjson.JSONObject;
import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.result.ResultEnum;
import njnu.edu.back.common.utils.LocalUploadUtil;
import njnu.edu.back.dao.main.BrowseHistoryMapper;
import njnu.edu.back.dao.main.FileMetaMapper;
import njnu.edu.back.dao.main.ShareFileMapper;
import njnu.edu.back.pojo.BrowseHistory;
import njnu.edu.back.pojo.FileMeta;
import njnu.edu.back.pojo.ShareFile;
import njnu.edu.back.pojo.dto.UpdateShareFileAndFileMetaDTO;
import njnu.edu.back.service.ShareFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/05/17/16:56
 * @Description:
 */
@Service
public class ShareFileServiceImpl implements ShareFileService {

    @Autowired
    ShareFileMapper shareFileMapper;

    @Autowired
    FileMetaMapper fileMetaMapper;

    @Autowired
    BrowseHistoryMapper browseHistoryMapper;

    @Value("${basedir}")
    String basedir;

    @Override
    public Map<String, Object> addShareFile(JSONObject jsonObject, String email, MultipartFile file,MultipartFile file2) {
        Map<String, Object> result = new HashMap<>();
        FileMeta fileMeta = jsonObject.getObject("meta", FileMeta.class);
        ShareFile shareFile = jsonObject.getObject("fileInfo", ShareFile.class);
        String meta = fileMetaMapper.addFileMeta(fileMeta);
        String uuidDea = UUID.randomUUID().toString();
        shareFile.setId(uuidDea);
        shareFile.setMeta(meta);
        shareFile.setCreator(email);
        shareFile.setDownload(0);
        shareFile.setWatch(0);
        result.put("list", uuidDea);
        if(!file.isEmpty()) {
            String uuid = UUID.randomUUID().toString();
            String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
            LocalUploadUtil.uploadAvatar(basedir + "other\\avatar\\" + uuid + "." + suffix, file);
            shareFile.setAvatar("/file/avatar/" + uuid + "." + suffix);
        } else {
            shareFile.setAvatar("");
        }
        if(!file2.isEmpty()) {
            String uuid = UUID.randomUUID().toString();
            String suffix = file2.getOriginalFilename().substring(file2.getOriginalFilename().lastIndexOf(".") + 1);
            LocalUploadUtil.uploadAvatar(basedir + "other\\thumbnail\\" + uuid + "." + suffix, file2);
            shareFile.setThumbnail("/file/thumbnail/" + uuid + "." + suffix);
        } else {
            shareFile.setThumbnail("");
        }
        shareFile.setStatus(0);
        shareFileMapper.addShareFile(shareFile);



        return result;
    }

    @Override
    public Map<String, Object> pageQueryByAdmin(int page, int size, String property, boolean flag, String keyWord) {
        Map<String, Object> map = new HashMap<>();
        keyWord = "%" + keyWord + "%";
        map.put("total", shareFileMapper.countFuzzyQuery(keyWord));
        if(flag) {
            map.put("list", shareFileMapper.pageQueryByAdminASC(size, size * page, property, keyWord));
        } else {
            map.put("list", shareFileMapper.pageQueryByAdminDESC(size, size * page, property, keyWord));
        }
        return map;
    }

    @Override
    public Map<String, Object> fuzzyQueryClassify(int page, int size, String property, boolean flag, String keyWord, String[] tags) {
        Map<String, Object> result = new HashMap<>();
        keyWord = "%" + keyWord + "%";
        result.put("total", shareFileMapper.countFuzzyQueryClassify(keyWord, tags));
        if(flag) {
            result.put("list", shareFileMapper.fuzzyQueryClassifyASC(size, size * page, property, keyWord, tags));
        } else {
            result.put("list", shareFileMapper.fuzzyQueryClassifyDESC(size, size * page, property, keyWord, tags));
        }
        return result;
    }

    @Override
    public Map<String, Object> getFileInfoAndMeta(String id) {
        ShareFile shareFileMap = shareFileMapper.getShareFileById(id);
        if(shareFileMap == null) {
            throw new MyException(ResultEnum.NO_OBJECT);
        }
        Map<String, Object> metaFileMap = fileMetaMapper.getFileMetaById(shareFileMap.getMeta());
        if(metaFileMap == null) {
            throw new MyException(ResultEnum.NO_OBJECT);
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("fileInfo", shareFileMap);
        resultMap.put("fileMeta", metaFileMap);
        return resultMap;
    }

    @Override
    public Map<String, Object> getFileInfoAndMetaAndUserInfo(String id) {
        ShareFile shareFileMap = shareFileMapper.getShareFileById(id);
        if(shareFileMap == null) {
            throw new MyException(ResultEnum.NO_OBJECT);
        }
        Map<String, Object> metaFileAndUserInfoMap = fileMetaMapper.getFileMetaAndUserInfo(shareFileMap.getMeta(), shareFileMap.getCreator());
        if(metaFileAndUserInfoMap == null) {
            throw new MyException(ResultEnum.NO_OBJECT);
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("fileInfo", shareFileMap);
        resultMap.put("fileMeta", metaFileAndUserInfoMap);
        return resultMap;

    }

    @Override
    public void addWatchCount(String id, String userId, String ip) {
        shareFileMapper.addWatchCount(id);
        browseHistoryMapper.addHistory(new BrowseHistory(null, userId, null, ip, id));
    }

    @Override
    public Map<String, Object> fuzzyQuery(int page, int size, String property, boolean flag, String keyWords) {
        Map<String, Object> result = new HashMap<>();
        keyWords = "%" + keyWords + "%";
        result.put("total", shareFileMapper.countFuzzyQuery(keyWords));
        if(flag) {
            result.put("list", shareFileMapper.fuzzyQueryASC(size, page * size, property, keyWords));
        } else {
            result.put("list", shareFileMapper.fuzzyQueryDESC(size, page * size, property, keyWords));
        }
        return result;
    }

    @Override
    public Map<String, Object> pageQueryByEmail(String email, int page, int size) {
        Map<String, Object> result = new HashMap<>();
        result.put("total", shareFileMapper.countPageQueryByEmail(email));
        result.put("list", shareFileMapper.pageQueryByEmail(email, page * size, size));
        return result;
    }

    @Override
    public void updateShareFileAndFileMetaNoAvatar(UpdateShareFileAndFileMetaDTO updateShareFileAndFileMetaDTO) {
        shareFileMapper.updateFileInfoAndFileMeta(updateShareFileAndFileMetaDTO);
    }

    @Override
    public void updateShareFileAndFileMeta(UpdateShareFileAndFileMetaDTO updateShareFileAndFileMetaDTO, MultipartFile multipartFile,MultipartFile multipartFile2) {
        String uuid = UUID.randomUUID().toString();
        String uuid2 = UUID.randomUUID().toString();
        String suffix = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1);
        String suffix2 = multipartFile.getOriginalFilename().substring(multipartFile2.getOriginalFilename().lastIndexOf(".") );
        LocalUploadUtil.uploadAvatar(basedir + "other\\avatar\\" + uuid + "." + suffix, multipartFile);
        LocalUploadUtil.uploadAvatar(basedir + "other\\thumbnail\\" + uuid2 + "." + suffix2, multipartFile2);
        updateShareFileAndFileMetaDTO.setAvatar("/file/avatar/" + uuid + "." + suffix);
        updateShareFileAndFileMetaDTO.setThumbnail("/file/thumbnail/" + uuid2 + "." + suffix2);
        shareFileMapper.updateFileInfoAndFileMeta(updateShareFileAndFileMetaDTO);
    }
    @Override
    public void updateShareFileAndFileMeta(UpdateShareFileAndFileMetaDTO updateShareFileAndFileMetaDTO, MultipartFile multipartFile,int flag){
            String uuid = UUID.randomUUID().toString();
            if(flag==3){
                String suffix = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1);
                LocalUploadUtil.uploadAvatar(basedir + "other\\avatar\\" + uuid + "." + suffix, multipartFile);
                updateShareFileAndFileMetaDTO.setAvatar("/file/avatar/" + uuid + "." + suffix);
                shareFileMapper.updateFileInfoAndFileMeta(updateShareFileAndFileMetaDTO);
            }
            else if (flag==2)
            {
                String suffix2 = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") );
                LocalUploadUtil.uploadAvatar(basedir + "other\\thumbnail\\" + uuid + "." + suffix2, multipartFile);
                updateShareFileAndFileMetaDTO.setThumbnail("/file/thumbnail/" + uuid + "." + suffix2);
                updateShareFileAndFileMetaDTO.setThumbnail("/file/thumbnail/" + uuid + "." + suffix2);
                shareFileMapper.updateFileInfoAndFileMeta(updateShareFileAndFileMetaDTO);
            }
            else {
                shareFileMapper.updateFileInfoAndFileMeta(updateShareFileAndFileMetaDTO);

            }
        }

    @Override
    public List<Map<String, Object>> deleteShareFileById(int page, int size, String property, String keyWord, String id) {
        keyWord = "%" + keyWord + "%";
        return shareFileMapper.deleteShareFileById(id, size, size * page, property, keyWord);
    }

    @Override
    public void updateStatusById(String id, int status) {
        shareFileMapper.updateStatusById(id, status);
    }

    @Override
    public void offlineById(String id) {
        shareFileMapper.offlineById(id);
    }

    @Override
    public void examineById(String id) {
        shareFileMapper.examineById(id);
    }

    @Override
    public void onlineById(String id) {
        shareFileMapper.onlineById(id);
    }


    @Override
    public List<ShareFile> deleteShareFileAsMember(String id, int size, int page, String email) {

        return shareFileMapper.deleteShareFileAsMember(id, size, page * size, email);
    }

    @Override
    public Map<String, Object> getShareFileById(String id) {
        Map<String, Object> result = new HashMap<>();
        result.put("list", shareFileMapper.getShareFileById(id));
        return result;
    }

    @Override
    public Map<String, Object> getOtherTags(String id){
        Map<String, Object> result = new HashMap<>();
        result.put("list", shareFileMapper.getOtherTags(id));
        return result;
    }
}
