package njnu.edu.back.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/12/01/10:46
 * @Description:
 */
public interface MultiSourceService {
    List<Map<String, Object>> getBuoyByBox(double top, double right, double bottom, double left);

    List<Map<String, Object>> getBuoyMoveInfoByTimestamp(long startTime, long endTime);

    void getPhoto(String fileName, HttpServletResponse response);

    List<Map<String, Object>> getParkInfoByBox(double top, double right, double bottom, double left);

    List<Map<String, Object>> getAnchorInfoByBox(double top, double right, double bottom, double left);

    List<Map<String, Object>> getOtherInfoBox(double top, double right, double bottom, double left);

    JSONArray getMeteorologyBox(double top, double right, double bottom, double left);

    List<Map<String, Object>> getStationBox(double top, double right, double bottom, double left);

    List<JSONObject> getWeatherInfoById(String id);
}
