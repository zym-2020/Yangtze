package njnu.edu.back.service.impl;

import njnu.edu.back.dao.main.DataRelationalMapper;
import njnu.edu.back.service.DataRelationalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/08/30/10:33
 * @Description:
 */
@Service
public class DataRelationalServiceImpl implements DataRelationalService {
    @Autowired
    DataRelationalMapper dataRelationalMapper;

    @Override
    public void addRelational(String dataListId, List<String> fileIdList) {
        dataRelationalMapper.batchInsert(fileIdList, dataListId);
    }
}
