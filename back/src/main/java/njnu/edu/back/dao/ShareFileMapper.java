package njnu.edu.back.dao;

import njnu.edu.back.pojo.ShareFile;
import org.springframework.stereotype.Repository;

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
}
