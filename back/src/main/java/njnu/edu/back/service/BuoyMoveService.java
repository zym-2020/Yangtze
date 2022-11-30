package njnu.edu.back.service;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/11/30/20:41
 * @Description:
 */
public interface BuoyMoveService {
    List<Map<String, Object>> getInfoByTimestamp(long startTime, long endTime);
}
