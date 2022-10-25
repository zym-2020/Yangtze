package njnu.edu.back.service;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/10/17/17:24
 * @Description:
 */
public interface AnalyticParameterService {
    List<Map<String, Object>> findByType(String type);
}
