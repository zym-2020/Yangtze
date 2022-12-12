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
    public JsonResult addSection(@RequestBody JSONObject jsonObject, @JwtTokenParser("email") String email) {
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
    @RequestMapping(value = "/addSection/{projectId}/{sectionId}/{demId}", method = RequestMethod.POST)
    public JsonResult addSection(@PathVariable String projectId, @PathVariable String sectionId, @PathVariable String demId, @JwtTokenParser("email") String email) {
        return ResultUtils.success(analyticDataSetService.addSection(projectId, sectionId, demId, email));
    }

    @AuthCheck
    @RequestMapping(value = "/addSectionCompare/{projectId}/{sectionId}", method = RequestMethod.POST)
    public JsonResult addSectionCompare(@PathVariable String projectId, @PathVariable String sectionId, @JwtTokenParser("email") String email, @RequestBody List<String> demList) {
        return ResultUtils.success(analyticDataSetService.addSectionCompare(projectId, sectionId, email, demList));
    }

    @AuthCheck
    @RequestMapping(value = "/addSectionFlush/{projectId}/{sectionId}/{benchmarkId}/{referId}", method = RequestMethod.POST)
    public JsonResult addSectionFlush(@PathVariable String projectId, @PathVariable String sectionId, @PathVariable String benchmarkId, @PathVariable String referId, @JwtTokenParser("email") String email) {
        return ResultUtils.success(analyticDataSetService.addSectionFlush(projectId, sectionId, benchmarkId, referId, email));
    }

    @AuthCheck
    @RequestMapping(value = "/addRegionFlush/{projectId}/{regionId}/{benchmarkId}/{referId}", method = RequestMethod.POST)
    public JsonResult addRegionFlush(@PathVariable String projectId, @PathVariable String regionId, @PathVariable String benchmarkId, @PathVariable String referId, @JwtTokenParser("email") String email) {
        return ResultUtils.success(analyticDataSetService.addRegionFlush(projectId, regionId, benchmarkId, referId, email));
    }

    @AuthCheck
    @RequestMapping(value = "/computeVolume/{projectId}/{regionId}/{demId}/{deep}", method = RequestMethod.POST)
    public JsonResult computeVolume(@PathVariable String projectId, @PathVariable String regionId, @PathVariable String demId, @PathVariable double deep, @JwtTokenParser("email") String email) {
        return ResultUtils.success(analyticDataSetService.computeVolume(deep, projectId, regionId, demId, email));
    }

    @AuthCheck
    @RequestMapping(value = "/addElevationFlush/{projectId}/{benchmarkId}/{referId}", method = RequestMethod.POST)
    public JsonResult addElevationFlush(@PathVariable String projectId, @PathVariable String benchmarkId, @PathVariable String referId, @JwtTokenParser("email") String email) {
        return ResultUtils.success(analyticDataSetService.addElevationFlush(projectId, benchmarkId, referId, email));
    }

    @AuthCheck
    @RequestMapping(value = "/addFlushContour/{projectId}/{benchmarkId}/{referId}", method = RequestMethod.POST)
    public JsonResult addFlushContour(@PathVariable String projectId, @PathVariable String benchmarkId, @PathVariable String referId, @JwtTokenParser("email") String emial) {
        return ResultUtils.success(analyticDataSetService.addFlushContour(projectId, benchmarkId, referId, emial));
    }

    @AuthCheck
    @RequestMapping(value = "/addSlope/{projectId}/{demId}", method = RequestMethod.POST)
    public JsonResult addSlope(@PathVariable String projectId, @PathVariable String demId, @JwtTokenParser("email") String email) {
        return ResultUtils.success(analyticDataSetService.addSlope(projectId, demId, email));
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
