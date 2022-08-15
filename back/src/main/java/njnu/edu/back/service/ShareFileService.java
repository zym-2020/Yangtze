package njnu.edu.back.service;

import com.alibaba.fastjson.JSONObject;
import njnu.edu.back.pojo.ShareFile;
import njnu.edu.back.pojo.dto.UpdateShareFileAndFileMetaDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/05/17/16:55
 * @Description:
 */
public interface ShareFileService {
    Map<String, Object> addShareFile(JSONObject jsonObject, String email, MultipartFile file,MultipartFile file2);

    Map<String, Object> pageQueryByAdmin(int page, int size, String property, boolean flag, String keyWord);

    Map<String, Object> fuzzyQueryClassify(int page, int size, String property, boolean flag, String keyWord, String[] tags);

    Map<String, Object> getFileInfoAndMeta(String id);

    Map<String, Object> getFileInfoAndMetaAndUserInfo(String id);

    Map<String, Object> pageQueryByEmail(String email, int page, int size);

    void addWatchCount(String id, String userId, String ip);

    Map<String, Object> fuzzyQuery(int page, int size, String property, boolean flag, String keyWords);

    void updateShareFileAndFileMetaNoAvatar(UpdateShareFileAndFileMetaDTO updateShareFileAndFileMetaDTO);

    void updateShareFileAndFileMeta(UpdateShareFileAndFileMetaDTO updateShareFileAndFileMetaDTO, MultipartFile multipartFile,MultipartFile multipartFile2);

    void updateShareFileAndFileMeta(UpdateShareFileAndFileMetaDTO updateShareFileAndFileMetaDTO, MultipartFile multipartFile,int flag);

    List<Map<String, Object>> deleteShareFileById(int page, int size, String property, String keyWord, String id);

    void updateStatusById(String id, int status);

    void offlineById(String id);

    void examineById(String id);

    void onlineById(String id);

    List<ShareFile> deleteShareFileAsMember(String id, int size, int page, String email);


    Map<String, Object> getShareFileById(String id);

    Map<String, Object> getOtherTags(String id);




}