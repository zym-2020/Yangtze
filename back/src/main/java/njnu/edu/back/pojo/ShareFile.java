package njnu.edu.back.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/05/16/14:57
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShareFile {
    String id;
    String name;
    String description;
    String originAddress;
    String visualSource;
    String visualType;
    String structuredSource;
    String[] tags;
    String creator;
    Date createTime;
    String meta;
}
