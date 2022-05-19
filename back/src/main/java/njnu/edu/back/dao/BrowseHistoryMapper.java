package njnu.edu.back.dao;

import njnu.edu.back.pojo.BrowseHistory;
import org.springframework.stereotype.Repository;

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


}
