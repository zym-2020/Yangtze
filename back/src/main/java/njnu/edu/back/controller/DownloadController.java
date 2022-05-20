package njnu.edu.back.controller;

import njnu.edu.back.common.auth.AuthCheck;
import njnu.edu.back.common.resolver.JwtTokenParser;
import njnu.edu.back.common.result.JsonResult;
import njnu.edu.back.common.result.ResultUtils;
import njnu.edu.back.service.DownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/05/19/15:10
 * @Description:
 */
@RestController
@RequestMapping("/download")
public class DownloadController {

    @Autowired
    DownloadService downloadService;

    @AuthCheck
    @RequestMapping(value = "/getDownloadURL/{id}", method = RequestMethod.GET)
    public JsonResult getDownloadURL(@PathVariable String id, @JwtTokenParser("id") String userId) {
        return ResultUtils.success(downloadService.getDownloadURL(id, userId));
    }

    /**
    * @Description:下载原始文件接口
    * @Author: Yiming
    * @Date: 2022/5/19
    */

    @RequestMapping(value = "/downloadShareFile/{userId}/{id}", method = RequestMethod.GET)
    public void downloadShareFile(@PathVariable String id, HttpServletResponse response, @PathVariable String userId, HttpServletRequest request) {
        downloadService.downloadShareFile(response, id, userId, request.getRemoteAddr());
    }
}
