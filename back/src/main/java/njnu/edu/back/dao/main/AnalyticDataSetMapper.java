package njnu.edu.back.dao.main;

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
    List<Map<String, Object>> getAnalyticData(@Param("projectId") String projectId);

    String addDataSet(@Param("Id") String id, @Param("fileName") String fileName, @Param("address") String address, @Param("email") String email, @Param("visualType") String visualType, @Param("visualId") String visualId, @Param("projectId") String projectId);

    void delAnalyticData(@Param("id") String id);

    void delAnalyticDataByProjectId(@Param("projectId") String projectId);

    Map<String, Object> getInfoById(@Param("id") String id);

    void rename(@Param("id") String id, @Param("name") String name);


}
