package njnu.edu.back.service.impl;

import njnu.edu.back.dao.staticdb.BuoyMapper;
import njnu.edu.back.service.BuoyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/11/30/20:17
 * @Description:
 */
@Service
public class BuoyServiceImpl implements BuoyService {
    @Autowired
    BuoyMapper buoyMapper;

    @Override
    public List<Map<String, Object>> getBuoyByBox(double top, double right, double bottom, double left) {
        return buoyMapper.getBuoyByBox(top, right, bottom, left);
    }
}
