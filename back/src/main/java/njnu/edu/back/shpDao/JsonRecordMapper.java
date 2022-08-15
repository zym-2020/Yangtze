package njnu.edu.back.shpDao;

import njnu.edu.back.pojo.JsonRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface JsonRecordMapper {
    List<JsonRecord> QueryByName(@Param("name") String name);
}
