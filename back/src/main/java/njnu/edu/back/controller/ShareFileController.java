package njnu.edu.back.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import njnu.edu.back.common.auth.AuthCheck;
import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.resolver.JwtTokenParser;
import njnu.edu.back.common.result.JsonResult;
import njnu.edu.back.common.result.ResultEnum;
import njnu.edu.back.common.result.ResultUtils;
import njnu.edu.back.pojo.FileMeta;
import njnu.edu.back.pojo.dto.UpdateShareFileAndFileMetaDTO;
import njnu.edu.back.service.ShareFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public JsonResult addShareFile(@RequestParam String jsonString, @RequestParam MultipartFile file, @JwtTokenParser("email") String email) {
        JSONObject jsonObject = JSON.parseObject(jsonString);
        return ResultUtils.success(shareFileService.addShareFile(jsonObject, email, file));
    }


    @AuthCheck
    @RequestMapping(value = "/updateShareFileNoAvatar", method = RequestMethod.PATCH)
    public JsonResult updateShareFileNoAvatar(@RequestBody UpdateShareFileAndFileMetaDTO updateShareFileAndFileMetaDTO) {
        shareFileService.updateShareFileAndFileMetaNoAvatar(updateShareFileAndFileMetaDTO);
        return ResultUtils.success();
    }

    @AuthCheck
    @RequestMapping(value = "/updateShareFile", method = RequestMethod.PATCH)
    public JsonResult updateShare(@RequestParam String jsonString, @RequestParam MultipartFile multipartFile) {
        UpdateShareFileAndFileMetaDTO updateShareFileAndFileMetaDTO = JSONObject.parseObject(jsonString, UpdateShareFileAndFileMetaDTO.class);
        shareFileService.updateShareFileAndFileMeta(updateShareFileAndFileMetaDTO, multipartFile);
        return ResultUtils.success();
    }

    @AuthCheck
    @RequestMapping(value = "/getFileInfoAndMeta/{id}", method = RequestMethod.GET)
    public JsonResult getFileInfoAndMeta(@PathVariable String id) {
        return ResultUtils.success(shareFileService.getFileInfoAndMeta(id));
    }

    @AuthCheck
    @RequestMapping(value = "/getFileInfoAndMetaAndUserInfo/{id}", method = RequestMethod.GET)
    public JsonResult getFileInfoAndMetaAndUserInfo(@PathVariable String id) {
        return ResultUtils.success(shareFileService.getFileInfoAndMetaAndUserInfo(id));
    }

    @AuthCheck
    @RequestMapping(value = "/addWatchCount/{id}", method = RequestMethod.PATCH)
    public JsonResult addWatchCount(@PathVariable String id, @JwtTokenParser("id") String userId, HttpServletRequest request) {
        shareFileService.addWatchCount(id, userId, request.getRemoteAddr());
        return ResultUtils.success();
    }

    @AuthCheck
    @RequestMapping(value = "/fuzzyQuery", method = RequestMethod.POST)
    public JsonResult fuzzyQuery(@RequestBody JSONObject jsonObject) {
        int page = jsonObject.getInteger("page");
        int size = jsonObject.getInteger("size");
        String property = jsonObject.getString("property");
        String keyWord = jsonObject.getString("keyWord");
        boolean flag = jsonObject.getBoolean("flag");
        return ResultUtils.success(shareFileService.fuzzyQuery(page, size, property, flag, keyWord));
    }

    @AuthCheck
    @RequestMapping(value = "/pageQueryByAdmin", method = RequestMethod.POST)
    public JsonResult pageQueryByAdmin(@RequestBody JSONObject jsonObject, @JwtTokenParser("roles") String[] roles) {
        if(roles[0].equals("admin")) {
            int page = jsonObject.getInteger("page");
            int size = jsonObject.getInteger("size");
            String property = jsonObject.getString("property");
            String keyWord = jsonObject.getString("keyWord");
            boolean flag = jsonObject.getBoolean("flag");
            return ResultUtils.success(shareFileService.pageQueryByAdmin(page, size, property, flag, keyWord));
        } else {
            throw new MyException(-99, "没有权限！");
        }

    }

    @AuthCheck
    @RequestMapping(value = "/fuzzyQueryClassify", method = RequestMethod.POST)
    public JsonResult fuzzyQueryClassify(@RequestBody JSONObject jsonObject) {
        int size = jsonObject.getInteger("size");
        int page = jsonObject.getInteger("page");
        String property = jsonObject.getString("property");
        String keyWord = jsonObject.getString("keyWord");
        boolean flag = jsonObject.getBoolean("flag");
        JSONArray jsonArray = jsonObject.getJSONArray("tags");
        String[] tags = jsonArray.toArray(new String[jsonArray.size()]);
        return ResultUtils.success(shareFileService.fuzzyQueryClassify(page, size, property, flag, keyWord, tags));
    }

    @AuthCheck
    @RequestMapping(value = "/pageQueryByEmail/{page}/{size}", method = RequestMethod.GET)
    public JsonResult pageQueryByEmail(@JwtTokenParser("email") String email, @PathVariable int page, @PathVariable int size) {
        return ResultUtils.success(shareFileService.pageQueryByEmail(email, page, size));
    }

    @AuthCheck
    @RequestMapping(value = "/deleteShareFileById", method = RequestMethod.DELETE)
    public JsonResult deleteShareFileById(@RequestBody JSONObject jsonObject, @JwtTokenParser("roles") String[] roles) {
        if(roles[0].equals("admin")) {
            int size = jsonObject.getInteger("size");
            int page = jsonObject.getInteger("page");
            String property = jsonObject.getString("property");
            String keyWord = jsonObject.getString("keyWord");
            String id = jsonObject.getString("id");
            return ResultUtils.success(shareFileService.deleteShareFileById(page, size, property, keyWord, id));
        } else {
            throw new MyException(-99, "没有权限！");
        }

    }

    /**
    * @Description:admin权限接口，修改资源条目状态
    * @Author: Yiming
    * @Date: 2022/5/30
    */

    @AuthCheck
    @RequestMapping(value = "/updateStatusById", method = RequestMethod.PATCH)
    public JsonResult updateStatusById(@RequestBody JSONObject jsonObject, @JwtTokenParser("roles") String[] roles) {
        if(roles[0].equals("admin")) {
            shareFileService.updateStatusById(jsonObject.getString("id"), jsonObject.getInteger("status"));
            return ResultUtils.success();
        } else {
            throw new MyException(-99, "没有权限！");
        }
    }

    @AuthCheck
    @RequestMapping(value = "/offlineById/{id}", method = RequestMethod.PATCH)
    public JsonResult offlineById(@PathVariable String id) {
        shareFileService.offlineById(id);
        return ResultUtils.success();
    }

    @AuthCheck
    @RequestMapping(value = "/examineById/{id}", method = RequestMethod.PATCH)
    public JsonResult examineById(@PathVariable String id) {
        shareFileService.examineById(id);
        return ResultUtils.success();
    }

    @AuthCheck
    @RequestMapping(value = "/onlineById/{id}", method = RequestMethod.PATCH)
    public JsonResult onlineById(@PathVariable String id) {
        shareFileService.onlineById(id);
        return ResultUtils.success();
    }

    @AuthCheck
    @RequestMapping(value = "/deleteShareFileAsMember/{id}/{page}/{size}", method = RequestMethod.DELETE)
    public JsonResult deleteShareFileAsMember(@PathVariable String id, @PathVariable int page, @PathVariable int size, @JwtTokenParser("email") String email) {
        return ResultUtils.success(shareFileService.deleteShareFileAsMember(id, size, page, email));
    }

    @AuthCheck
    @RequestMapping(value = "/getShareFileById/{id}", method = RequestMethod.PATCH)
    public JsonResult getShareFileById(@PathVariable String id) {
        return ResultUtils.success(shareFileService.getShareFileById(id));
    }
}
