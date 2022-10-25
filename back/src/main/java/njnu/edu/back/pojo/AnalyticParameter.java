package njnu.edu.back.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/10/17/17:01
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticParameter {
    @Id
    String id;
    String type;
    String fileId;
    String dataListId;
    String address;
    String content;
    String benchmarkId;
    String referId;
}
