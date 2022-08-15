package njnu.edu.back.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import njnu.edu.back.pojo.Project;
import njnu.edu.back.pojo.dto.AddProject;
import njnu.edu.back.pojo.support.Layer;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
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

    Project addProject(Project project, String email);

    Project addProject(String jsonString, MultipartFile file, String email);

    void addLayer(Layer layer, String projectId);

    Project addLayers(List<Layer> layers, String projectId);

    Layer addSection(Layer layer, String projectId, String email);

    List<Map<String, Object>> getSectionValue(String sectionId, String projectId, String email, List<String> valueIds);

    Project getProjectInfo(String projectId);

    Page<Project> getAll(int page, int size, String keyWord);

    List<Project> getProjectsByEmail(String email);

    int checkState(String projectId, String layerId);

    void delLayer(String projectId, String layerId, String email);

    String getFlushId(String projectId, String benchmark, String reference, String name);

    String computeContour(String projectId, String demId, String email, String interval, String shpName, String srid);

    int checkContourState(String uid);

    void sortLayer(String projectId, List<Layer> layers);

    String addRegion(String projectId, JSONArray jsonArray, String demId, String email);

    Layer getRegionLayer(String projectId, String layerId);

    int checkAddRegion(String key);

    void getRegion(String projectId, String layerId, String Email, HttpServletResponse response);
    
    void getRegion(String pngName ,String Email, HttpServletResponse response);
}
