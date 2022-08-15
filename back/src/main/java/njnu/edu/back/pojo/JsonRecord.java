package njnu.edu.back.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JsonRecord {
    String id;
    String shpName;
    String jsonName;
    String jsonAddress;
    String shpAddress;
    String shpPinYinName;

}
