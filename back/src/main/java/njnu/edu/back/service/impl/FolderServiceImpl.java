package njnu.edu.back.service.impl;

import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.result.ResultEnum;
import njnu.edu.back.dao.main.FolderMapper;
import njnu.edu.back.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/08/19/16:49
 * @Description:
 */
@Service
public class FolderServiceImpl implements FolderService {
    @Autowired
    FolderMapper folderMapper;

    @Value("${basePath}")
    String basePath;


    @Override
    public Map<String, Object> addFolder(String parentId, String folderName, String email) {
        if(parentId.equals("")) {
            String address = email + "/" + "upload/" + folderName;
            String path = basePath + address;
            File file = new File(path);
            if(!file.mkdirs()) {
                throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
            }
            return folderMapper.addFolder(parentId, folderName, email, address);
        } else {
            Map<String, Object> map = folderMapper.findById(parentId);
            String address = (String) map.get("address");
            String path = basePath + address + "/" + folderName;
            File file = new File(path);
            if(!file.mkdirs()) {
                throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
            }
            return folderMapper.addFolder(parentId, folderName, email, address + "/" + folderName);
        }
    }
}
