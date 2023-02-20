package njnu.edu.back.service;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/02/20/15:54
 * @Description:
 */
public interface SpecialDataService {
    void addRecord(String id);

    void delRecord(String id);

    List<String> getAllSpecialData();

    List<Map<String, Object>> getIdAndDataListName(int size, int start);
}
