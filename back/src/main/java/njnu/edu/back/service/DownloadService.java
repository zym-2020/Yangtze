package njnu.edu.back.service;

import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/05/19/15:11
 * @Description:
 */
public interface DownloadService {
    void downloadShareFile(HttpServletResponse response, String id);

    String getDownloadURL(String id, String userId);
}
