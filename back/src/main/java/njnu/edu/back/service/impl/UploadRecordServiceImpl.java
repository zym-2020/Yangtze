package njnu.edu.back.service.impl;

import njnu.edu.back.common.utils.CommonUtils;
import njnu.edu.back.dao.UploadRecordMapper;
import njnu.edu.back.pojo.UploadRecord;
import njnu.edu.back.service.UploadRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/06/01/22:23
 * @Description:
 */
@Service
public class UploadRecordServiceImpl implements UploadRecordService {
    @Autowired
    UploadRecordMapper uploadRecordMapper;

    @Override
    public List<Map<String, Object>> getRecords(int number, String email) {
        String date = CommonUtils.getDate(number);
        return uploadRecordMapper.getRecords(date, email);
    }

    @Override
    public void delRecord(String id) {
        uploadRecordMapper.delRecord(id);
    }

    @Override
    public void addRecord(UploadRecord record, String email) {
        record.setUploader(email);
        uploadRecordMapper.addUploadRecord(record);
    }
}
