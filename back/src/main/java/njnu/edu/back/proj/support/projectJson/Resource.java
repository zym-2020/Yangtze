package njnu.edu.back.proj.support.projectJson;

import cn.hutool.json.JSONObject;
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
public class Resource {
    String id;
    String name;

    String type;         //raster || vector || geoJson
    boolean show;
    String tableName;   //针对shape
    String vectorType;   //针对shape
    JSONObject geoJson;

}
