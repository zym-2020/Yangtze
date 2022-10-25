package njnu.edu.back.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/06/14/21:08
 * @Description:
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AnalyticDataSet {
    String id;
    String fileName;
    String address;
    Date createTime;
    String creator;
    String visualType;
    String visualId;
    String projectId;
}
