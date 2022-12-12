package njnu.edu.back.dao.ship;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/12/06/15:59
 * @Description:
 */
@Repository
public interface LocusMapper {
    List<Map<String, Object>> getShipByBox(@Param("tableName") String tableName, @Param("top") double top, @Param("right") double right, @Param("bottom") double bottom, @Param("left") double left);

    Integer existTable(@Param("tableName") String tableName);

    List<Map<String, Object>> record(@Param("tableName") String tableName, @Param("mmsi") String mmsi, @Param("time") String time);
}
