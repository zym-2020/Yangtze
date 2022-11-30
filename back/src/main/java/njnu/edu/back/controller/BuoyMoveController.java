package njnu.edu.back.controller;

import njnu.edu.back.common.result.JsonResult;
import njnu.edu.back.common.result.ResultUtils;
import njnu.edu.back.service.BuoyMoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/11/30/20:41
 * @Description:
 */
@RestController
@RequestMapping("/buoyMove")
public class BuoyMoveController {
    @Autowired
    BuoyMoveService buoyMoveService;

    @RequestMapping(value = "/getInfoByTimestamp/{startTime}/{endTime}", method = RequestMethod.GET)
    public JsonResult getInfoByTimestamp(@PathVariable long startTime, @PathVariable long endTime) {
        return ResultUtils.success(buoyMoveService.getInfoByTimestamp(startTime, endTime));
    }

}
