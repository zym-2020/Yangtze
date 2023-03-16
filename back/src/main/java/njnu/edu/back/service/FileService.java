package njnu.edu.back.service;

import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSONArray;
import njnu.edu.back.pojo.File;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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
    String addFile(File file, String email);

    List<Map<String, Object>> findByFolderId(String folderId, String email);

    void uploadFile(MultipartFile multipartFile, String key, String email, String name);

    String mergeFile(String email, String tempFolderName, int total, String name, String folderId);

    int checkMergeState(String key);

    void deleteFilesOrFolders(JSONObject jsonObject);

    String getDownloadURL(String id, String userId);

    void downloadInList(String userId, String id, String dataListId, HttpServletResponse response, HttpServletRequest request);

    void downloadLocalFile(String userId, String id, HttpServletResponse response);

    String bindVisualData(JSONObject jsonObject);

    void cancelVisualBind(String id);

    Map<String, Object> getVisualAuditFiles(String role, String keyword, Integer page, Integer size);

    void changeFileVisualState(String id, Integer state, String role);

    String getView(String visualId);
}
