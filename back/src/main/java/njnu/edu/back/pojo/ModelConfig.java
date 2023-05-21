package njnu.edu.back.pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/05/20/22:15
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@XStreamAlias("model")
public class ModelConfig {
    @XStreamAsAttribute
    String id;
    String name;
    String description;

    @XStreamAlias("parameters")
    ModelParameters parameters;
}
