package njnu.edu.back.service;

import com.alibaba.fastjson.JSONObject;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/05/17/16:55
 * @Description:
 */
public interface ShareFileService {
    void addShareFile(JSONObject jsonObject, String email);
}
