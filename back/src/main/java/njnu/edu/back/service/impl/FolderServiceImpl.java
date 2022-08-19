package njnu.edu.back.service.impl;

import njnu.edu.back.dao.main.FolderMapper;
import njnu.edu.back.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public List<Map<String, Object>> findByParentId(String parentId) {
        return folderMapper.findByParentId(parentId);
    }
}
