package njnu.edu.back.controller;

import com.alibaba.fastjson.JSONObject;
import njnu.edu.back.common.auth.AuthCheck;
import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.resolver.JwtTokenParser;
import njnu.edu.back.common.result.JsonResult;
import njnu.edu.back.common.result.ResultEnum;
import njnu.edu.back.common.result.ResultUtils;
import njnu.edu.back.service.ShareFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/05/17/17:08
 * @Description:
 */
@RestController
@RequestMapping("/share")
public class ShareFileController {
    @Autowired
    ShareFileService shareFileService;

    @AuthCheck
    @RequestMapping(value = "/addShareFile", method = RequestMethod.POST)
    public JsonResult addShareFile(@RequestBody JSONObject jsonObject, @JwtTokenParser("email") String email, @JwtTokenParser("roles") String[] roles) {
        if(roles[0].equals("admin")) {
            shareFileService.addShareFile(jsonObject, email);
            return ResultUtils.success();
        } else {
            throw new MyException(-99, "没有权限！");
        }
    }

    @RequestMapping(value = "/pageQueryOrderByDownload/{page}/{size}", method = RequestMethod.GET)
    public JsonResult pageQueryOrderByDownload(@PathVariable int page, @PathVariable int size) {
        return ResultUtils.success(shareFileService.pageQueryOrderByDownload(page, size));
    }

    @RequestMapping(value = "/getFileInfoAndMeta/{id}", method = RequestMethod.GET)
    public JsonResult getFileInfoAndMeta(@PathVariable String id) {
        return ResultUtils.success(shareFileService.getFileInfoAndMeta(id));
    }

    @RequestMapping(value = "/addWatchCount/{id}", method = RequestMethod.PATCH)
    public JsonResult addWatchCount(@PathVariable String id, @JwtTokenParser("id") String userId, HttpServletRequest request) {
        shareFileService.addWatchCount(id, userId, request.getRemoteAddr());
        return ResultUtils.success();
    }

}
