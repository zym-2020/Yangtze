package njnu.edu.back.dao.staticdb;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/12/01/14:49
 * @Description:
 */
@Repository
public interface ParkMapper {
    List<Map<String, Object>> getAllInfo();
}
