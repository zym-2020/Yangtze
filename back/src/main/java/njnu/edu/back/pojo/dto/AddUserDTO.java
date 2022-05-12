package njnu.edu.back.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/03/28/20:53
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddUserDTO {
    @NotEmpty(message = "姓名不能为空")
    String name;
    @NotEmpty(message = "邮箱不能为空")
    String email;
    @NotEmpty(message = "密码不能为空")
    String password;
    @NotEmpty(message = "权限不能为空")
    String[] roles;
}
