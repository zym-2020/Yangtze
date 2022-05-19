package njnu.edu.back.service;

import com.alibaba.fastjson.JSONObject;
import njnu.edu.back.pojo.ShareFile;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/05/17/16:55
 * @Description:
 */
public interface ShareFileService {
    void addShareFile(JSONObject jsonObject, String email);

    Map<String, Object> pageQueryOrderByDownload(int page, int size);

    Map<String, Object> getFileInfoAndMeta(String id);

    void addWatchCount(String id, String userId, String ip);
}
