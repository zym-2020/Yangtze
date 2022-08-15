package njnu.edu.back.shpDao;

import njnu.edu.back.pojo.support.TileBox;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/04/05/22:35
 * @Description:
 */
@Repository
public interface VectorUserTile {
    Object selectTile(TileBox tileBox);
}
