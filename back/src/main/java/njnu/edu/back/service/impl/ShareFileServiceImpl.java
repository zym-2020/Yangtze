package njnu.edu.back.service.impl;

import com.alibaba.fastjson.JSONObject;
import njnu.edu.back.dao.FileMetaMapper;
import njnu.edu.back.dao.ShareFileMapper;
import njnu.edu.back.pojo.FileMeta;
import njnu.edu.back.pojo.ShareFile;
import njnu.edu.back.service.ShareFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        shareFileMapper.addShareFile(shareFile);
    }
}
