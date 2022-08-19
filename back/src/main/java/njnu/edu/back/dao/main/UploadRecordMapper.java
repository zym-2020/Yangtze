package njnu.edu.back.dao.main;

import njnu.edu.back.pojo.UploadRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/06/01/22:08
 * @Description:
 */
@Repository
public interface UploadRecordMapper {
    void addUploadRecord(@Param("uploadRecord") UploadRecord uploadRecord);

    List<Map<String, Object>> getRecords(@Param("date") String date, @Param("email") String email);

    void delRecord(@Param("id") String id);
}
