package njnu.edu.back.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/06/14/21:21
 * @Description:
 */
@Repository
public interface AnalyticDataSetMapper {
    List<Map<String, Object>> findDataByType(@Param("list") List<String> types);

    Map<String, Object> findById(@Param("id") String id);

    String findIdByBenchmarkAndReference(@Param("benchmark") String benchmark, @Param("reference") String reference);
}
