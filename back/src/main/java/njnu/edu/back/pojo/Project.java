package njnu.edu.back.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/04/06/19:13
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {
    String id;
    String projectName;
    String creator;
    String description;
    Date createTime;
    String result;
    String avatar;
}
