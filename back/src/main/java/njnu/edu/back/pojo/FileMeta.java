package njnu.edu.back.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/05/17/14:01
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileMeta {
    String id;
    String provider;
    String time;
    String range;
    String detail;
}
