package njnu.edu.back.service;


import njnu.edu.back.proj.User;
import njnu.edu.back.proj.dto.AddUserDTO;

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


}
