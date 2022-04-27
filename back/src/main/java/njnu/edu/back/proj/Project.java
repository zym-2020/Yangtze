package njnu.edu.back.proj;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

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
}
