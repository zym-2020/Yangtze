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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    @CrossOrigin
    @AuthCheck
    @RequestMapping(value = "/addDataList", method = RequestMethod.POST)
    public JsonResult addDataList(@RequestParam MultipartFile avatar, @RequestParam MultipartFile thumbnail, @RequestParam String jsonString, @JwtTokenParser("email") String email) {
        dataListService.addDataList(avatar, thumbnail, jsonString, email);
        return ResultUtils.success();
    }

    @AuthCheck
    @CrossOrigin
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
    @CrossOrigin
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
    @CrossOrigin
    @RequestMapping(value = "/fuzzyQuery", method = RequestMethod.POST)
    public JsonResult fuzzyQuery(@RequestBody JSONObject jsonObject) {
        int page = jsonObject.getIntValue("page");
        int size = jsonObject.getIntValue("size");
        String keyword = jsonObject.getString("titleKeyword");
        String[] tags = jsonObject.getObject("tags", String[].class);
        String property = jsonObject.getString("property");
        Boolean flag = jsonObject.getBoolean("flag");
        String type = jsonObject.getString("type");
        return ResultUtils.success(dataListService.fuzzyQuery(page, size, keyword, tags, property, flag, type));
    }

    @AuthCheck
    @RequestMapping(value = "/pageQueryByEmail", method = RequestMethod.POST)
    public JsonResult pageQueryByEmail(@JwtTokenParser("email") String email, @RequestBody JSONObject jsonObject) {
        int size = jsonObject.getIntValue("size");
        int page = jsonObject.getIntValue("page");
        String keyword = jsonObject.getString("keyword");
        String type = jsonObject.getString("type");
        String property = jsonObject.getString("property");
        return ResultUtils.success(dataListService.pageQueryByEmail(email, size, page, keyword, type, property));
    }

    @AuthCheck
    @RequestMapping(value = "/deleteAsMember", method = RequestMethod.DELETE)
    public JsonResult deleteAsMember(@RequestBody JSONObject jsonObject, @JwtTokenParser("email") String email) {
        String id = jsonObject.getString("id");
        int size = jsonObject.getIntValue("size");
        int page = jsonObject.getIntValue("page");
        String type = jsonObject.getString("type");
        String property = jsonObject.getString("property");
        return ResultUtils.success(dataListService.deleteAsMember(id, email, page, size, type, property));
    }

    @AuthCheck
    @CrossOrigin
    @RequestMapping(value = "/getDownloadURL/{id}", method = RequestMethod.GET)
    public JsonResult getDownloadURL(@PathVariable String id, @JwtTokenParser("id") String userId) {
        return ResultUtils.success(dataListService.getDownloadURL(id, userId));
    }

    @RequestMapping(value = "/downloadAll/{userId}/{id}", method = RequestMethod.GET)
    public void downloadAll(@PathVariable String userId, @PathVariable String id, HttpServletRequest request, HttpServletResponse response) {
        dataListService.downloadAll(userId, id, request ,response);
    }

    @AuthCheck
    @RequestMapping(value = "/findFiles/{dataListId}", method = RequestMethod.GET)
    public JsonResult findFiles(@PathVariable String dataListId) {
        return ResultUtils.success(dataListService.findFiles(dataListId));
    }

    @AuthCheck
    @RequestMapping(value = "/updateStatusById/{id}/{status}", method = RequestMethod.PATCH)
    public JsonResult updateStatusById(@PathVariable String id, @PathVariable int status, @JwtTokenParser("role") String role, @JwtTokenParser("email") String email) {
        dataListService.updateStatusById(id, status, role, email);
        return ResultUtils.success();
    }

    @AuthCheck
    @RequestMapping(value = "/getHot/{size}", method = RequestMethod.GET)
    public JsonResult getHot(@PathVariable Integer size) {
        return ResultUtils.success(dataListService.getHot(size));
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
        String type = jsonObject.getString("type");
        int status = jsonObject.getIntValue("status");
        return ResultUtils.success(dataListService.fuzzyQueryAdmin(page, size, keyword, tags, property, flag, type, status));
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
        String id = jsonObject.getString("id");
        String type = jsonObject.getString("type");
        int status = jsonObject.getIntValue("status");
        return ResultUtils.success(dataListService.deleteByAdmin(page, size, keyword, tags, property, flag, id, type, status));
    }

    @AuthCheck
    @CrossOrigin
    @RequestMapping(value = "/clearQuery", method = RequestMethod.POST)
    public JsonResult clearQuery(@RequestBody JSONObject jsonObject) {
        String[] tags = jsonObject.getObject("tags", String[].class);
        String type = jsonObject.getString("type");
        String location = jsonObject.getString("location");
        String startDate = jsonObject.getString("startDate");
        String endDate = jsonObject.getString("endDate");
        return ResultUtils.success(dataListService.clearQuery( tags, type,location,startDate,endDate));
    }

    @AuthCheck
    @RequestMapping(value = "/getSimilarData/{type}/{id}/{size}/{page}", method = RequestMethod.GET)
    public JsonResult getSimilarData(@PathVariable String type, @PathVariable String id, @PathVariable int size, @PathVariable int page) {
        return ResultUtils.success(dataListService.getSimilarData(type, id, size, page));
    }

}
