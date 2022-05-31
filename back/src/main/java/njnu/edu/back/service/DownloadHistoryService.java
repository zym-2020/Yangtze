package njnu.edu.back.service;

import njnu.edu.back.pojo.DownloadHistory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/05/19/21:21
 * @Description:
 */

public interface DownloadHistoryService {
    void addHistory(DownloadHistory downloadHistory);

    Map<String, Object> pageQuery(int size, int page, String dataId);

}
