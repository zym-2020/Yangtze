package njnu.edu.back.service.impl;

import com.alibaba.fastjson.JSONObject;
import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.result.ResultEnum;
import njnu.edu.back.dao.FileMetaMapper;
import njnu.edu.back.dao.ShareFileMapper;
import njnu.edu.back.pojo.FileMeta;
import njnu.edu.back.pojo.ShareFile;
import njnu.edu.back.service.ShareFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public void addShareFile(JSONObject jsonObject, String email) {
        FileMeta fileMeta = jsonObject.getObject("meta", FileMeta.class);
        ShareFile shareFile = jsonObject.getObject("fileInfo", ShareFile.class);
        String meta = fileMetaMapper.addFileMeta(fileMeta);
        shareFile.setMeta(meta);
        shareFile.setCreator(email);
        shareFile.setDownload(0);
        shareFile.setWatch(0);
        shareFileMapper.addShareFile(shareFile);
    }

    @Override
    public Map<String, Object> pageQueryOrderByDownload(int page, int size) {
        Map<String, Object> map = new HashMap<>();
        map.put("total", shareFileMapper.countAll());
        map.put("list", shareFileMapper.pageQueryOrderByDownload(size, page * size));
        return map;
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
    public void addWatchCount(String id) {
        shareFileMapper.addWatchCount(id);
    }
}
