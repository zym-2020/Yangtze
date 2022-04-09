package njnu.edu.back.proj.support.projectJson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/04/07/17:03
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LayerData {
    String name;
    String type;        //file || database
    String data;        //address || table_name
}
