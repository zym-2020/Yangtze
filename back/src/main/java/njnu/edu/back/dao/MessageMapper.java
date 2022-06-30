package njnu.edu.back.dao;

import njnu.edu.back.pojo.Message;
import njnu.edu.back.pojo.ShareFile;
import njnu.edu.back.pojo.dto.AddMessageDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public interface MessageMapper {
    void addMessage(AddMessageDTO addMessageDTO);

    void addResponseMessage(AddMessageDTO addMessageDTO);

    List<Message> pageQueryDESC(@Param("size") int size, @Param("start") int start, @Param("property") String property);

    List<Message> pageQueryASC(@Param("size") int size, @Param("start") int start, @Param("property") String property);

    List<Map<String, Object>> QueryByType(@Param("property") String property);

    List<Map<String, Object>> QueryByUser(@Param("property") String property, @Param("type") String type);

    List<Map<String, Object>> QueryByReceiver(@Param("property") String property);

    List<Map<String, Object>> QueryByTime(@Param("property") String property);

    List<Map<String, Object>> QueryByUserEmail(@Param("email") String email);

    List<Map<String, Object>> QueryByUserType(@Param("property") String property,@Param("email") String email);

    void offlineMessage (@Param("property") String property,@Param("email") String email,@Param("dataUploadTime") String dataUploadTime);

    void offlineUserMessage (@Param("property") String property,@Param("email") String email,@Param("dataUploadTime") String dataUploadTime);

    List<Map<String, Object>> QueryHistoryMessage(@Param("email") String email);

    List<Map<String, Object>> QueryAllHistoryMessage();

    List<Map<String, Object>> showMessageDetails(@Param("property") String property);

    void responseMessage (@Param("response") String response,@Param("id") String id);

    Integer CountReply();

    Integer CountUserReply();

}
