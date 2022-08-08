package njnu.edu.back.service;

import njnu.edu.back.pojo.BrowseHistory;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/05/19/21:19
 * @Description:
 */

public interface BrowseHistoryService {
    void addHistory(BrowseHistory browseHistory);

    List<Map<String, Object>> getDataGroupByDate(String dataId, int number);
}
