package njnu.edu.back.dao.ship;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/12/06/15:57
 * @Description:
 */
@Repository
public interface ShipMapper {
    Map<String, Object> getShipInfoByMMSI(@Param("mmsi") String mmsi);
}
