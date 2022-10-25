package njnu.edu.back.dao.main;

import njnu.edu.back.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

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

    int addUser(@Param("user") User user);

    User updateUserInfo(User user);


    String getAvatarURL(@Param("email") String email);
}
