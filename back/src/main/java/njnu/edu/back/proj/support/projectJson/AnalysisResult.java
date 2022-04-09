package njnu.edu.back.proj.support.projectJson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/04/07/17:12
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisResult {
    String name;
    String classify;        //断面形态 || 边界对比 || 汊道断面对比 || 冲淤等深线 || 等高线对比 || 断面比较.....
    String address;
}
