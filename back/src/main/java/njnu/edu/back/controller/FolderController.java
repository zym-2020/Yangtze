package njnu.edu.back.controller;

import com.alibaba.fastjson.JSONObject;
import njnu.edu.back.common.resolver.JwtTokenParser;
import njnu.edu.back.common.result.JsonResult;
import njnu.edu.back.common.result.ResultUtils;
import njnu.edu.back.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/08/19/16:48
 * @Description:
 */
@RestController
@RequestMapping("/folder")
public class FolderController {
    @Autowired
    FolderService folderService;

    @RequestMapping(value = "/findByParentId/{parentId}", method = RequestMethod.GET)
    public JsonResult findByParentId(@PathVariable String parentId, @JwtTokenParser("email") String email) {
        return ResultUtils.success(folderService.findByParentId(parentId, email));
    }

    @RequestMapping(value = "/addFolder", method = RequestMethod.POST)
    public JsonResult addFolder(@RequestBody JSONObject jsonObject, @JwtTokenParser("email") String email) {
        return ResultUtils.success(folderService.addFolder(jsonObject.getString("parentId"), jsonObject.getString("folderName"), email));
    }
}
