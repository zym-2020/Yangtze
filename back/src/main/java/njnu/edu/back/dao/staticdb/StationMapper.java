package njnu.edu.back.dao.staticdb;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/12/04/18:42
 * @Description:
 */
@Repository
public interface StationMapper {
    List<Map<String, Object>> getStationByBox(@Param("top") double top, @Param("right") double right, @Param("bottom") double bottom, @Param("left") double left);

    List<Map<String, Object>> pageQuery(@Param("size") int size, @Param("start") int start, @Param("keyword") String keyword);

    int count(@Param("keyword") String keyword);

    List<Map<String, Object>> getAllStation();
}
