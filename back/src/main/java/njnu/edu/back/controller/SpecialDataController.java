package njnu.edu.back.controller;

import njnu.edu.back.common.result.JsonResult;
import njnu.edu.back.common.result.ResultUtils;
import njnu.edu.back.service.SpecialDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/02/20/15:52
 * @Description:
 */
@RestController
@RequestMapping("/special")
public class SpecialDataController {
    @Autowired
    SpecialDataService specialDataService;

    @RequestMapping(value = "/addSpecialRecord/{id}", method = RequestMethod.POST)
    public JsonResult addSpecialRecord(@PathVariable String id) {
        specialDataService.addRecord(id);
        return ResultUtils.success();
    }

    @RequestMapping(value = "/delSpecialRecord/{id}", method = RequestMethod.DELETE)
    public JsonResult delSpecialRecord(@PathVariable String id) {
        specialDataService.delRecord(id);
        return ResultUtils.success();
    }

    @RequestMapping(value = "/getAllSpecialData", method = RequestMethod.GET)
    public JsonResult getAllSpecialData() {
        return ResultUtils.success(specialDataService.getAllSpecialData());
    }

    @RequestMapping(value = "/getIdAndDataListName/{size}/{start}", method = RequestMethod.GET)
    public JsonResult getIdAndDataListName(@PathVariable int size, @PathVariable int start) {
        return ResultUtils.success(specialDataService.getIdAndDataListName(size, start));
    }
}
