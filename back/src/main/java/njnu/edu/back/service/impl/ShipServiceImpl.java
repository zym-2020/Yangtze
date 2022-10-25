package njnu.edu.back.service.impl;

import com.google.common.primitives.Bytes;
import njnu.edu.back.dao.main.ShipMapper;
import njnu.edu.back.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public class ShipServiceImpl implements ShipService {
    @Autowired
    ShipMapper shipMapper;

    @Override
    public byte[] QueryCode()
    {
        byte[] bytes=new byte[0];
        //Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> list = shipMapper.QueryCode();;
        //list = shipMapper.QueryCode();
        for(int i=0; i<list.size();i++){
            bytes= Bytes.concat(bytes, (byte[]) list.get(i).get("code"));
        }
        return bytes;
    }
}
