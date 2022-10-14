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

    String addProject(String projectName, MultipartFile file, String email);

    Map<String, Object> getAll(String keyword, int page, int size);

    Map<String, Object> getProjectInfo(String projectId);

    void addData(String projectId, List<Map<String, String>> list);

    List<Map<String, Object>> getData(String projectId);

    void delData(String projectId, String dataListId, String fileId);

    void updateLayer(String projectId, List<String> list);

    List<Map<String, Object>> getLayersInfo(String projectId);


//    void addLayer(Layer layer, String projectId);
//
//    Project addLayers(List<Layer> layers, String projectId);
//
//    Layer addSection(Layer layer, String projectId, String email);
//
//    List<Map<String, Object>> getSectionValue(String sectionId, String projectId, String email, List<String> valueIds);
//
//    Project getProjectInfo(String projectId);
//
//    Page<Project> getAll(int page, int size, String keyWord);
//
//    List<Project> getProjectsByEmail(String email);
//
//    int checkState(String projectId, String layerId);
//
//    void delLayer(String projectId, String layerId, String email);
//
//    String getFlushId(String projectId, String benchmark, String reference, String name);
//
//    String computeContour(String projectId, String demId, String email, String interval, String shpName, String srid);
//
//    int checkContourState(String uid);
//
//    void sortLayer(String projectId, List<Layer> layers);
//
//    String addRegion(String projectId, JSONArray jsonArray, String demId, String email);
//
//    Layer getRegionLayer(String projectId, String layerId);
//
//    int checkAddRegion(String key);
//
//    void getRegion(String projectId, String layerId, String Email, HttpServletResponse response);
//
//    void getRegion(String pngName ,String Email, HttpServletResponse response);
}
