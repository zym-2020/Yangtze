package njnu.edu.back.dao.main;

import njnu.edu.back.pojo.BrowseHistory;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/05/19/21:07
 * @Description:
 */
@Repository
public interface BrowseHistoryMapper {
    void addHistory(BrowseHistory browseHistory);

    List<Map<String, Object>> getDataGroupByDate(@Param("dataId") String dataId, @Param("date") String date);
}
