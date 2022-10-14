package njnu.edu.back.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
    public JsonResult addProject(@RequestParam String projectName, @RequestParam MultipartFile file, @JwtTokenParser("email") String email) {
        return ResultUtils.success(projectService.addProject(projectName, file, email));
    }

    @AuthCheck
    @RequestMapping(value = "/getAll", method = RequestMethod.POST)
    public JsonResult getAll(@RequestBody JSONObject jsonObject) {
        return ResultUtils.success(projectService.getAll(jsonObject.getString("keyword"), jsonObject.getIntValue("page"), jsonObject.getIntValue("size")));
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


//    @AuthCheck
//    @RequestMapping(value = "/addLayer/{projectId}", method = RequestMethod.PATCH)
//    public JsonResult addLayer(@RequestBody Layer layer, @PathVariable String projectId) {
//        projectService.addLayer(layer, projectId);
//        return ResultUtils.success();
//    }
//
//    @AuthCheck
//    @RequestMapping(value = "/addLayers/{projectId}", method = RequestMethod.PATCH)
//    public JsonResult addLayers(@RequestBody List<Layer> layers, @PathVariable String projectId) {
//        return ResultUtils.success(projectService.addLayers(layers, projectId));
//    }
//
//    @AuthCheck
//    @RequestMapping(value = "/addSection/{projectId}", method = RequestMethod.POST)
//    public JsonResult addSection(@RequestBody Layer layer, @PathVariable String projectId, @JwtTokenParser("email") String email) {
//
//        return ResultUtils.success(projectService.addSection(layer, projectId, email));
//    }
//
//    @AuthCheck
//    @RequestMapping(value = "/getSectionValue/{projectId}/{sectionId}", method = RequestMethod.POST)
//    public JsonResult getSectionValue(@PathVariable String projectId, @PathVariable String sectionId, @JwtTokenParser("email") String email, @RequestBody List<String> valueIds) {
//        return ResultUtils.success(projectService.getSectionValue(sectionId, projectId, email, valueIds));
//    }
//
//
//    @AuthCheck
//    @RequestMapping(value = "/getProjectInfo/{projectId}", method = RequestMethod.GET)
//    public JsonResult getProjectInfo(@PathVariable String projectId) {
//        return ResultUtils.success(projectService.getProjectInfo(projectId));
//    }
//
//    @AuthCheck
//    @RequestMapping(value = "/getAll", method = RequestMethod.POST)
//    public JsonResult getAll(@RequestBody JSONObject jsonObject) {
//        int page = jsonObject.getInt("page");
//        int size = jsonObject.getInt("size");
//        String keyWord = jsonObject.getStr("keyWord");
//        return ResultUtils.success(projectService.getAll(page, size, keyWord));
//    }
//
//    @AuthCheck
//    @RequestMapping(value = "/getProjectsByEmail/{email}", method = RequestMethod.GET)
//    public JsonResult getProjectsByEmail(@PathVariable String email) {
//        return ResultUtils.success(projectService.getProjectsByEmail(email));
//    }
//
//    @AuthCheck
//    @RequestMapping(value = "/checkState/{projectId}/{sectionId}", method = RequestMethod.GET)
//    public JsonResult checkState(@PathVariable String projectId, @PathVariable String sectionId) {
//        return ResultUtils.success(projectService.checkState(projectId, sectionId));
//    }
//
//    @AuthCheck
//    @RequestMapping(value = "/delLayer/{projectId}/{layerId}", method = RequestMethod.DELETE)
//    public JsonResult delLayer(@PathVariable String projectId, @PathVariable String layerId, @JwtTokenParser("email") String email) {
//        projectService.delLayer(projectId, layerId, email);
//        return ResultUtils.success();
//    }
//
//    @AuthCheck
//    @RequestMapping(value = "/getFlushId", method = RequestMethod.POST)
//    public JsonResult getFlushId(@RequestBody JSONObject jsonObject) {
//        String projectId = jsonObject.getStr("projectId");
//        String benchmark = jsonObject.getStr("benchmark");
//        String reference = jsonObject.getStr("reference");
//        String name = jsonObject.getStr("name");
//        return ResultUtils.success(projectService.getFlushId(projectId, benchmark, reference, name));
//    }
//
//    @AuthCheck
//    @RequestMapping(value = "/computeContour", method = RequestMethod.POST)
//    public JsonResult computeContour(@RequestBody JSONObject jsonObject, @JwtTokenParser("email") String email) {
//        String projectId = jsonObject.getStr("projectId");
//        String demId = jsonObject.getStr("demId");
//        String interval = jsonObject.getStr("interval");
//        String shpName = jsonObject.getStr("shpName");
//        String srid = jsonObject.getStr("srid");
//        return ResultUtils.success(projectService.computeContour(projectId, demId, email, interval, shpName, srid));
//    }
//
//    @AuthCheck
//    @RequestMapping(value = "/checkContourState/{uid}", method = RequestMethod.GET)
//    public JsonResult checkContourState(@PathVariable String uid) {
//        return ResultUtils.success(projectService.checkContourState(uid));
//    }
//
//    @AuthCheck
//    @RequestMapping(value = "/sortLayer/{projectId}", method = RequestMethod.POST)
//    public JsonResult sortLayer(@PathVariable String projectId, @RequestBody List<Layer> layers) {
//        projectService.sortLayer(projectId, layers);
//        return ResultUtils.success();
//    }
//
//    @AuthCheck
//    @RequestMapping(value = "/addRegion/{projectId}/{demId}", method = RequestMethod.POST)
//    public JsonResult addRegion(@RequestBody JSONArray jsonArray, @PathVariable String projectId, @PathVariable String demId, @JwtTokenParser("email") String email) {
//        return ResultUtils.success(projectService.addRegion(projectId, jsonArray, demId, email));
//    }
//
//    @AuthCheck
//    @RequestMapping(value = "/checkAddRegion/{key}", method = RequestMethod.GET)
//    public JsonResult checkAddRegion(@PathVariable String key) {
//        return ResultUtils.success(projectService.checkAddRegion(key));
//    }
//
//    @AuthCheck
//    @RequestMapping(value = "/getRegionLayer/{projectId}/{layerId}", method = RequestMethod.GET)
//    public JsonResult getRegionLayer(@PathVariable String projectId, @PathVariable String layerId) {
//        return ResultUtils.success(projectService.getRegionLayer(projectId, layerId));
//    }
//
//    @CrossOrigin
//    @RequestMapping(value = "/getRegion/{email}/{projectId}/{layerId}", method = RequestMethod.GET)
//    public JsonResult getRegion(@PathVariable String projectId, @PathVariable String layerId, @PathVariable String email, HttpServletResponse response) {
//        projectService.getRegion(projectId, layerId, email, response);
//        return ResultUtils.success();
//    }


}
