package njnu.edu.back.service;

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
}
