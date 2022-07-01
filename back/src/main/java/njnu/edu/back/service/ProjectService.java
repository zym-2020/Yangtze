package njnu.edu.back.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import njnu.edu.back.pojo.Project;
import njnu.edu.back.pojo.dto.AddProject;
import njnu.edu.back.pojo.support.Layer;
import njnu.edu.back.pojo.support.projectJson.ProjectJsonBean;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

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
//    Map<String, Object> addProject(AddProject addProject, String email, MultipartFile multipartFile);
//
//    Map<String, Object> addProjectWithoutAvatar(AddProject addProject, String email);
//
//    String getResultById(String id);
//
//    List<Map<String, Object>> getProjectsByEmail(String email);
//
//    int setResult(ProjectJsonBean result, String id);
//
//    void saveSectionValue(String id, String DEMId, Double lat1, Double lon1, Double lat2, Double lon2, String email, String projectId);
//
//    List<Double> getSectionValue(String id, String email, String projectId);
//
//    void delSection(String email, String projectId, String sectionId);
//
//    void saveSectionContrastValue(String id, Double lat1, Double lon1, Double lat2, Double lon2, List<String> demIds, String email, String projectId);
//
//    Map<String, List<Double>> getSectionContrastValue(String email, String projectId, String sectionId);
//
//    JSONObject pageQuery(int size, int page, String keyWord);
//
//    Map<String, Object> findProjectById(String projectId);


    Project addProject(Project project);

    Project addProject(String jsonString, MultipartFile file);

    void addLayer(Layer layer, String projectId);

    Project addLayers(List<Layer> layers, String projectId);

    Project getProjectInfo(String projectId);

    Page<Project> getAll(int page, int size, String keyWord);

    List<Project> getProjectsByEmail(String email);
}
