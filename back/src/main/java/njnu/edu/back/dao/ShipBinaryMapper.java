package njnu.edu.back.dao;

import njnu.edu.back.pojo.ShipBinary;

import java.util.List;
import java.util.Map;

public interface ShipBinaryMapper {
    void addShipBinary(ShipBinary shipBinary);

    List<Map<String, Object>> QueryBinary();


}
