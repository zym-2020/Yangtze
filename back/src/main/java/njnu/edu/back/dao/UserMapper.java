package njnu.edu.back.dao;

import njnu.edu.back.proj.User;
import njnu.edu.back.proj.dto.AddUserDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/03/28/17:18
 * @Description:
 */

@Repository
public interface UserMapper {
    User getUserByEmail(@Param("email") String email);

    int addUser(AddUserDTO addUserDTO);

}
