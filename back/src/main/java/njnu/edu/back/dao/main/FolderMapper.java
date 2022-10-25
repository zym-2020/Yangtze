package njnu.edu.back.dao.main;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/08/19/16:21
 * @Description:
 */
@Repository
public interface FolderMapper {
    List<Map<String, Object>> findListById(@Param("list") List<String> list);

    void batchDelete(@Param("list") List<String> list);

    List<Map<String, Object>> findByParentId(@Param("parentId") String parentId, @Param("email") String email);

    Map<String, Object> findById(@Param("id") String id);

    Map<String, Object> addFolder(@Param("parentId") String parentId, @Param("folderName") String folderName, @Param("email") String email, @Param("address") String address);
}
