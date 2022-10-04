package njnu.edu.back.controller;

import com.alibaba.fastjson.JSONObject;
import njnu.edu.back.common.auth.AuthCheck;
import njnu.edu.back.common.resolver.JwtTokenParser;
import njnu.edu.back.common.result.JsonResult;
import njnu.edu.back.common.result.ResultUtils;
import njnu.edu.back.service.DataListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/08/19/17:09
 * @Description:
 */
@RestController
@RequestMapping("/dataList")
public class DataListController {
    @Autowired
    DataListService dataListService;

    @AuthCheck
    @RequestMapping(value = "/addDataList", method = RequestMethod.POST)
    public JsonResult addDataList(@RequestParam MultipartFile avatar, @RequestParam MultipartFile thumbnail, @RequestParam String jsonString, @JwtTokenParser("email") String email) {
        dataListService.addDataList(avatar, thumbnail, jsonString, email);
        return ResultUtils.success();
    }

    @AuthCheck
    @RequestMapping(value = "/updateDataList", method = RequestMethod.PATCH)
    public JsonResult updateDataList(@RequestParam MultipartFile avatar, @RequestParam MultipartFile thumbnail, @RequestParam String jsonString) {
        dataListService.updateList(avatar, thumbnail, jsonString);
        return ResultUtils.success();
    }

    @AuthCheck
    @RequestMapping(value = "/getFileInfo/{id}", method = RequestMethod.GET)
    public JsonResult getFileInfo(@PathVariable String id) {
        return ResultUtils.success(dataListService.getFileInfo(id));
    }

    @AuthCheck
    @RequestMapping(value = "/getFileInfoAndUserInfo/{id}", method = RequestMethod.GET)
    public JsonResult getFileInfoAndUserInfo(@PathVariable String id) {
        return ResultUtils.success(dataListService.getFileInfoAndUserInfo(id));
    }

    @AuthCheck
    @RequestMapping(value = "/addWatchCount/{id}", method = RequestMethod.PATCH)
    public JsonResult addWatchCount(@PathVariable String id) {
        dataListService.addWatchCount(id);
        return ResultUtils.success();
    }

    @AuthCheck
    @RequestMapping(value = "/fuzzyQuery", method = RequestMethod.POST)
    public JsonResult fuzzyQuery(@RequestBody JSONObject jsonObject) {
        int page = jsonObject.getIntValue("page");
        int size = jsonObject.getIntValue("size");
        String keyword = jsonObject.getString("keyword");
        String[] tags = jsonObject.getObject("tags", String[].class);
        String property = jsonObject.getString("property");
        Boolean flag = jsonObject.getBoolean("flag");
        return ResultUtils.success(dataListService.fuzzyQuery(page, size, keyword, tags, property, flag));
    }

    @AuthCheck
    @RequestMapping(value = "/pageQueryByEmail/{page}/{size}", method = RequestMethod.GET)
    public JsonResult pageQueryByEmail(@JwtTokenParser("email") String email, @PathVariable int page, @PathVariable int size) {
        return ResultUtils.success(dataListService.pageQueryByEmail(email, size, page));
    }

    @AuthCheck
    @RequestMapping(value = "/deleteAsMember", method = RequestMethod.POST)
    public JsonResult deleteAsMember(@RequestBody JSONObject jsonObject, @JwtTokenParser("email") String email) {
        String id = jsonObject.getString("id");
        int size = jsonObject.getIntValue("size");
        int page = jsonObject.getIntValue("page");
        return ResultUtils.success(dataListService.deleteAsMember(id, email, page, size));
    }


    /**
    * @Description:以下是Admin用户接口
    * @Author: Yiming
    * @Date: 2022/8/20
    */


    @AuthCheck
    @RequestMapping(value = "/fuzzyQueryAdmin", method = RequestMethod.POST)
    public JsonResult fuzzyQueryAdmin(@RequestBody JSONObject jsonObject) {
        int page = jsonObject.getIntValue("page");
        int size = jsonObject.getIntValue("size");
        String keyword = jsonObject.getString("keyword");
        String[] tags = jsonObject.getObject("tags", String[].class);
        String property = jsonObject.getString("property");
        Boolean flag = jsonObject.getBoolean("flag");
        int status = jsonObject.getIntValue("status");
        return ResultUtils.success(dataListService.fuzzyQueryAdmin(page, size, keyword, tags, property, flag, status));
    }

    @AuthCheck
    @RequestMapping(value = "/deleteByAdmin", method = RequestMethod.DELETE)
    public JsonResult deleteByAdmin(@RequestBody JSONObject jsonObject) {
        int page = jsonObject.getIntValue("page");
        int size = jsonObject.getIntValue("size");
        String keyword = jsonObject.getString("keyword");
        String[] tags = jsonObject.getObject("tags", String[].class);
        String property = jsonObject.getString("property");
        Boolean flag = jsonObject.getBoolean("flag");
        int status = jsonObject.getIntValue("status");
        String id = jsonObject.getString("id");
        return ResultUtils.success(dataListService.deleteByAdmin(page, size, keyword, tags, property, flag, status, id));
    }

    @AuthCheck
    @RequestMapping(value = "/updateStatusById/{id}/{status}", method = RequestMethod.PATCH)
    public JsonResult updateStatusById(@PathVariable String id, @PathVariable int status) {
        dataListService.updateStatusById(id, status);
        return ResultUtils.success();
    }


}