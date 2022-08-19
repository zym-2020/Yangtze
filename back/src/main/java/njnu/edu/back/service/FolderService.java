package njnu.edu.back.service;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/08/19/16:49
 * @Description:
 */
public interface FolderService {
    List<Map<String, Object>> findByParentId(String parentId);
}
