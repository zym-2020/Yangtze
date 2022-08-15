package njnu.edu.back.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/07/22/17:20
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JsonRecord {
    String id;
    String name;
    String shpName;
    String jsonName;
    String shpAddress;
    String shpPinYinName;
}
