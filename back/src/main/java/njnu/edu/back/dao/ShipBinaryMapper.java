package njnu.edu.back.dao;

import njnu.edu.back.pojo.ShipBinary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface ShipBinaryMapper {
    void addShipBinary(ShipBinary shipBinary);

    List<Map<String, Object>> QueryBinary();


}
