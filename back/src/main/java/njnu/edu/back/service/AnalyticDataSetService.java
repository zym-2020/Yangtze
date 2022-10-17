package njnu.edu.back.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

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


}
