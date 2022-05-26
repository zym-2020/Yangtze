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

    /**
    * @Description: admin用户使用该接口，直接公开数据
    * @Author: Yiming
    * @Date: 2022/5/25
    */

    @AuthCheck
    @RequestMapping(value = "/addShareFile", method = RequestMethod.POST)
    public JsonResult addShareFile(@RequestParam String jsonString, @RequestParam MultipartFile file, @JwtTokenParser("email") String email, @JwtTokenParser("roles") String[] roles) {
        if(roles[0].equals("admin")) {
            JSONObject jsonObject = JSON.parseObject(jsonString);
            shareFileService.addShareFile(jsonObject, email, file);
            return ResultUtils.success();
        } else {
            throw new MyException(-99, "没有权限！");
        }
    }

    /**
    * @Description:admin用户使用改接口直接修改数据
    * @Author: Yiming
    * @Date: 2022/5/25
    */

    @AuthCheck
    @RequestMapping(value = "/updateShareFileNoAvatar", method = RequestMethod.PATCH)
    public JsonResult updateShareFileNoAvatar(@RequestBody UpdateShareFileAndFileMetaDTO updateShareFileAndFileMetaDTO, @JwtTokenParser("roles") String[] roles) {
        if(roles[0].equals("admin")) {
            shareFileService.updateShareFileAndFileMetaNoAvatar(updateShareFileAndFileMetaDTO);
            return ResultUtils.success();
        } else {
            throw new MyException(-99, "没有权限！");
        }

    }

    @AuthCheck
    @RequestMapping(value = "/updateShareFile", method = RequestMethod.PATCH)
    public JsonResult updateShare(@RequestParam String jsonString, @JwtTokenParser("roles") String[] roles, @RequestParam MultipartFile multipartFile) {
        if(roles[0].equals("admin")) {
            UpdateShareFileAndFileMetaDTO updateShareFileAndFileMetaDTO = JSONObject.parseObject(jsonString, UpdateShareFileAndFileMetaDTO.class);
            shareFileService.updateShareFileAndFileMeta(updateShareFileAndFileMetaDTO, multipartFile);
            return ResultUtils.success();
        } else {
            throw new MyException(-99, "没有权限！");
        }
    }

    @AuthCheck
    @RequestMapping(value = "/getFileInfoAndMeta/{id}", method = RequestMethod.GET)
    public JsonResult getFileInfoAndMeta(@PathVariable String id) {
        return ResultUtils.success(shareFileService.getFileInfoAndMeta(id));
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
    @RequestMapping(value = "/pageQueryByAdmin/{property}/{flag}/{page}/{size}", method = RequestMethod.GET)
    public JsonResult pageQueryByAdmin(@PathVariable String property, @PathVariable boolean flag, @PathVariable int page, @PathVariable int size) {
        return ResultUtils.success(shareFileService.pageQueryByAdmin(page, size, property, flag));
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

}
