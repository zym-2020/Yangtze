package njnu.edu.back.service;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/11/30/20:17
 * @Description:
 */
public interface BuoyService {
    List<Map<String, Object>> getBuoyByBox(double top, double right, double bottom, double left);
}
