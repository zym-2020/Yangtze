package njnu.edu.back.dao.shp;

import njnu.edu.back.pojo.support.TileBox;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/07/08/22:03
 * @Description:
 */
@Repository
public interface VectorTile {
    Object selectTile(TileBox tileBox);
}
