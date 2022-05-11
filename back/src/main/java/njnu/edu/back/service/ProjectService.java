package njnu.edu.back.service;

import com.alibaba.fastjson.JSONObject;
import njnu.edu.back.proj.dto.AddProject;
import njnu.edu.back.proj.support.projectJson.ProjectJsonBean;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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
    void addProject(AddProject addProject, String email, MultipartFile multipartFile);

    void addProjectWithoutAvatar(AddProject addProject, String email);

    String getResultById(String id);

    List<Map<String, Object>> getProjectsByEmail(String email);

    int setResult(ProjectJsonBean result, String id);

    void saveSectionValue(String DEMId, Double lat1, Double lon1, Double lat2, Double lon2, String sectionName, String email, String projectName);

    List<Double> getSectionValue(String email, String projectName, String sectionName, String DEMName, String DEMId);

    void delSection(String email, String projectName, String sectionName, String DEMName);

    void saveSectionContrastValue(Double lat1, Double lon1, Double lat2, Double lon2, String sectionName, String email, String projectName);

    Map<String, List<Double>> getSectionContrastValue(String email, String projectName, String sectionName);

    JSONObject pageQuery(int size, int page);

    Map<String, Object> findProjectById(String projectId);

}
