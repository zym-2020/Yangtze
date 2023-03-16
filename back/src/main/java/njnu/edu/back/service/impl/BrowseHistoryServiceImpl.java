package njnu.edu.back.service.impl;
import njnu.edu.back.common.utils.CommonUtils;
import njnu.edu.back.dao.main.BrowseHistoryMapper;
import njnu.edu.back.dao.main.DataListMapper;
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

    @Autowired
    DataListMapper dataListMapper;

    @Override
    public void addHistory(BrowseHistory browseHistory) {
        Map<String, Object> map = dataListMapper.getFileInfo(browseHistory.getDataId());
        if ((Integer) map.get("status") == 1)
        browseHistoryMapper.addHistory(browseHistory);
    }

    @Override
    public List<Map<String, Object>> getDataGroupByDate(String dataId, int number) {
        String date = CommonUtils.getDate(number);
        return browseHistoryMapper.getDataGroupByDate(dataId, date);
    }
}
