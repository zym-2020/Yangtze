package njnu.edu.back.service;

import njnu.edu.back.pojo.dto.AddFileDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
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

    List<Map<String, Object>> findByLevel(int level, String email);

    List<Map<String, Object>> findByParentId(String parentId);

    List<String> getNoUpload(String MD5, String email, int total);

    void uploadFile(MultipartFile multipartFile, String MD5, String email, String name);

    String mergeFile(String email, String MD5, String type, String name, int total, int level, String parentId, String meta);

    int checkMergeState(String key);

    void rename(String id, String name);

    void deleteFile(String id);

    void getAvatar(String pictureName, HttpServletResponse response);
}
