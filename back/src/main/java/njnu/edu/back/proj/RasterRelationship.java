package njnu.edu.back.proj;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/04/11/11:00
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RasterRelationship {
    String id;
    String fileName;
    String category;
    String address;
    String meta;
    boolean hasTiles;
    String uploader;
    String source;
}
