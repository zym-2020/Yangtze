package njnu.edu.back.proj.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/04/15/20:29
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddVector {
    String tableName;
    String category;
    String address;
    String meta;
    String fileName;
    String uploader;
    String source;
    String type;
}
