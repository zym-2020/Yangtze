package njnu.edu.back.dao.monitor;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/07/22/10:27
 * @Description:
 */
@Repository
public interface SandContentResultMapper {
    List<Map<String, Object>> getAllResult();

    List<Map<String, Object>> getAllResultByName(@Param("name") String name);

    List<Map<String, Object>> getAllResultByTimeAndType(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("type") String type);

    List<Map<String, Object>> getAllResultByNameAndTime(@Param("name") String name, @Param("startTime") String startTime, @Param("endTime") String endTime);
}
