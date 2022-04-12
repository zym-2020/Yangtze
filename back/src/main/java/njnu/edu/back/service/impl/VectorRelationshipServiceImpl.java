package njnu.edu.back.service.impl;

import cn.hutool.json.JSONObject;
import njnu.edu.back.dao.VectorRelationshipMapper;
import njnu.edu.back.service.VectorRelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/04/11/17:33
 * @Description:
 */
@Service
public class VectorRelationshipServiceImpl implements VectorRelationshipService {

    @Autowired
    VectorRelationshipMapper vectorRelationshipMapper;

    @Override
    public JSONObject pageQuery(int size, int page) {
        int start = page * size;
        JSONObject jsonObject = new JSONObject();
        jsonObject.set("total", vectorRelationshipMapper.countAll());
        jsonObject.set("list", vectorRelationshipMapper.pageQuery(size, start));
        return jsonObject;
    }
}
