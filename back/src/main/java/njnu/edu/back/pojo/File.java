package njnu.edu.back.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/05/02/15:15
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class File {
    String id;
    String name;
    String address;
    String fileName;
    Integer level;
    String parentId;
    Date createTime;
    String uploader;
    String meta;
    boolean folder;
    String size;
}
