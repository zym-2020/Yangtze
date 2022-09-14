package njnu.edu.back.service;


import njnu.edu.back.pojo.User;
import njnu.edu.back.pojo.dto.AddUserDTO;
import org.springframework.web.multipart.MultipartFile;

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

    int register(AddUserDTO addUserDTO);

    User getUserByEmail(String email);

    Map<String, String> setUserInfo(String email, String name, String contactEmail, String occupation, String department, MultipartFile avatar);

    String setUserInfoWithoutAvatar(String email, User user);

    String getAvatarURL(String email);

}
