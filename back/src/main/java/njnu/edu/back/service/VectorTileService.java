package njnu.edu.back.service;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/04/05/16:07
 * @Description:
 */
public interface VectorTileService {
    Object getVectorTile(String tableName, int x, int y, int z);


}
