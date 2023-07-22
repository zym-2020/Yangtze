package njnu.edu.back.dao.monitor;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/07/22/10:10
 * @Description:
 */
@Repository
public interface VerticalLineMapper {
    List<Map<String, Object>> getAllVerticalLine();
}
