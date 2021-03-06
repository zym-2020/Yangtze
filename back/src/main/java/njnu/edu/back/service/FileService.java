package njnu.edu.back.service;

import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSONArray;
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
    String addFile(AddFileDTO addFileDTO, String email);

    List<Map<String, Object>> findByLevel(int level, String email);

    List<Map<String, Object>> findByParentId(String parentId, String email);

    List<String> getNoUpload(String MD5, String email, int total, JSONObject jsonObject);

    void uploadFile(MultipartFile multipartFile, String MD5, String email, String name);

    String mergeFile(String email, String MD5, String uuid);

    int checkMergeState(String key);

    void rename(String id, String name);

    void deleteFilesOrFolders(JSONObject jsonObject);

    void getAvatar(String pictureName, HttpServletResponse response);

    void unPack(String filePath, String parentId, int level, String email);

    List<Map<String, Object>> getFolderTree(String email);

    void updateParentIdAndLevel(JSONObject jsonObject);

    void compressFile(JSONObject jsonObject, String email);
}
