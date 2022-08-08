package njnu.edu.back.service.impl;
import njnu.edu.back.common.utils.CommonUtils;
import njnu.edu.back.dao.BrowseHistoryMapper;
import njnu.edu.back.pojo.BrowseHistory;
import njnu.edu.back.service.BrowseHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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

    @Override
    public List<Map<String, Object>> getDataGroupByDate(String dataId, int number) {
        String date = CommonUtils.getDate(number);
        return browseHistoryMapper.getDataGroupByDate(dataId, date);
    }
}
