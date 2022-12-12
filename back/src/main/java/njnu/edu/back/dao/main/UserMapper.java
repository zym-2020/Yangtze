package njnu.edu.back.dao.main;

import njnu.edu.back.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    String addUser(@Param("user") User user);

    User updateUserInfo(User user);

    List<Map<String, Object>> getAllUserInfo(@Param("size") int size, @Param("start") int start, @Param("keyword") String keyword);

    int countAll(@Param("keyword") String keyword);

    String resetPassword(@Param("id") String id, @Param("password") String password);

    Map<String, Object> findById(@Param("id") String id);

    void deleteById(@Param("id") String id);

    List<Map<String, Object>> findByIds(@Param("ids") List<String> ids);

    void batchDelete(@Param("ids") List<String> ids);
}
