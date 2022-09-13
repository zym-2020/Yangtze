package njnu.edu.back.controller;

import cn.hutool.json.JSONObject;
import njnu.edu.back.common.auth.AuthCheck;
import njnu.edu.back.common.resolver.JwtTokenParser;
import njnu.edu.back.common.result.JsonResult;
import njnu.edu.back.common.result.ResultUtils;
import njnu.edu.back.common.utils.ZipOperate;
import njnu.edu.back.pojo.dto.AddFileDTO;
import njnu.edu.back.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @Value("${basedir}")
    String basedir;

    @AuthCheck
    @RequestMapping(value = "/addFile", method = RequestMethod.POST)
    public JsonResult addFile(@RequestBody AddFileDTO addFileDTO, @JwtTokenParser("email") String email) {
        return ResultUtils.success(fileService.addFile(addFileDTO, email));
    }

    @AuthCheck
    @RequestMapping(value = "/findByLevel/{level}", method = RequestMethod.GET)
    public JsonResult findByLevel(@PathVariable int level, @JwtTokenParser("email") String email) {
        return ResultUtils.success(fileService.findByLevel(level, email));
    }

    @AuthCheck
    @RequestMapping(value = "/findByParentId/{parentId}", method = RequestMethod.GET)
    public JsonResult findByParentId(@PathVariable String parentId, @JwtTokenParser("email") String email) {
        return ResultUtils.success(fileService.findByParentId(parentId, email));
    }

    @AuthCheck
    @RequestMapping(value = "/getNoUpload", method = RequestMethod.POST)
    public JsonResult getNoUpload(@RequestBody JSONObject jsonObject, @JwtTokenParser("email") String email) {
        String MD5 = jsonObject.getStr("MD5");
        int total = jsonObject.getInt("total");
        JSONObject metaData = jsonObject.getJSONObject("meta");
        return ResultUtils.success(fileService.getNoUpload(MD5, email, total, metaData));
    }
    @CrossOrigin
    @AuthCheck
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public JsonResult uploadFile(@RequestParam MultipartFile file, @RequestParam String MD5, @RequestParam String name, @JwtTokenParser("email") String email) {
        fileService.uploadFile(file, MD5, email, name);
        return ResultUtils.success();
    }
    @CrossOrigin
    @AuthCheck
    @RequestMapping(value = "/mergeFile", method = RequestMethod.POST)
    public JsonResult mergeFile(@RequestBody JSONObject jsonObject, @JwtTokenParser("email") String email) {
        String MD5 = jsonObject.getStr("MD5");
        String uid = jsonObject.getStr("uid");
        int total = jsonObject.getInt("total");
        String name = jsonObject.getStr("name");
        String folderId = jsonObject.getStr("folderId");
        return ResultUtils.success(fileService.mergeFile(email, MD5, uid, total, name, folderId));
    }

    @AuthCheck
    @RequestMapping(value = "/checkMergeState/{key}", method = RequestMethod.GET)
    public JsonResult checkMergeState(@PathVariable String key) {
        return ResultUtils.success(fileService.checkMergeState(key));
    }

    @AuthCheck
    @RequestMapping(value = "/rename", method = RequestMethod.PATCH)
    public JsonResult rename(@RequestBody JSONObject jsonObject) {
        fileService.rename(jsonObject.getStr("id"), jsonObject.getStr("name"));
        return ResultUtils.success();
    }

    @AuthCheck
    @CrossOrigin
    @RequestMapping(value = "/deleteFilesOrFolders", method = RequestMethod.DELETE)
    public JsonResult deleteFilesOrFolders(@RequestBody JSONObject jsonObject) {
        fileService.deleteFilesOrFolders(jsonObject);
        return ResultUtils.success();
    }

    @CrossOrigin
    @RequestMapping(value = "/avatar/{pictureName}", method = RequestMethod.GET)
    public void getAvatar(@PathVariable String pictureName, HttpServletResponse response) {
        fileService.getAvatar(pictureName, response);
    }

    @CrossOrigin
    @RequestMapping(value = "/thumbnail/{pictureName}", method = RequestMethod.GET)
    public void getThumbnail(@PathVariable String pictureName, HttpServletResponse response) {
        fileService.getThumbnail(pictureName, response);
    }

    @CrossOrigin
    @RequestMapping(value = "/photos/{pictureName}", method = RequestMethod.GET)
    public void getPhotos(@PathVariable String pictureName, HttpServletResponse response) {
        fileService.getPhotos(pictureName, response);
    }

    @AuthCheck
    @CrossOrigin
    @RequestMapping(value = "/getDownloadURL/{id}", method = RequestMethod.GET)
    public JsonResult getDownloadURL(@PathVariable String id, @JwtTokenParser("id") String userId) {
        return ResultUtils.success(fileService.getDownloadURL(id, userId));
    }

    @AuthCheck
    @RequestMapping(value = "/getTree", method = RequestMethod.GET)
    public JsonResult getTree(@JwtTokenParser("email") String email) {
        return ResultUtils.success(fileService.getFolderTree(email));
    }

    @AuthCheck
    @RequestMapping(value = "/updateParentIdAndLevel", method = RequestMethod.POST)
    public JsonResult updateParentIdAndLevel(@RequestBody JSONObject jsonObject) {

        fileService.updateParentIdAndLevel(jsonObject);
        return ResultUtils.success();
    }

    @AuthCheck
    @RequestMapping(value = "/compressFile", method = RequestMethod.POST)
    public JsonResult compressFile(@RequestBody JSONObject jsonObject, @JwtTokenParser("email") String email) {
        fileService.compressFile(jsonObject, email);
        return ResultUtils.success();
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
}