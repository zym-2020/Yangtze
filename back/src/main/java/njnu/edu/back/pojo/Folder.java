package njnu.edu.back.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/08/19/14:15
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Folder {
    String id;
    String folderName;
    String parentId;
    String address;
}
