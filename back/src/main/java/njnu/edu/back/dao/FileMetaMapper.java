package njnu.edu.back.dao;

import njnu.edu.back.pojo.FileMeta;
import org.springframework.stereotype.Repository;

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
}
