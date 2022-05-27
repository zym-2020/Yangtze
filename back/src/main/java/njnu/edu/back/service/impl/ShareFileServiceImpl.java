package njnu.edu.back.service.impl;

import com.alibaba.fastjson.JSONObject;
import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.result.ResultEnum;
import njnu.edu.back.common.utils.LocalUploadUtil;
import njnu.edu.back.dao.BrowseHistoryMapper;
import njnu.edu.back.dao.FileMetaMapper;
import njnu.edu.back.dao.ShareFileMapper;
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
    public void addShareFile(JSONObject jsonObject, String email, MultipartFile file) {
        String uuid = UUID.randomUUID().toString();
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        LocalUploadUtil.uploadAvatar(basedir + "other\\avatar\\" + uuid + "." + suffix, file);
        FileMeta fileMeta = jsonObject.getObject("meta", FileMeta.class);
        ShareFile shareFile = jsonObject.getObject("fileInfo", ShareFile.class);
        String meta = fileMetaMapper.addFileMeta(fileMeta);
        shareFile.setMeta(meta);
        shareFile.setCreator(email);
        shareFile.setDownload(0);
        shareFile.setWatch(0);
        shareFile.setAvatar("/file/avatar/" + uuid + "." + suffix);
        shareFileMapper.addShareFile(shareFile);
    }

    @Override
    public Map<String, Object> pageQueryByAdmin(int page, int size, String property, boolean flag, String keyWord) {
        Map<String, Object> map = new HashMap<>();
        map.put("total", shareFileMapper.countAll());
        keyWord = "%" + keyWord + "%";
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
    public void updateShareFileAndFileMetaNoAvatar(UpdateShareFileAndFileMetaDTO updateShareFileAndFileMetaDTO) {
        shareFileMapper.updateFileInfoAndFileMeta(updateShareFileAndFileMetaDTO);
    }

    @Override
    public void updateShareFileAndFileMeta(UpdateShareFileAndFileMetaDTO updateShareFileAndFileMetaDTO, MultipartFile multipartFile) {
        String uuid = UUID.randomUUID().toString();
        String suffix = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1);
        LocalUploadUtil.uploadAvatar(basedir + "other\\avatar\\" + uuid + "." + suffix, multipartFile);
        updateShareFileAndFileMetaDTO.setAvatar("/file/avatar/" + uuid + "." + suffix);
        shareFileMapper.updateFileInfoAndFileMeta(updateShareFileAndFileMetaDTO);
    }
}
