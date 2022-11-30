package njnu.edu.back.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import njnu.edu.back.common.auth.AuthCheck;
import njnu.edu.back.common.resolver.JwtTokenParser;
import njnu.edu.back.common.result.JsonResult;
import njnu.edu.back.common.result.ResultUtils;


import njnu.edu.back.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/04/07/18:30
 * @Description:
 */
@RestController
@RequestMapping("/project")
public class ProjectController {
    @Autowired
    ProjectService projectService;

    @AuthCheck
    @RequestMapping(value = "/addProject", method = RequestMethod.POST)
    public JsonResult addProject(@RequestParam String projectName, @RequestParam MultipartFile file, @RequestParam boolean isPublic, @JwtTokenParser("email") String email) {
        return ResultUtils.success(projectService.addProject(projectName, file, isPublic, email));
    }

    @AuthCheck
    @RequestMapping(value = "/getAll", method = RequestMethod.POST)
    public JsonResult getAll(@RequestBody JSONObject jsonObject) {
        return ResultUtils.success(projectService.getAll(jsonObject.getString("keyword"), jsonObject.getIntValue("page"), jsonObject.getIntValue("size")));
    }

    @AuthCheck
    @RequestMapping(value = "/getAllByEmail/{page}/{size}/{status}", method = RequestMethod.GET)
    public JsonResult getAllByEmail(@JwtTokenParser("email") String email, @PathVariable int page, @PathVariable int size, @PathVariable int status) {
        return ResultUtils.success(projectService.getAllByEmail(email, page, size, status));
    }

    @AuthCheck
    @RequestMapping(value = "/getCount", method = RequestMethod.GET)
    public JsonResult getCount(@JwtTokenParser("email") String email) {
        return ResultUtils.success(projectService.getCount(email));
    }

    @AuthCheck
    @RequestMapping(value = "/getProjectInfo/{projectId}", method = RequestMethod.GET)
    public JsonResult getProjectInfo(@PathVariable String projectId) {
        return ResultUtils.success(projectService.getProjectInfo(projectId));
    }

    @AuthCheck
    @RequestMapping(value = "/addData", method = RequestMethod.POST)
    public JsonResult addData(@RequestBody JSONObject jsonObject) {
        String projectId = jsonObject.getString("projectId");
        List<Map<String, String>> list = jsonObject.getObject("list", List.class);
        projectService.addData(projectId, list);
        return ResultUtils.success();
    }

    @AuthCheck
    @RequestMapping(value = "/getData/{projectId}", method = RequestMethod.GET)
    public JsonResult getData(@PathVariable String projectId) {
        return ResultUtils.success(projectService.getData(projectId));
    }

    @AuthCheck
    @RequestMapping(value = "/delData/{projectId}/{dataListId}/{fileId}", method = RequestMethod.DELETE)
    public JsonResult delData(@PathVariable String projectId, @PathVariable String dataListId, @PathVariable String fileId) {
        projectService.delData(projectId, dataListId, fileId);
        return ResultUtils.success();
    }

    @AuthCheck
    @RequestMapping(value = "/updateLayer/{projectId}", method = RequestMethod.POST)
    public JsonResult updateLayer(@PathVariable String projectId, @RequestBody List<String> list) {
        projectService.updateLayer(projectId, list);
        return ResultUtils.success();
    }

    @AuthCheck
    @RequestMapping(value = "/getLayersInfo/{projectId}", method = RequestMethod.GET)
    public JsonResult getLayersInfo(@PathVariable String projectId) {
        return ResultUtils.success(projectService.getLayersInfo(projectId));
    }

    @AuthCheck
    @RequestMapping(value = "/updateBasemap", method = RequestMethod.PATCH)
    public JsonResult updateBasemap(@RequestBody JSONObject jsonObject) {
        projectService.updateBasemap(jsonObject.getString("projectId"), jsonObject.getString("url"));
        return ResultUtils.success();
    }

    @AuthCheck
    @RequestMapping(value = "/updatePublicState", method = RequestMethod.PATCH)
    public JsonResult updatePublicState(@RequestBody JSONObject jsonObject) {
        projectService.updatePublicState(jsonObject.getString("projectId"), jsonObject.getBoolean("state"));
        return ResultUtils.success();
    }

    @AuthCheck
    @RequestMapping(value = "/updateProjectInfo", method = RequestMethod.PATCH)
    public JsonResult updateProjectInfo(@RequestParam MultipartFile file, @RequestParam String projectName, @RequestParam String id, @RequestParam boolean isPublic) {
        return ResultUtils.success(projectService.updateProjectInfo(file, projectName, id, isPublic));
    }

    @AuthCheck
    @RequestMapping(value = "/deleteProject/{projectId}", method = RequestMethod.DELETE)
    public JsonResult deleteProject(@PathVariable String projectId, @JwtTokenParser("email") String email, @JwtTokenParser("role") String role) {
        projectService.deleteProject(projectId, email, role);
        return ResultUtils.success();
    }

    @AuthCheck
    @RequestMapping(value = "/copyProject", method = RequestMethod.POST)
    public JsonResult copyProject(@RequestParam MultipartFile file, @RequestParam String jsonString, @JwtTokenParser("email") String email) {
        JSONObject jsonObject = JSON.parseObject(jsonString);
        String projectId = jsonObject.getString("projectId");
        String creator = jsonObject.getString("creator");
        boolean isPublic = jsonObject.getBoolean("isPublic");
        String projectName = jsonObject.getString("projectName");
        return ResultUtils.success(projectService.copyProject(projectId, creator, projectName, isPublic, file, email));
    }


    /**
    * @Description:admin用户接口
    * @Author: Yiming
    * @Date: 2022/10/26
    */
    @AuthCheck
    @RequestMapping(value = "/getAllByAdmin", method = RequestMethod.POST)
    public JsonResult getAllByAdmin(@RequestBody JSONObject jsonObject, @JwtTokenParser("role") String role) {
        return ResultUtils.success(projectService.getAllByAdmin(jsonObject.getString("keyword"), jsonObject.getIntValue("page"), jsonObject.getIntValue("size"), role));
    }

}
