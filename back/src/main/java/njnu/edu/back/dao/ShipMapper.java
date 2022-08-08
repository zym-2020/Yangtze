package njnu.edu.back.dao;

import njnu.edu.back.pojo.Message;
import njnu.edu.back.pojo.dto.AddMessageDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ShipMapper {

    List<Map<String, Object>> QueryByMsi(@Param("mmsi") int mmsi);


}
