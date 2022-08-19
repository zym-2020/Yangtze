package njnu.edu.back.dao.main;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/08/19/16:10
 * @Description:
 */
@Repository
public interface DataRelationalMapper {
    void batchDeleteByDataListId(@Param("list") List<String> list);

    void batchDeleteFolder(@Param("list") List<String> list);
}
