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
    Map<String, Object> addProject(AddProject addProject);

    String getResult(@Param("id") String id);

    List<Map<String, Object>> getProjectsByEmail(@Param("email") String email);

    int setResult(@Param("result") String result, @Param("id") String id);

    String getResultByEmailAndProjectName(@Param("email") String email, @Param("projectName") String projectName);

    List<Map<String, Object>> pageQuery(@Param("size") int size, @Param("start") int start, @Param("keyWord") String keyWord);

    Map<String, Object> findProjectById(@Param("id") String id);

    int countAll(@Param("keyWord") String keyWord);
}
