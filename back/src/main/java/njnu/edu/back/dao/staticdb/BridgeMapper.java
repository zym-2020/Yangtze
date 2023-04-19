package njnu.edu.back.dao.staticdb;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/04/19/20:00
 * @Description:
 */
@Repository
public interface BridgeMapper {
    List<Map<String, Object>> pageQuery(@Param("size") int size, @Param("start") int start, @Param("keyword") String keyword);

    int count(@Param("keyword") String keyword);

    List<Map<String, Object>> getAllBridgeInfo();
}
