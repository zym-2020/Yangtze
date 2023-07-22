package njnu.edu.back.dao.monitor;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/07/22/10:59
 * @Description:
 */
@Repository
public interface FluxResultMapper {
    List<Map<String, Object>> getAllResult();

    List<Map<String, Object>> getAllResultByTypeAndName(@Param("type") String type, @Param("name") String name);

    List<Map<String, Object>> getAllResultByTypeAndNameAndTime(@Param("type") String type, @Param("name") String name, @Param("startTime") String startTime, @Param("endTime") String endTime);
}
