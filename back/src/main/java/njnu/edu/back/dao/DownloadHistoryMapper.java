package njnu.edu.back.dao;

import njnu.edu.back.pojo.DownloadHistory;
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
public interface DownloadHistoryMapper {
    void addHistory(DownloadHistory downloadHistory);

    List<Map<String, Object>> pageQuery(@Param("size") int size, @Param("start") int start, @Param("dataId") String id);
}
