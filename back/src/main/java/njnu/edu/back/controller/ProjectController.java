package njnu.edu.back.controller;

import njnu.edu.back.common.auth.AuthCheck;
import njnu.edu.back.common.resolver.JwtTokenParser;
import njnu.edu.back.common.result.JsonResult;
import njnu.edu.back.common.result.ResultUtils;
import njnu.edu.back.proj.dto.AddProject;
import njnu.edu.back.proj.support.projectJson.ProjectJsonBean;
import njnu.edu.back.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public JsonResult getResult(@PathVariable int id) {
        return ResultUtils.success(projectService.getResultById(id));
    }

    @AuthCheck
    @RequestMapping(value = "/getProjectId", method = RequestMethod.GET)
    public JsonResult getProjectId(@JwtTokenParser("email") String email) {
        return ResultUtils.success(projectService.getProjectId(email));
    }

    @AuthCheck
    @RequestMapping(value = "/setResult/{id}", method = RequestMethod.PATCH)
    public JsonResult setResult(@RequestBody ProjectJsonBean result, @PathVariable int id) {
        return ResultUtils.success(projectService.setResult(result, id));
    }
}
