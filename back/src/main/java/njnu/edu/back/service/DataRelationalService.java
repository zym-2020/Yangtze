package njnu.edu.back.service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/08/30/10:32
 * @Description:
 */
public interface DataRelationalService {
    void addRelational(String dataListId, List<String> fileIdList);

    void updateRelational(String dataListId, List<String> fileList);
}
