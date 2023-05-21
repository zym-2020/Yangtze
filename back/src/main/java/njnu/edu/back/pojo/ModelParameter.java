package njnu.edu.back.pojo;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/05/20/22:21
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModelParameter {
    @XStreamAsAttribute
    Boolean optional;
    String name;
    String description;
    String type;
}
