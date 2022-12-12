package njnu.edu.back.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/03/28/17:20
 * @Description:
 */

@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    String id;
    String name;
    String email;
    String password;
    String role;
    String avatar = "";
    String occupation = "";
    String department = "";
}
