package njnu.edu.back.service.impl;

import njnu.edu.back.dao.staticdb.BuoyMoveMapper;
import njnu.edu.back.service.BuoyMoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/11/30/20:42
 * @Description:
 */
@Service
public class BuoyMoveServiceImpl implements BuoyMoveService {

    @Autowired
    BuoyMoveMapper buoyMoveMapper;

    @Override
    public List<Map<String, Object>> getInfoByTimestamp(long startTime, long endTime) {
        return buoyMoveMapper.getInfoByTimestamp(startTime, endTime);
    }
}
