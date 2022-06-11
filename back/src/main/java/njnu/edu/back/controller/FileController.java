package njnu.edu.back.controller;

import cn.hutool.json.JSONObject;
import njnu.edu.back.common.auth.AuthCheck;
import njnu.edu.back.common.resolver.JwtTokenParser;
import njnu.edu.back.common.result.JsonResult;
import njnu.edu.back.common.result.ResultUtils;
import njnu.edu.back.pojo.dto.AddFileDTO;
import njnu.edu.back.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

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

    @AuthCheck
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public JsonResult uploadFile(@RequestParam MultipartFile file, @RequestParam String MD5, @RequestParam String name, @JwtTokenParser("email") String email) {
        fileService.uploadFile(file, MD5, email, name);
        return ResultUtils.success();
    }

    @AuthCheck
    @RequestMapping(value = "/mergeFile/{MD5}/{uuid}", method = RequestMethod.POST)
    public JsonResult mergeFile(@PathVariable String MD5, @PathVariable String uuid, @JwtTokenParser("email") String email) {
        return ResultUtils.success(fileService.mergeFile(email, MD5, uuid));
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

    @AuthCheck
    @RequestMapping(value = "/unPack", method = RequestMethod.POST)
    public JsonResult unPack(@RequestBody JSONObject jsonObject, @JwtTokenParser("email") String email) {
        String id = jsonObject.getStr("id");
        String parentId = jsonObject.getStr("parentId");
        int level = jsonObject.getInt("level");
        fileService.unPack(id, parentId, level, email);
        return ResultUtils.success();
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
}
