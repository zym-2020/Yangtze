package njnu.edu.back.controller;

import com.alibaba.fastjson.JSONObject;
import njnu.edu.back.common.auth.AuthCheck;
import njnu.edu.back.common.resolver.JwtTokenParser;
import njnu.edu.back.common.result.JsonResult;
import njnu.edu.back.common.result.ResultUtils;
import njnu.edu.back.service.AnalyticDataSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/06/14/21:35
 * @Description:
 */
@RestController
@RequestMapping("/analyticDataSet")
public class AnalyticDataSetController {

    @Autowired
    AnalyticDataSetService analyticDataSetService;

    @AuthCheck
    @RequestMapping(value = "/getAnalyticData/{projectId}", method = RequestMethod.GET)
    public JsonResult getAnalyticData(@PathVariable String projectId) {
        return ResultUtils.success(analyticDataSetService.getAnalyticData(projectId));
    }

    @AuthCheck
    @RequestMapping(value = "/addDraw", method = RequestMethod.POST)
    public JsonResult addDraw(@RequestBody JSONObject jsonObject, @JwtTokenParser("email") String email) {
        return ResultUtils.success(analyticDataSetService.addDraw(jsonObject, email));
    }

    @AuthCheck
    @RequestMapping(value = "/delAnalyticData/{id}", method = RequestMethod.DELETE)
    public JsonResult delAnalyticData(@PathVariable String id) {
        analyticDataSetService.delAnalyticData(id);
        return ResultUtils.success();
    }

    @AuthCheck
    @RequestMapping(value = "/checkState/{key}", method = RequestMethod.GET)
    public JsonResult checkState(@PathVariable String key) {
        return ResultUtils.success(analyticDataSetService.checkState(key));
    }

    @AuthCheck
    @RequestMapping(value = "/addSection", method = RequestMethod.POST)
    public JsonResult addSection(@RequestBody JSONObject jsonObject, @JwtTokenParser("email") String email) {
        String projectId = jsonObject.getString("projectId");
        String sectionId = jsonObject.getString("sectionId");
        String demId = jsonObject.getString("demId");
        String fileName = jsonObject.getString("fileName");
        return ResultUtils.success(analyticDataSetService.addSection(projectId, sectionId, demId, email, fileName));
    }

    @AuthCheck
    @RequestMapping(value = "/addSectionCompare", method = RequestMethod.POST)
    public JsonResult addSectionCompare(@RequestBody JSONObject jsonObject, @JwtTokenParser("email") String email) {
        String projectId = jsonObject.getString("projectId");
        String sectionId = jsonObject.getString("sectionId");
        String fileName = jsonObject.getString("fileName");
        List<String> demList = jsonObject.getObject("demList", List.class);
        return ResultUtils.success(analyticDataSetService.addSectionCompare(projectId, sectionId, email, demList, fileName));
    }

    @AuthCheck
    @RequestMapping(value = "/addSectionFlush", method = RequestMethod.POST)
    public JsonResult addSectionFlush(@RequestBody JSONObject jsonObject, @JwtTokenParser("email") String email) {
        String projectId = jsonObject.getString("projectId");
        String sectionId = jsonObject.getString("sectionId");
        String benchmarkId = jsonObject.getString("benchmarkId");
        String referId = jsonObject.getString("referId");
        String fileName = jsonObject.getString("fileName");
        return ResultUtils.success(analyticDataSetService.addSectionFlush(projectId, sectionId, benchmarkId, referId, email, fileName));
    }

    @AuthCheck
    @RequestMapping(value = "/addRegionFlush", method = RequestMethod.POST)
    public JsonResult addRegionFlush(@RequestBody JSONObject jsonObject, @JwtTokenParser("email") String email) {
        String projectId = jsonObject.getString("projectId");
        String regionId = jsonObject.getString("regionId");
        String benchmarkId = jsonObject.getString("benchmarkId");
        String referId = jsonObject.getString("referId");
        String fileName = jsonObject.getString("fileName");
        return ResultUtils.success(analyticDataSetService.addRegionFlush(projectId, regionId, benchmarkId, referId, email, fileName));
    }

    @AuthCheck
    @RequestMapping(value = "/computeVolume", method = RequestMethod.POST)
    public JsonResult computeVolume(@RequestBody JSONObject jsonObject, @JwtTokenParser("email") String email) {
        Double deep = jsonObject.getDouble("deep");
        String projectId = jsonObject.getString("projectId");
        String regionId = jsonObject.getString("regionId");
        String demId = jsonObject.getString("demId");
        String fileName = jsonObject.getString("fileName");
        return ResultUtils.success(analyticDataSetService.computeVolume(deep, projectId, regionId, demId, email, fileName));
    }

    @AuthCheck
    @RequestMapping(value = "/addElevationFlush", method = RequestMethod.POST)
    public JsonResult addElevationFlush(@RequestBody JSONObject jsonObject, @JwtTokenParser("email") String email) {
        String projectId = jsonObject.getString("projectId");
        String benchmarkId = jsonObject.getString("benchmarkId");
        String referId = jsonObject.getString("referId");
        String fileName = jsonObject.getString("fileName");
        return ResultUtils.success(analyticDataSetService.addElevationFlush(projectId, benchmarkId, referId, email, fileName));
    }

    @AuthCheck
    @RequestMapping(value = "/addFlushContour", method = RequestMethod.POST)
    public JsonResult addFlushContour(@RequestBody JSONObject jsonObject, @JwtTokenParser("email") String email) {
        String projectId = jsonObject.getString("projectId");
        String benchmarkId = jsonObject.getString("benchmarkId");
        String referId = jsonObject.getString("referId");
        String fileName = jsonObject.getString("fileName");
        return ResultUtils.success(analyticDataSetService.addFlushContour(projectId, benchmarkId, referId, email, fileName));
    }

    @AuthCheck
    @RequestMapping(value = "/addSlope", method = RequestMethod.POST)
    public JsonResult addSlope(@RequestBody JSONObject jsonObject, @JwtTokenParser("email") String email) {
        String projectId = jsonObject.getString("projectId");
        String demId = jsonObject.getString("demId");
        String fileName = jsonObject.getString("fileName");
        return ResultUtils.success(analyticDataSetService.addSlope(projectId, demId, email, fileName));
    }

    @AuthCheck
    @RequestMapping(value = "/rename", method = RequestMethod.PATCH)
    public JsonResult rename(@RequestBody JSONObject jsonObject) {
        analyticDataSetService.rename(jsonObject.getString("id"), jsonObject.getString("name"));
        return ResultUtils.success();
    }

    @AuthCheck
    @RequestMapping(value = "/getUrl/{id}", method = RequestMethod.GET)
    public JsonResult getUrl(@PathVariable String id, @JwtTokenParser("id") String userId) {
        return ResultUtils.success(analyticDataSetService.getUrl(id, userId));
    }

    @CrossOrigin
    @RequestMapping(value = "/downloadAnalyticData/{userId}/{id}", method = RequestMethod.GET)
    public void downloadAnalyticData(@PathVariable String userId, @PathVariable String id, HttpServletResponse response) {
        analyticDataSetService.downloadAnalyticData(userId, id, response);
    }
}
