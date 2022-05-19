package njnu.edu.back.controller;

import njnu.edu.back.common.auth.AuthCheck;
import njnu.edu.back.common.resolver.JwtTokenParser;
import njnu.edu.back.common.result.JsonResult;
import njnu.edu.back.common.result.ResultUtils;
import njnu.edu.back.pojo.BrowseHistory;
import njnu.edu.back.service.BrowseHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/05/19/21:24
 * @Description:
 */
@RestController
@RequestMapping("/browseHistory")
public class BrowseHistoryController {

    @Autowired
    BrowseHistoryService browseHistoryService;

    @AuthCheck
    @RequestMapping(value = "/addHistory/{dataId}", method = RequestMethod.POST)
    public JsonResult addHistory(@JwtTokenParser("id") String userId, @PathVariable String dataId, HttpServletRequest request) {
        BrowseHistory browseHistory = new BrowseHistory();
        browseHistory.setUserId(userId);
        browseHistory.setDataId(dataId);
        browseHistory.setId(request.getRemoteAddr());
        browseHistoryService.addHistory(browseHistory);
        return ResultUtils.success();
    }
}
