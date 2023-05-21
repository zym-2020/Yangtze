package njnu.edu.back.pojo;

import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/05/20/22:20
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModelParameters {
    @XStreamImplicit(itemFieldName = "parameter")
    List<ModelParameter> parameterList;
}
