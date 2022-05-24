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
        fileService.addFile(addFileDTO, email);
        return ResultUtils.success();
    }

    @AuthCheck
    @RequestMapping(value = "/findByLevel/{level}", method = RequestMethod.GET)
    public JsonResult findByLevel(@PathVariable int level, @JwtTokenParser("email") String email) {
        return ResultUtils.success(fileService.findByLevel(level, email));
    }

    @AuthCheck
    @RequestMapping(value = "/findByParentId/{parentId}", method = RequestMethod.GET)
    public JsonResult findByParentId(@PathVariable String parentId) {
        return ResultUtils.success(fileService.findByParentId(parentId));
    }

    @AuthCheck
    @RequestMapping(value = "/getNoUpload/{MD5}/{total}", method = RequestMethod.GET)
    public JsonResult getNoUpload(@PathVariable String MD5, @JwtTokenParser("email") String email, @PathVariable int total) {
        return ResultUtils.success(fileService.getNoUpload(MD5, email, total));
    }

    @AuthCheck
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public JsonResult uploadFile(@RequestParam MultipartFile file, @RequestParam String MD5, @RequestParam String name, @JwtTokenParser("email") String email) {
        fileService.uploadFile(file, MD5, email, name);
        return ResultUtils.success();
    }

    @AuthCheck
    @RequestMapping(value = "/mergeFile", method = RequestMethod.POST)
    public JsonResult mergeFile(@RequestBody JSONObject jsonObject, @JwtTokenParser("email") String email) {
        return ResultUtils.success(fileService.mergeFile(email, jsonObject.getStr("MD5"), jsonObject.getStr("type"), jsonObject.getStr("name"), jsonObject.getInt("total"), jsonObject.getInt("level"), jsonObject.getStr("parentId"), jsonObject.getStr("meta")));
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
    @RequestMapping(value = "/deleteFile/{id}", method = RequestMethod.DELETE)
    public JsonResult deleteFile(@PathVariable String id) {
        fileService.deleteFile(id);
        return ResultUtils.success();
    }

    @CrossOrigin
    @RequestMapping(value = "/avatar/{pictureName}", method = RequestMethod.GET)
    public void getAvatar(@PathVariable String pictureName, HttpServletResponse response) {
        fileService.getAvatar(pictureName, response);
    }

    @AuthCheck
    @RequestMapping(value = "/deleteFolder/{id}", method = RequestMethod.DELETE)
    public JsonResult deleteFolder(@PathVariable String id) {
        fileService.deleteFolder(id);
        return ResultUtils.success();
    }
}
