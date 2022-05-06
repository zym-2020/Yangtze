package njnu.edu.back.controller;

import cn.hutool.json.JSONObject;
import njnu.edu.back.common.auth.AuthCheck;
import njnu.edu.back.common.resolver.JwtTokenParser;
import njnu.edu.back.common.result.JsonResult;
import njnu.edu.back.common.result.ResultUtils;
import njnu.edu.back.dao.ProjectMapper;
import njnu.edu.back.proj.Project;
import njnu.edu.back.proj.dto.AddProject;
import njnu.edu.back.proj.support.projectJson.ProjectJsonBean;
import njnu.edu.back.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;

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
    public JsonResult addProject(@RequestBody AddProject addProject, @JwtTokenParser("email") String email) {
        projectService.addProject(addProject, email);
        return ResultUtils.success();
    }

    @AuthCheck
    @RequestMapping(value = "/getResult/{id}", method = RequestMethod.GET)
    public JsonResult getResult(@PathVariable String id) {
        return ResultUtils.success(projectService.getResultById(id));
    }

    @AuthCheck
    @RequestMapping(value = "/getProjectId", method = RequestMethod.GET)
    public JsonResult getProjectId(@JwtTokenParser("email") String email) {
        return ResultUtils.success(projectService.getProjectId(email));
    }

    @AuthCheck
    @RequestMapping(value = "/setResult/{id}", method = RequestMethod.PATCH)
    public JsonResult setResult(@RequestBody ProjectJsonBean result, @PathVariable String id) {
        return ResultUtils.success(projectService.setResult(result, id));
    }

    @AuthCheck
    @RequestMapping(value = "/section", method = RequestMethod.POST)
    public JsonResult sectionValue(@RequestBody JSONObject jsonObject, @JwtTokenParser("email") String email) {
        projectService.saveSectionValue(jsonObject.getStr("DEMId"), jsonObject.getDouble("lat1"), jsonObject.getDouble("lon1"), jsonObject.getDouble("lat2"), jsonObject.getDouble("lon2"), jsonObject.getStr("sectionName"), email, jsonObject.getStr("projectName"));
        return ResultUtils.success();
    }

    @AuthCheck
    @RequestMapping(value = "/getSectionValue/{projectName}/{sectionName}/{DEMName}/{DEMId}", method = RequestMethod.GET)
    public JsonResult getSectionValue(@JwtTokenParser("email") String email, @PathVariable String projectName, @PathVariable String sectionName, @PathVariable String DEMName, @PathVariable String DEMId) {
        return ResultUtils.success(projectService.getSectionValue(email, projectName, sectionName, DEMName, DEMId));
    }

    @AuthCheck
    @RequestMapping(value = "/delSection/{projectName}/{sectionName}/{DEMName}", method = RequestMethod.DELETE)
    public JsonResult delSection(@JwtTokenParser("email") String emial, @PathVariable String projectName, @PathVariable String sectionName, @PathVariable String DEMName) {
        projectService.delSection(emial, projectName, sectionName, DEMName);
        return ResultUtils.success();
    }

    @AuthCheck
    @RequestMapping(value = "/saveSectionContrastValue", method = RequestMethod.POST)
    public JsonResult saveSectionContrastValue(@RequestBody JSONObject jsonObject, @JwtTokenParser("email") String email) {
        projectService.saveSectionContrastValue(jsonObject.getDouble("lat1"), jsonObject.getDouble("lon1"), jsonObject.getDouble("lat2"), jsonObject.getDouble("lon2"), jsonObject.getStr("sectionName"), email, jsonObject.getStr("projectName"));
        return ResultUtils.success();
    }

    @AuthCheck
    @RequestMapping(value = "/getSectionContrastValue/{projectName}/{sectionName}")
    public JsonResult getSectionContrastValue(@PathVariable String projectName, @PathVariable String sectionName, @JwtTokenParser("email") String email) {
        return ResultUtils.success(projectService.getSectionContrastValue(email, projectName, sectionName));
    }

    @AuthCheck
    @RequestMapping(value = "/getProjects", method = RequestMethod.GET)
    public JsonResult getProjects() {
        return ResultUtils.success(projectService.getProjects());
    }

}
