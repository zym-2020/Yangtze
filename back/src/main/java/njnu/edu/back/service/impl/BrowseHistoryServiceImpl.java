package njnu.edu.back.service.impl;
import njnu.edu.back.dao.BrowseHistoryMapper;
import njnu.edu.back.pojo.BrowseHistory;
import njnu.edu.back.service.BrowseHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/05/19/21:19
 * @Description:
 */
@Service
public class BrowseHistoryServiceImpl implements BrowseHistoryService {

    @Autowired
    BrowseHistoryMapper browseHistoryMapper;

    @Override
    public void addHistory(BrowseHistory browseHistory) {
        browseHistoryMapper.addHistory(browseHistory);
    }
}
