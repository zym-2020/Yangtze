package njnu.edu.back.dao;

import njnu.edu.back.pojo.RasterRelationship;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/04/11/11:32
 * @Description:
 */
@Repository
public interface RasterRelationshipMapper {
    String getAddress(@Param("id") UUID rasterId);

    List<Map<String, Object>> pageQuery(@Param("size") int size, @Param("start") int start);

    int countAll();

    RasterRelationship getById(@Param("id") UUID rasterId);
}
