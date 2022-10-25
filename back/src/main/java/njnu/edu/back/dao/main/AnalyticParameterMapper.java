package njnu.edu.back.dao.main;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/10/17/17:08
 * @Description:
 */
@Repository
public interface AnalyticParameterMapper {
    List<Map<String, Object>> findByType(@Param("type") String type);

    String findAddressByBenchmarkIdAndReferId(@Param("benchmarkId") String benchmarkId, @Param("referId") String referId, @Param("type") String type);

    Map<String, Object> findByBenchmarkIdAndReferId(@Param("benchmarkId") String benchmarkId, @Param("referId") String referId, @Param("type") String type);

    Map<String, Object> findSlope(@Param("demId") String demId);

    Map<String, Object> findInfoById(@Param("id") String id);
}
