package njnu.edu.back.controller;

import cn.hutool.json.JSONObject;
import njnu.edu.back.common.auth.AuthCheck;
import njnu.edu.back.common.resolver.JwtTokenParser;
import njnu.edu.back.common.result.JsonResult;
import njnu.edu.back.common.result.ResultUtils;
import njnu.edu.back.common.utils.ZipOperate;
import njnu.edu.back.pojo.File;
import njnu.edu.back.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/05/02/15:48
 * @Description:
 */
@RestController
@RequestMapping("/file")
public class FileController {
    @Autowired
    FileService fileService;


    @AuthCheck
    @RequestMapping(value = "/addFile", method = RequestMethod.POST)
    public JsonResult addFile(@RequestBody File file, @JwtTokenParser("email") String email) {
        return ResultUtils.success(fileService.addFile(file, email));
    }

    @AuthCheck
    @RequestMapping(value = "/findByFolderId/{folderId}", method = RequestMethod.GET)
    public JsonResult findByParentId(@PathVariable String folderId, @JwtTokenParser("email") String email) {
        return ResultUtils.success(fileService.findByFolderId(folderId, email));
    }


    @CrossOrigin
    @AuthCheck
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public JsonResult uploadFile(@RequestParam MultipartFile file, @RequestParam String key, @RequestParam String name, @JwtTokenParser("email") String email) {
        fileService.uploadFile(file, key, email, name);
        return ResultUtils.success();
    }

    @AuthCheck
    @RequestMapping(value = "/mergeFile", method = RequestMethod.POST)
    public JsonResult mergeFile(@RequestBody JSONObject jsonObject, @JwtTokenParser("email") String email) {
        String key = jsonObject.getStr("key");
        int total = jsonObject.getInt("total");
        String name = jsonObject.getStr("name");
        String folderId = jsonObject.getStr("folderId");
        return ResultUtils.success(fileService.mergeFile(email, key, total, name, folderId));
    }

    @AuthCheck
    @RequestMapping(value = "/checkMergeState/{key}", method = RequestMethod.GET)
    public JsonResult checkMergeState(@PathVariable String key) {
        return ResultUtils.success(fileService.checkMergeState(key));
    }


    @AuthCheck
    @RequestMapping(value = "/deleteFilesOrFolders", method = RequestMethod.DELETE)
    public JsonResult deleteFilesOrFolders(@RequestBody JSONObject jsonObject) {
        fileService.deleteFilesOrFolders(jsonObject);
        return ResultUtils.success();
    }

    @AuthCheck
    @RequestMapping(value = "/getDownloadURL/{id}", method = RequestMethod.GET)
    public JsonResult getDownloadURL(@PathVariable String id, @JwtTokenParser("id") String userId) {
        return ResultUtils.success(fileService.getDownloadURL(id, userId));
    }

    /**
     * @Description:加密下载，下载条目下的文件
     * @Author: Yiming
     * @Date: 2022/8/20
     */

    @CrossOrigin
    @RequestMapping(value = "/downloadInList/{userId}/{id}/{dataListId}", method = RequestMethod.GET)
    public void downloadInList(@PathVariable String userId, @PathVariable String id, @PathVariable String dataListId, HttpServletResponse response, HttpServletRequest request) {
        fileService.downloadInList(userId, id, dataListId, response, request);
    }

    @CrossOrigin
    @RequestMapping(value = "/downloadLocalFile/{userId}/{id}", method = RequestMethod.GET)
    public void downloadLocalFile(@PathVariable String userId, @PathVariable String id, HttpServletResponse response) {
        fileService.downloadLocalFile(userId, id, response);
    }


    /**
     * @Description:方便录入数据的接口
     * @Author: Yiming
     * @Date: 2022/8/23
     */
    @RequestMapping(value = "/importData", method = RequestMethod.POST)
    public JsonResult importData(@RequestBody JSONObject jsonObject) {
        fileService.importData(jsonObject.getStr("path"), jsonObject.getStr("email"), jsonObject.getStr("time"), jsonObject.getStr("visualType"), jsonObject.getStr("visualId"));
        return ResultUtils.success();
    }

    @RequestMapping(value = "/importGrid", method = RequestMethod.POST)
    public JsonResult importGrid() {
        fileService.importGrid();
        return ResultUtils.success();
    }

    /**
    * @Description:检查文件完整性
    * @Author: Yiming
    * @Date: 2022/10/4
    */

    @RequestMapping(value = "/check", method = RequestMethod.POST)
    public JsonResult check(@RequestBody JSONObject jsonObject) {
        return ResultUtils.success(fileService.check(jsonObject.getStr("path")));
    }
}