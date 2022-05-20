package njnu.edu.back.controller;

import njnu.edu.back.common.auth.AuthCheck;
import njnu.edu.back.common.resolver.JwtTokenParser;
import njnu.edu.back.common.result.JsonResult;
import njnu.edu.back.common.result.ResultUtils;
import njnu.edu.back.pojo.DownloadHistory;
import njnu.edu.back.service.DownloadHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/05/19/21:40
 * @Description:
 */
@RestController
@RequestMapping("/downloadHistory")
public class DownloadHistoryController{

    @Autowired
    DownloadHistoryService downloadHistoryService;

    @AuthCheck
    @RequestMapping(value = "/addHistory", method = RequestMethod.POST)
    public JsonResult addHistory(@RequestBody DownloadHistory downloadHistory, @JwtTokenParser("id") String userId, HttpServletRequest request) {
        downloadHistory.setUserId(userId);
        downloadHistory.setIp(request.getRemoteAddr());
        downloadHistoryService.addHistory(downloadHistory);
        return ResultUtils.success();
    }

    @AuthCheck
    @RequestMapping(value = "/pageQuery/{dataId}/{size}/{page}", method = RequestMethod.GET)
    public JsonResult pageQuery(@PathVariable int size, @PathVariable int page, @PathVariable String dataId) {
        return ResultUtils.success(downloadHistoryService.pageQuery(size, page, dataId));
    }

}
