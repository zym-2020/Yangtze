package njnu.edu.back.service;


import njnu.edu.back.pojo.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/03/28/20:50
 * @Description:
 */

public interface UserService {
    String login(String email, String password);

    int register(User user);

    Map<String, Object> getUserByEmail(String email);

    Map<String, String> setUserInfo(String email, String name, String occupation, String department, MultipartFile avatar);

    Map<String, Object> getUserInfo(String email);

    Map<String, Integer> getResourceCount(String email);

    Map<String, Object> getAllUserInfo(String role, Integer page, Integer size, String keyword);

    void resetPassword(String role, String id, String password);

    void delete(String id, String role);

    void batchDelete(List<String> ids, String role);

    String adminAddUser(MultipartFile file, String jsonString, String role);
}
