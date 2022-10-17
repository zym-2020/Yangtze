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

//    @AuthCheck
//    @RequestMapping(value = "/findDataByType/{type}", method = RequestMethod.GET)
//    public JsonResult findDataByType(@PathVariable String type) {
//        return ResultUtils.success(analyticDataSetService.findDataByType(type));
//    }
//
//    @CrossOrigin
//    @RequestMapping(value = "/getRaster/{rasterId}/{x}/{y}/{z}", method = RequestMethod.GET)
//    public void getRaster(@PathVariable String rasterId, @PathVariable String x, @PathVariable String y, @PathVariable String z, HttpServletResponse response) {
//        analyticDataSetService.getRaster(rasterId, x, y, z, response);
//    }
//
//    @CrossOrigin
//    @RequestMapping(value = "/getSlope/{rasterId}/{x}/{y}/{z}", method = RequestMethod.GET)
//    public void getSlope(@PathVariable String rasterId, @PathVariable String x, @PathVariable String y, @PathVariable String z, HttpServletResponse response) {
//        analyticDataSetService.getSlope(rasterId, x, y, z, response);
//    }
//
//    @CrossOrigin
//    @RequestMapping(value = "/{tableName}/{x}/{y}/{z}", method = RequestMethod.GET)
//    public Object getVectorTile(@PathVariable String tableName, @PathVariable int x, @PathVariable int y, @PathVariable int z) {
//        return analyticDataSetService.getVectorTile(tableName, x, y, z);
//    }

}
