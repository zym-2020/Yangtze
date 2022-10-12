package njnu.edu.back.dao.main;

import njnu.edu.back.pojo.Project;
import njnu.edu.back.pojo.dto.AddProject;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/04/07/17:31
 * @Description:
 */
@Repository
public interface ProjectMapper {
    String addProject(@Param("projectName") String projectName, @Param("email") String email, @Param("avatar") String avatar, @Param("layerManage") String[] layerManage);

    List<Map<String, Object>> fuzzyQuery(@Param("keyword") String keyword, @Param("size") int size, @Param("start") int start);

    int fuzzyCount(@Param("keyword") String keyword);

    Map<String, Object> getProjectInfo(@Param("projectId") String projectId);

    void updateLayer(@Param("projectId") String projectId, @Param("layers") String[] layers);
}
