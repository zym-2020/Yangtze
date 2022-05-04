package njnu.edu.back.service;

import njnu.edu.back.proj.dto.AddFileDTO;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/05/02/15:44
 * @Description:
 */

public interface FileService {
    void addFile(AddFileDTO addFileDTO, String email);

    List<Map<String, Object>> findByLevel(int level);

    List<Map<String, Object>> findByParentId(String parentId);

    List<String> getNoUpload(String MD5, String email, int total);
}
