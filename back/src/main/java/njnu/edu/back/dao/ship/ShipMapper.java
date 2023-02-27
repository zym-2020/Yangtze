package njnu.edu.back.dao.ship;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    List<Map<String, Object>> pageQuery(@Param("size") int size, @Param("start") int start, @Param("keyword") String keyword);

    int count(@Param("keyword") String keyword);
}
