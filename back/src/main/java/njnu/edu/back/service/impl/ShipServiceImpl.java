package njnu.edu.back.service.impl;

import njnu.edu.back.dao.ShipMapper;
import njnu.edu.back.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Service
public class ShipServiceImpl implements ShipService {
    @Autowired
    ShipMapper shipMapper;

    @Override
    public Map<String, Object> QueryByMsi(int mmsi)
    {
        Map<String, Object> result = new HashMap<>();
        result.put("list", shipMapper.QueryByMsi(mmsi));
        return result;
    }
}
