package njnu.edu.back.dao.main;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TideStationMapper {

    List<Map<String,Object>> QueryHeightByName(@Param("name") String name);
}
