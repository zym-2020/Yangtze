package njnu.edu.back.dao.monitor;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/07/22/10:17
 * @Description:
 */
@Repository
public interface LocusResultMapper {
    List<Map<String, Object>> getAllLocus();

    List<Map<String, Object>> getAllLocusByName(@Param("name") String name);

    List<Map<String, Object>> getAllLocusByTime(@Param("startTime") String startTime, @Param("endTime") String endTime);

    List<Map<String, Object>> getAllLocusByNameAndTime(@Param("name") String name, @Param("startTime") String startTime, @Param("endTime") String endTime);
}
