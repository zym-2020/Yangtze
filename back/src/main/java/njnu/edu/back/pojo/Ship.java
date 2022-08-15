package njnu.edu.back.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ship {

        String id;
        Integer mmsi;
        Date date;
        Integer zoom;
        byte[] code;

}
