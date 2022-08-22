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

    Map<String, Object> pageQuery(int size, int page, String dataListId);

}
