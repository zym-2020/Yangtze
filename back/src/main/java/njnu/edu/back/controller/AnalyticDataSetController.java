package njnu.edu.back.controller;

import njnu.edu.back.common.auth.AuthCheck;
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
    @RequestMapping(value = "/findDataByType/{type}", method = RequestMethod.GET)
    public JsonResult findDataByType(@PathVariable String type) {
        return ResultUtils.success(analyticDataSetService.findDataByType(type));
    }

    @CrossOrigin
    @RequestMapping(value = "/getRaster/{rasterId}/{x}/{y}/{z}", method = RequestMethod.GET)
    public void getRaster(@PathVariable String rasterId, @PathVariable String x, @PathVariable String y, @PathVariable String z, HttpServletResponse response) {
        analyticDataSetService.getRaster(rasterId, x, y, z, response);
    }
}
