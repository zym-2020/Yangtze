package njnu.edu.back.dao.monitor;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/07/22/14:05
 * @Description:
 */
@Repository
public interface VelocityDirectionResultMapper {
    List<Map<String, Object>> getAllResult();

    List<Map<String, Object>> getAllResultByTypeAndNameAndTime(@Param("type") String type, @Param("name") String name, @Param("startTime") String startTime, @Param("endTime") String endTime);

    List<String> getDistanceByName(@Param("name") String name);

    List<Map<String, Object>> getAllResultByTypeAndNameAndDistanceAndTime(@Param("type") String type, @Param("name") String name, @Param("distance") String distance, @Param("startTime") String startTime, @Param("endTime") String endTime);
}
