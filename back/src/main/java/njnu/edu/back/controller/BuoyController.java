package njnu.edu.back.controller;

import njnu.edu.back.common.result.JsonResult;
import njnu.edu.back.common.result.ResultUtils;
import njnu.edu.back.service.BuoyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/11/30/20:10
 * @Description:
 */
@RestController
@RequestMapping("/buoy")
public class BuoyController {

    @Autowired
    BuoyService buoyService;

    /**
    * @Description:获取box范围内的航标
    * @Author: Yiming
    * @Date: 2022/11/30
    */
    @RequestMapping(value = "/getBuoyByBox/{top}/{right}/{bottom}/{left}", method = RequestMethod.GET)
    public JsonResult getBuoyByBox(@PathVariable double top, @PathVariable double right, @PathVariable double bottom, @PathVariable double left) {
        return ResultUtils.success(buoyService.getBuoyByBox(top, right, bottom, left));
    }
}
