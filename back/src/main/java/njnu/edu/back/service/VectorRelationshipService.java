package njnu.edu.back.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/04/11/17:10
 * @Description:
 */
public interface VectorRelationshipService {
    JSONObject pageQuery(int size, int page);

    String newShape(JSONArray jsonArray, String fileName, String type, String email, String source, String projectName, String category, String meta);

    Map<String, Object> checkState(String uuid);
}
