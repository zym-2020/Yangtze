package njnu.edu.back.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ship {

        String mmsi;
        String weight;
        String height;
        String Speed;
        String Draught;
        String lastReport;
        String DeadWeight;

}
