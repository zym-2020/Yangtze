package njnu.edu.back.dao.main;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ShpCoordinatesMapper {
    List<Map<String,Object>> QueryCoordinates();

    List<Map<String,Object>> QueryCoordinatesByName(@Param ("name") String name);



}
