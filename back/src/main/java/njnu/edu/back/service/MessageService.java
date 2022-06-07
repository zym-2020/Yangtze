package njnu.edu.back.service;

import com.alibaba.fastjson.JSONObject;
import njnu.edu.back.pojo.ShareFile;
import njnu.edu.back.pojo.dto.AddMessageDTO;

import java.util.Map;

public interface MessageService {

    void addMessage(AddMessageDTO addMessageDTO, String id);

    Map<String, Object> pageQuerys(int page, int size, String property, boolean flag);

    Map<String, Object> QueryByType(String property);

    Map<String, Object> QueryByUser(String property, String type);

    Map<String, Object> QueryByReceiver(String property);

    Map<String, Object> QueryByTime(String property);
}
