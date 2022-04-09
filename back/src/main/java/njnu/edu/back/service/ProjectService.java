package njnu.edu.back.service;

import njnu.edu.back.proj.dto.AddProject;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/04/07/17:26
 * @Description:
 */
public interface ProjectService {
    void addProject(AddProject addProject, String email);

    String getResultById(Integer id);

    List<Map<String, Object>> getProjectId(String email);
}
