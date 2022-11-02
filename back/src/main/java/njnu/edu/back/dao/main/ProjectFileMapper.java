package njnu.edu.back.dao.main;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/10/12/10:34
 * @Description:
 */
@Repository
public interface ProjectFileMapper {
    void addRecord(@Param("projectId") String projectId, @Param("list")List<Map<String, String>> list);

    List<Map<String, Object>> getData(@Param("projectId") String projectId);

    void delData(@Param("projectId") String projectId, @Param("dataListId") String dataListId, @Param("fileId") String fileId);

    void delByProjectId(@Param("projectId") String projectId);
}
