package njnu.edu.back.pojo.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/07/04/17:05
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Section {
    String id;
    String sectionId;
    Integer state;
}
