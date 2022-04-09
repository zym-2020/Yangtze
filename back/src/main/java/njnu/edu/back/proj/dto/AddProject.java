package njnu.edu.back.proj.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/04/07/17:27
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddProject {
    String projectName;
    String creator;
    String description;
    Date createTime = new Date();
    String result;
}
