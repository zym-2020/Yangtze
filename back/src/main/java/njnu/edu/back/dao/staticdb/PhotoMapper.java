package njnu.edu.back.dao.staticdb;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/11/30/21:07
 * @Description:
 */
@Service
public interface PhotoMapper {
    Object getPhoto(@Param("fileName") String fileName);
}
