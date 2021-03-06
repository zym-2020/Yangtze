package njnu.edu.back.pojo.support.projectJson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/04/07/16:38
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectJsonBean {
    List<Resource> layerDataList;
    Analyse analyse;
}
