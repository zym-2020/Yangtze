package njnu.edu.back.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShipBinary {

    int mmsi;
    int lon;
    int lat;
    int rot;
    int len;
    int wid;
    int zoom;


}
