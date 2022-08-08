package njnu.edu.back.service;


import njnu.edu.back.pojo.dto.AddMessageDTO;

import java.util.Map;

public interface MessageService {

    void addMessage(AddMessageDTO addMessageDTO, String id,String email);

    void addResponseMessage(AddMessageDTO addMessageDTO, String id,String email);

    Map<String, Object> pageQuerys(int page, int size, String property, boolean flag);

    Map<String, Object> QueryByType(String property);

    Map<String, Object> QueryByUser(String property, String type);

    Map<String, Object> QueryByReceiver(String property);

    Map<String, Object> QueryByTime(String property);

    Map<String, Object> QueryByUserEmail(String email);

    Map<String, Object> QueryByUserType(String property,String email);

    void offlineMessage(String email,String property,String dataUploadTime);

    void offlineUserMessage(String email,String property,String dataUploadTime);

    Map<String, Object> QueryHistoryMessage(String email);

    Map<String, Object> QueryAllHistoryMessage();

    Map<String, Object> showMessageDetails(String property);

    void responseMessage(String response,String id);
}
