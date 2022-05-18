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

    List<ShareFile> pageQueryOrderByDownload(@Param("size") int size, @Param("start") int start);

    int countAll();

    ShareFile getShareFileById(@Param("id") String id);
}
