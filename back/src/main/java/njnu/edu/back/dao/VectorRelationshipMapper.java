package njnu.edu.back.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/04/11/17:34
 * @Description:
 */
@Repository
public interface VectorRelationshipMapper {
    List<Map<String, Object>> pageQuery(@Param("size") int size, @Param("start") int start);

    int countAll();
}
