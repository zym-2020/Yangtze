package njnu.edu.back.service;


import org.springframework.web.multipart.MultipartFile;


import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/04/07/17:26
 * @Description:
 */
public interface ProjectService {

    String addProject(String projectName, MultipartFile file, boolean isPublic, String email);

    Map<String, Object> getAll(String keyword, int page, int size);

    Map<String, Object> getAllByEmail(String email, int page, int size);

    Map<String, Object> getProjectInfo(String projectId);

    void addData(String projectId, List<Map<String, String>> list);

    List<Map<String, Object>> getData(String projectId);

    void delData(String projectId, String dataListId, String fileId);

    void updateLayer(String projectId, List<String> list);

    List<Map<String, Object>> getLayersInfo(String projectId);

    void updateBasemap(String projectId, String url);

    void updatePublicState(String projectId, boolean b);

    String updateProjectInfo(MultipartFile file, String projectName, String id, boolean isPublic);

    void deleteProject(String projectId, String email, String role);

    Map<String, Object> getAllByAdmin(String keyword, int page, int size, String role);

    String copyProject(String projectId, String creator, String projectName, boolean isPublic, MultipartFile file, String email);
}
