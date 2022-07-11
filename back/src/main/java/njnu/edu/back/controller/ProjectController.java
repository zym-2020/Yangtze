package njnu.edu.back.controller;

import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSONArray;
import njnu.edu.back.common.auth.AuthCheck;
import njnu.edu.back.common.resolver.JwtTokenParser;
import njnu.edu.back.common.result.JsonResult;
import njnu.edu.back.common.result.ResultUtils;
import njnu.edu.back.pojo.Project;
import njnu.edu.back.pojo.dto.AddProject;
import njnu.edu.back.pojo.support.Layer;

import njnu.edu.back.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    @RequestMapping(value = "/addProjectWithoutAvatar", method = RequestMethod.POST)
    public JsonResult addProject(@RequestBody Project project, @JwtTokenParser("email") String email) {
        return ResultUtils.success(projectService.addProject(project, email));
    }

    @RequestMapping(value = "/addProjectWithAvatar", method = RequestMethod.POST)
    public JsonResult addProject(@RequestParam String jsonString, @RequestParam MultipartFile file, @JwtTokenParser("email") String email) {
        return ResultUtils.success(projectService.addProject(jsonString, file, email));
    }

    @RequestMapping(value = "/addLayer/{projectId}", method = RequestMethod.PATCH)
    public JsonResult addLayer(@RequestBody Layer layer, @PathVariable String projectId) {
        projectService.addLayer(layer, projectId);
        return ResultUtils.success();
    }

    @RequestMapping(value = "/addLayers/{projectId}", method = RequestMethod.PATCH)
    public JsonResult addLayers(@RequestBody List<Layer> layers, @PathVariable String projectId) {
        return ResultUtils.success(projectService.addLayers(layers, projectId));
    }

    @RequestMapping(value = "/addSection/{projectId}", method = RequestMethod.POST)
    public JsonResult addSection(@RequestBody Layer layer, @PathVariable String projectId, @JwtTokenParser("email") String email) {

        return ResultUtils.success(projectService.addSection(layer, projectId, email));
    }

    @RequestMapping(value = "/getSectionValue/{projectId}/{sectionId}", method = RequestMethod.POST)
    public JsonResult getSectionValue(@PathVariable String projectId, @PathVariable String sectionId, @JwtTokenParser("email") String email, @RequestBody List<String> valueIds) {
        return ResultUtils.success(projectService.getSectionValue(sectionId, projectId, email, valueIds));
    }


    @RequestMapping(value = "/getProjectInfo/{projectId}", method = RequestMethod.GET)
    public JsonResult getProjectInfo(@PathVariable String projectId) {
        return ResultUtils.success(projectService.getProjectInfo(projectId));
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.POST)
    public JsonResult getAll(@RequestBody JSONObject jsonObject) {
        int page = jsonObject.getInt("page");
        int size = jsonObject.getInt("size");
        String keyWord = jsonObject.getStr("keyWord");
        return ResultUtils.success(projectService.getAll(page, size, keyWord));
    }

    @RequestMapping(value = "/getProjectsByEmail/{email}", method = RequestMethod.GET)
    public JsonResult getProjectsByEmail(@PathVariable String email) {
        return ResultUtils.success(projectService.getProjectsByEmail(email));
    }

    @RequestMapping(value = "/checkState/{projectId}/{sectionId}", method = RequestMethod.GET)
    public JsonResult checkState(@PathVariable String projectId, @PathVariable String sectionId) {
        return ResultUtils.success(projectService.checkState(projectId, sectionId));
    }

    @RequestMapping(value = "/delLayer/{projectId}/{layerId}", method = RequestMethod.DELETE)
    public JsonResult delLayer(@PathVariable String projectId, @PathVariable String layerId, @JwtTokenParser("email") String email) {
        projectService.delLayer(projectId, layerId, email);
        return ResultUtils.success();
    }

    @RequestMapping(value = "/getFlushId", method = RequestMethod.POST)
    public JsonResult getFlushId(@RequestBody JSONObject jsonObject) {
        String projectId = jsonObject.getStr("projectId");
        String benchmark = jsonObject.getStr("benchmark");
        String reference = jsonObject.getStr("reference");
        String name = jsonObject.getStr("name");
        return ResultUtils.success(projectService.getFlushId(projectId, benchmark, reference, name));
    }

    @RequestMapping(value = "/computeContour", method = RequestMethod.POST)
    public JsonResult computeContour(@RequestBody JSONObject jsonObject, @JwtTokenParser("email") String email) {
        String projectId = jsonObject.getStr("projectId");
        String demId = jsonObject.getStr("demId");
        String interval = jsonObject.getStr("interval");
        String shpName = jsonObject.getStr("shpName");
        return ResultUtils.success(projectService.computeContour(projectId, demId, email, interval, shpName));
    }

    @RequestMapping(value = "/checkContourState/{uid}", method = RequestMethod.GET)
    public JsonResult checkContourState(@PathVariable String uid) {
        return ResultUtils.success(projectService.checkContourState(uid));
    }






}
