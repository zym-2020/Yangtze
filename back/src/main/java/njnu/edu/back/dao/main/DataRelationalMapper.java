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

    List<Map<String, Object>> findFilesByDataListId(@Param("dataListId") String dataListId);

    List<Map<String, Object>> findFileIdByDataListId(@Param("dataListId") String dataListId);

    void batchInsert(@Param("list") List<String> list, @Param("dataListId") String dataListId);

    void batchDelete(@Param("list") List<String> list, @Param("dataListId") String dataListId);
}
