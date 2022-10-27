package njnu.edu.back.dao.main;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ShipMapper {

    List<Map<String, Object>> QueryCode();


}
