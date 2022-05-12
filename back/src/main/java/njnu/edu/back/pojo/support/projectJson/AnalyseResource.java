package njnu.edu.back.pojo.support.projectJson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/04/16/21:11
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyseResource {
    Integer classifyCount;
    String classify;        //断面形态 || 边界对比 || 汊道断面对比 || 冲淤等深线 || 等高线对比 || 断面比较.....
    List<Resource> analysisResultList;
}
