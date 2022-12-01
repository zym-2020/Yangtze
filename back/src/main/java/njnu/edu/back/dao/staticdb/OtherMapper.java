package njnu.edu.back.dao.staticdb;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/12/01/21:46
 * @Description:
 */
@Repository
public interface OtherMapper {
    List<Map<String, Object>> getAllInfo();
}
