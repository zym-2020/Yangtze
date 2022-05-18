package njnu.edu.back.service.impl;

import njnu.edu.back.dao.FileMetaMapper;
import njnu.edu.back.service.FileMetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/05/17/16:57
 * @Description:
 */
@Service
public class FileMetaServiceImpl implements FileMetaService {

    @Autowired
    FileMetaMapper fileMetaMapper;

    @Override
    public Map<String, Object> getFileMetaById(String id) {
        return fileMetaMapper.getFileMetaById(id);
    }
}
