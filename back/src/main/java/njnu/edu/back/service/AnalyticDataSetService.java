package njnu.edu.back.service;

import com.alibaba.fastjson.JSONObject;
import njnu.edu.back.pojo.ModelConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/06/14/21:31
 * @Description:
 */
public interface AnalyticDataSetService {
//    List<Map<String, Object>> findDataByType(String type);
//
//    void getRaster(String id, String x, String y, String z, HttpServletResponse response);
//
//    void getSlope(String rasterId, String x, String y, String z, HttpServletResponse response);
//
//    Object getVectorTile(String tableName, int x, int y, int z);

    List<Map<String, Object>> getAnalyticData(String projectId);

    String addDraw(JSONObject jsonObject, String email);

    void delAnalyticData(String id);

    Map<String, Object> checkState(String key);

    String addSection(String projectId, String sectionId, String demId, String email, String fileName);

    String addSectionCompare(String projectId, String sectionId, String email, List<String> demList, String fileName);

    String addSectionFlush(String projectId, String sectionId, String benchmarkId, String referId, String email, String fileName);

    String addRegionFlush(String projectId, String regionId, String benchmarkId, String referId, String email, String fileName);

    String computeVolume(double deep, String projectId, String regionId, String demId, String email, String fileName);

    Map<String, Object> addElevationFlush(String projectId, String benchmarkId, String referId, String email, String fileName);

    Map<String, Object> addFlushContour(String projectId, String benchmarkId, String referId, String email, String fileName);

    Map<String, Object> addSlope(String projectId, String demId, String email, String fileName);

    JSONObject getAllModels();

    ModelConfig getModelConfig(String id);

    Map<String, String> uploadParameter(MultipartFile file, String projectId, String email);

    String generateConfig(JSONObject jsonObject, String email);

    Map<String, String> predict(String projectId, String config, String email);

    void rename(String id, String name);

    String getUrl(String id, String userId);

    void downloadAnalyticData(String userId, String id, HttpServletResponse response);
}
