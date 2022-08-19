package njnu.edu.back.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/08/19/17:03
 * @Description:
 */
public interface DataListService {
    void addDataList(MultipartFile avatar, MultipartFile thumbnail, String jsonString, String email);

    void updateList(MultipartFile avatar, MultipartFile thumbnail, String jsonString);

    Map<String, Object> getFileInfo(String id);

    Map<String, Object> getFileInfoAndUserInfo(String id);

    void addWatchCount(String id);

    Map<String, Object> fuzzyQuery(int page, int size, String keyword, String[] tags, String property, Boolean flag);

    Map<String, Object> fuzzyQueryAdmin(int page, int size, String keyword, String[] tags, String property, Boolean flag, int status);

    Map<String, Object> deleteShareFileById(int page, int size, String keyword, String[] tags, String property, Boolean flag, int status, String id);
}
