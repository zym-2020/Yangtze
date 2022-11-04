package njnu.edu.back.service;

import njnu.edu.back.pojo.UploadRecord;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/06/01/22:22
 * @Description:
 */
public interface UploadRecordService {
    List<Map<String, Object>> getRecords(String email);

    void delRecord(String id);

    void addRecord(UploadRecord record, String email);
}
