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
import njnu.edu.back.pojo.support.projectJson.ProjectJsonBean;
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
        projectService.addSection(layer, projectId, email);
        return ResultUtils.success();
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




//    @Autowired
//    ProjectService projectService;
//
//    @AuthCheck
//    @RequestMapping(value = "/addProject", method = RequestMethod.POST)
//    public JsonResult addProject(@RequestParam String projectName, @RequestParam String description, @RequestParam String result, @JwtTokenParser("email") String email, @RequestParam MultipartFile file) {
//        AddProject addProject = new AddProject();
//        addProject.setProjectName(projectName);
//        addProject.setResult(result);
//        addProject.setDescription(description);
//        return ResultUtils.success(projectService.addProject(addProject, email, file));
//    }
//
//    @AuthCheck
//    @RequestMapping(value = "/addProjectWithoutAvatar", method = RequestMethod.POST)
//    public JsonResult addProjectWithoutAvatar(@RequestBody AddProject addProject, @JwtTokenParser("email") String email) {
//        return ResultUtils.success(projectService.addProjectWithoutAvatar(addProject, email));
//    }
//
//
//    @AuthCheck
//    @RequestMapping(value = "/getResult/{id}", method = RequestMethod.GET)
//    public JsonResult getResult(@PathVariable String id) {
//        return ResultUtils.success(projectService.getResultById(id));
//    }
//
//    @AuthCheck
//    @RequestMapping(value = "/getProjectsByEmail", method = RequestMethod.GET)
//    public JsonResult getProjectsByEmail(@JwtTokenParser("email") String email) {
//        return ResultUtils.success(projectService.getProjectsByEmail(email));
//    }
//
//    @AuthCheck
//    @RequestMapping(value = "/setResult/{id}", method = RequestMethod.PATCH)
//    public JsonResult setResult(@RequestBody ProjectJsonBean result, @PathVariable String id) {
//        return ResultUtils.success(projectService.setResult(result, id));
//    }
//
//    @AuthCheck
//    @RequestMapping(value = "/section", method = RequestMethod.POST)
//    public JsonResult sectionValue(@RequestBody JSONObject jsonObject, @JwtTokenParser("email") String email) {
//        projectService.saveSectionValue(jsonObject.getStr("id"), jsonObject.getStr("DEMId"), jsonObject.getDouble("lat1"), jsonObject.getDouble("lon1"), jsonObject.getDouble("lat2"), jsonObject.getDouble("lon2"), email, jsonObject.getStr("projectId"));
//        return ResultUtils.success();
//    }
//
//    @AuthCheck
//    @RequestMapping(value = "/getSectionValue/{projectId}/{id}", method = RequestMethod.GET)
//    public JsonResult getSectionValue(@PathVariable("id") String id, @JwtTokenParser("email") String email, @PathVariable String projectId) {
//        return ResultUtils.success(projectService.getSectionValue(id, email, projectId));
//    }
//
//    @AuthCheck
//    @RequestMapping(value = "/delSection/{projectId}/{sectionId}", method = RequestMethod.DELETE)
//    public JsonResult delSection(@JwtTokenParser("email") String emial, @PathVariable String projectId, @PathVariable String sectionId) {
//        projectService.delSection(emial, projectId, sectionId);
//        return ResultUtils.success();
//    }
//
//    @AuthCheck
//    @RequestMapping(value = "/saveSectionContrastValue", method = RequestMethod.POST)
//    public JsonResult saveSectionContrastValue(@RequestBody JSONObject jsonObject, @JwtTokenParser("email") String email) {
//        projectService.saveSectionContrastValue(jsonObject.getStr("id"), jsonObject.getDouble("lat1"), jsonObject.getDouble("lon1"), jsonObject.getDouble("lat2"), jsonObject.getDouble("lon2"), jsonObject.getJSONArray("demIds").toList(String.class), email, jsonObject.getStr("projectId"));
//        return ResultUtils.success();
//    }
//
//    @AuthCheck
//    @RequestMapping(value = "/getSectionContrastValue/{projectId}/{sectionId}")
//    public JsonResult getSectionContrastValue(@PathVariable String projectId, @PathVariable String sectionId, @JwtTokenParser("email") String email) {
//        return ResultUtils.success(projectService.getSectionContrastValue(email, projectId, sectionId));
//    }
//
//    @AuthCheck
//    @RequestMapping(value = "/getProjects", method = RequestMethod.POST)
//    public JsonResult getProjects(@RequestBody JSONObject jsonObject) {
//        int size = jsonObject.getInt("size");
//        int page = jsonObject.getInt("page");
//        String keyWord = jsonObject.getStr("keyWord");
//        return ResultUtils.success(projectService.pageQuery(size, page, keyWord));
//    }
//
//    @AuthCheck
//    @RequestMapping(value = "/findProjectById/{projectId}", method = RequestMethod.GET)
//    public JsonResult findProjectById(@PathVariable String projectId) {
//        return ResultUtils.success(projectService.findProjectById(projectId));
//    }



}
