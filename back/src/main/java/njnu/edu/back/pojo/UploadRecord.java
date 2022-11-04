package njnu.edu.back.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/06/01/22:06
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadRecord {
    String id;
    String fileName;
    String uploader;
    Date uploadTime;
    String size;
}
