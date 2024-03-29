package njnu.edu.back.dao.main;

import njnu.edu.back.pojo.VisualFile;
import njnu.edu.back.pojo.support.TileBox;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/08/22/14:54
 * @Description:
 */
@Repository
public interface VisualFileMapper {
    Map<String, Object> findById(@Param("id") String id);

    Map<String, Object> addVisualFile(VisualFile visualFile);

    String getView(@Param("id") String id);
}
