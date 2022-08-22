package njnu.edu.back.service.impl;

import com.alibaba.fastjson.JSONObject;
import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.result.ResultEnum;
import njnu.edu.back.common.utils.LocalUploadUtil;
import njnu.edu.back.dao.main.BrowseHistoryMapper;
import njnu.edu.back.dao.main.ShareFileMapper;
import njnu.edu.back.pojo.BrowseHistory;
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
    BrowseHistoryMapper browseHistoryMapper;

    @Value("${basedir}")
    String basedir;



    @Override
    public Map<String, Object> getShareFileById(String id) {
        Map<String, Object> result = new HashMap<>();
        result.put("list", shareFileMapper.getShareFileById(id));
        return result;
    }


}
