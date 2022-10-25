package njnu.edu.back.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import njnu.edu.back.pojo.VisualFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/08/22/14:30
 * @Description:
 */
public interface VisualService {
    void getAvatar(String fileName, HttpServletResponse response);

    void getRaster(String visualId, int x, int y, int z, HttpServletResponse response);

    void getVectorTiles(String visualId, int x, int y, int z, HttpServletResponse response);

    void getPhoto(String fileId, HttpServletResponse response);

    JSONArray getCoordinates(String visualId);

    void getPngResource(String visualId, HttpServletResponse response);

    JSONObject getTide(String visualId);

    JSONObject getRateDirection(String visualId);

    JSONObject getSandContent(String visualId);

    JSONObject getFlowSand_Z(String visualId);

    JSONObject getSalinity(String visualId);

    JSONObject getSuspension(String visualId);

    JSONObject getGeoJson(String fileId);

    Map<String, Object> getSection(String fileId);

    List<List<Double>> getSectionContrast(String fileId);

    Map<String, Object> getSectionFlush(String fileId);

    JSONObject getTianDiTu();

    JSONObject getTianDiTuImage();

    void addVisualFile(VisualFile visualFile);

    void addSameNameVisualFile(String type, String address);
}
