package njnu.edu.back.dao.staticdb;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/11/30/20:03
 * @Description:
 */
@Repository
public interface BuoyMapper {
    List<Map<String, Object>> getBuoyByBox(@Param("top") double top, @Param("right") double right, @Param("bottom") double bottom, @Param("left") double left);

    List<Map<String, Object>> pageQuery(@Param("size") int size, @Param("start") int start, @Param("keyword") String keyword);

    int count(@Param("keyword") String keyword);
}
