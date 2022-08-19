package njnu.edu.back.dao.main;

import njnu.edu.back.pojo.FileMeta;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/05/17/16:24
 * @Description:
 */
@Repository
public interface FileMetaMapper {
    String addFileMeta(FileMeta fileMeta);

    Map<String, Object> getFileMetaById(@Param("id") String id);

    Map<String, Object> getFileMetaAndUserInfo(@Param("id") String id, @Param("email") String email);
}
