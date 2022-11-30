package njnu.edu.back.dao.staticdb;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/11/30/20:42
 * @Description:
 */
@Repository
public interface BuoyMoveMapper {
    List<Map<String, Object>> getInfoByTimestamp(@Param("startTime") long startTime, @Param("endTime") long endTime);
}
