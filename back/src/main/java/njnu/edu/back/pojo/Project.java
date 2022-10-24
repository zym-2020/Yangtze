package njnu.edu.back.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;

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
    @Id
    String id;
    String projectName;
    String creator;
    Date createTime;
    String avatar;
    String[] LayerManage;
}
