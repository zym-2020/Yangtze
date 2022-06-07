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

    List<Message> pageQueryDESC(@Param("size") int size, @Param("start") int start, @Param("property") String property);

    List<Message> pageQueryASC(@Param("size") int size, @Param("start") int start, @Param("property") String property);

    List<Map<String, Object>> QueryByType(@Param("property") String property);

    List<Map<String, Object>> QueryByUser(@Param("property") String property, @Param("type") String type);

    List<Map<String, Object>> QueryByReceiver(@Param("property") String property);

    List<Map<String, Object>> QueryByTime(@Param("property") String property);
}
