package njnu.edu.back.dao;

import njnu.edu.back.proj.support.TileBox;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/04/05/22:35
 * @Description:
 */
@Repository
public interface VectorTile {
    Object selectTile(TileBox tileBox);
}
