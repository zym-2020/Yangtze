package njnu.edu.back.dao;

import njnu.edu.back.pojo.ShareFile;
import njnu.edu.back.pojo.dto.UpdateShareFileAndFileMetaDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/05/17/16:24
 * @Description:
 */
@Repository
public interface ShareFileMapper {
    void addShareFile(ShareFile shareFile);

    List<Map<String, Object>> pageQueryByAdminDESC(@Param("size") int size, @Param("start") int start, @Param("property") String property, @Param("keyWord") String keyWord);

    List<Map<String, Object>> pageQueryByAdminASC(@Param("size") int size, @Param("start") int start, @Param("property") String property, @Param("keyWord") String keyWord);

    List<ShareFile> fuzzyQueryDESC(@Param("size") int size, @Param("start") int start, @Param("property") String property, @Param("keyWord") String keyWord);

    List<ShareFile> fuzzyQueryASC(@Param("size") int size, @Param("start") int start, @Param("property") String property, @Param("keyWord") String keyWord);

    List<ShareFile> fuzzyQueryClassifyDESC(@Param("size") int size, @Param("start") int start, @Param("property") String property, @Param("keyWord") String keyWord, @Param("tags") String[] tags);

    List<ShareFile> fuzzyQueryClassifyASC(@Param("size") int size, @Param("start") int start, @Param("property") String property, @Param("keyWord") String keyWord, @Param("tags") String[] tags);

    List<ShareFile> pageQueryByEmail(@Param("email") String email, @Param("start") int start, @Param("size") int size);

    int countAll();

    int countFuzzyQuery(@Param("keyWord") String keyWord);

    int countFuzzyQueryClassify(@Param("keyWord") String keyWord, @Param("tags") String[] tags);

    int countPageQueryByEmail(@Param("email") String email);

    ShareFile getShareFileById(@Param("id") String id);

    void addWatchCount(@Param("id") String id);

    void addDownload(@Param("id") String id);

    Map<String, Object> getOriginAddressAndGetOnline(@Param("id") String id);

    void updateFileInfoAndFileMeta(UpdateShareFileAndFileMetaDTO updateShareFileAndFileMetaDTO);

    List<Map<String, Object>> deleteShareFileById(@Param("id") String id, @Param("size") int size, @Param("start") int start, @Param("property") String property, @Param("keyWord") String keyWord);

    void updateStatusById(@Param("id") String id, @Param("status") int status);

    void offlineById(@Param("id") String id);
}
