package njnu.edu.back.service;

import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/04/11/10:52
 * @Description:
 */
public interface RasterTileService {
    void getRaster(String rasterId, String x, String y, String z, HttpServletResponse response);
}
