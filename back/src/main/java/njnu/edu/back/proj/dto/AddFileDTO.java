package njnu.edu.back.proj.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/05/02/15:35
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddFileDTO {
    String id;
    String name;
    String address;
    String fileName;
    Integer level;
    String parentId;
    String uploader;
    String meta;
    boolean folder;
}
