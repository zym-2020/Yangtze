package njnu.edu.back.service.impl;

import njnu.edu.back.dao.DownloadHistoryMapper;
import njnu.edu.back.pojo.DownloadHistory;
import njnu.edu.back.service.DownloadHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/05/19/21:21
 * @Description:
 */
@Service
public class DownloadHistoryServiceImpl implements DownloadHistoryService {

    @Autowired
    DownloadHistoryMapper downloadHistoryMapper;

    @Override
    public void addHistory(DownloadHistory downloadHistory) {
        downloadHistoryMapper.addHistory(downloadHistory);
    }
}
