package njnu.edu.back.dao;

import njnu.edu.back.proj.dto.AddProject;
import njnu.edu.back.proj.support.projectJson.ProjectJsonBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/04/07/17:31
 * @Description:
 */
@Repository
public interface ProjectMapper {
    void addProject(AddProject addProject);

    String getResult(@Param("id") Integer id);

    List<Map<String, Object>> getProjectId(@Param("email") String email);

    int setResult(@Param("result") String result, @Param("id") int id);

    String getResultByEmailAndProjectName(@Param("email") String email, @Param("projectName") String projectName);
}
