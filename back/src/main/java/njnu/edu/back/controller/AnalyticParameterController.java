package njnu.edu.back.controller;

import njnu.edu.back.common.result.JsonResult;
import njnu.edu.back.common.result.ResultUtils;
import njnu.edu.back.service.AnalyticParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/10/17/17:23
 * @Description:
 */
@RestController
@RequestMapping("/analyse")
public class AnalyticParameterController {
    @Autowired
    AnalyticParameterService analyticParameterService;

    @RequestMapping(value = "/findByType/{type}", method = RequestMethod.GET)
    public JsonResult findByType(@PathVariable String type) {
        return ResultUtils.success(analyticParameterService.findByType(type));
    }
}
