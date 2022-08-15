package njnu.edu.back.service.impl;

import njnu.edu.back.common.utils.TileUtil;
import njnu.edu.back.pojo.support.TileBox;
import njnu.edu.back.service.VectorTileService;
import njnu.edu.back.shpDao.VectorUserTile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/04/05/22:33
 * @Description:
 */
@Service
public class VectorTileImpl implements VectorTileService {

    @Autowired
    VectorUserTile vectorUserTile;

    @Override
    public Object getVectorTile(String tableName, int x, int y, int z) {
        TileBox tileBox = TileUtil.tile2boundingBox(x, y, z, tableName);
        Object result = vectorUserTile.selectTile(tileBox);
        return result;
    }
}
