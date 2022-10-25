package njnu.edu.back.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/05/19/21:05
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DownloadHistory {
    String id;
    String userId;
    Date downloadTime;
    String ip;
    String dataListId;
    String fileId;
}
