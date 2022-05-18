package njnu.edu.back.service;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/05/17/16:56
 * @Description:
 */
public interface FileMetaService {
    Map<String, Object> getFileMetaById(String id);
}
