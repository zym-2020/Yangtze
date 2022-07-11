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
    String name;
    String address;
    String description;
    String type;
    Date uploadTime;
    String benchmark;
    String reference;
}
