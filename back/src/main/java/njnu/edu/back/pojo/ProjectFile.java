package njnu.edu.back.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/10/12/10:33
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectFile {
    @Id
    String id;
    String projectId;
    String fileId;
    String dataListId;
}
