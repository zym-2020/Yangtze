package njnu.edu.back.proj;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;
import java.util.List;

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
    Integer id;
    String name;
    String email;
    String password;
    String[] roles;
}
