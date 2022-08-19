package njnu.edu.back.service.impl;

import cn.hutool.json.JSONObject;
import njnu.edu.back.dao.main.RasterRelationshipMapper;
import njnu.edu.back.service.RasterRelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/04/11/20:09
 * @Description:
 */
@Service
public class RasterRelationshipServiceImpl implements RasterRelationshipService {

    @Autowired
    RasterRelationshipMapper rasterRelationshipMapper;

    @Override
    public JSONObject pageQuery(int size, int page) {
        int start = page * size;
        JSONObject jsonObject = new JSONObject();
        jsonObject.set("total", rasterRelationshipMapper.countAll());
        jsonObject.set("list", rasterRelationshipMapper.pageQuery(size, start));
        return jsonObject;
    }
}
