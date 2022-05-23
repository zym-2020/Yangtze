package njnu.edu.back.dao;

import njnu.edu.back.pojo.ShareFile;
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


    List<ShareFile> pageQueryDESC(@Param("size") int size, @Param("start") int start, @Param("property") String property);

    List<ShareFile> pageQueryASC(@Param("size") int size, @Param("start") int start, @Param("property") String property);

    int countAll();

    ShareFile getShareFileById(@Param("id") String id);

    void addWatchCount(@Param("id") String id);

    void addDownload(@Param("id") String id);

    Map<String, Object> getOriginAddressAndGetOnline(@Param("id") String id);
}
