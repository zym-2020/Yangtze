package njnu.edu.back.dao.main;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/02/20/15:45
 * @Description:
 */
@Repository
public interface SpecialDataMapper {
    void addRecord(@Param("id") String id);

    void delRecord(@Param("id") String id);

    List<String> getAllSpecialData();

    List<Map<String, Object>> getIdAndDataListName();
}
