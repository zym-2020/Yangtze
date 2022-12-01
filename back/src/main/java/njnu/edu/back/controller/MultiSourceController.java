package njnu.edu.back.controller;

import njnu.edu.back.common.result.JsonResult;
import njnu.edu.back.common.result.ResultUtils;
import njnu.edu.back.service.MultiSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/12/01/10:42
 * @Description:长江网络数据处理接口
 */
@RestController
@RequestMapping("/multiSource")
public class MultiSourceController {
    @Autowired
    MultiSourceService multiSourceService;

    @CrossOrigin
    @RequestMapping(value = "/getBuoyByBox/{top}/{right}/{bottom}/{left}", method = RequestMethod.GET)
    public JsonResult getBuoyByBox(@PathVariable double top, @PathVariable double right, @PathVariable double bottom, @PathVariable double left) {
        return ResultUtils.success(multiSourceService.getBuoyByBox(top, right, bottom, left));
    }

    @CrossOrigin
    @RequestMapping(value = "/getBuoyMoveInfoByTimestamp/{startTime}/{endTime}", method = RequestMethod.GET)
    public JsonResult getBuoyMoveInfoByTimestamp(@PathVariable long startTime, @PathVariable long endTime) {
        return ResultUtils.success(multiSourceService.getBuoyMoveInfoByTimestamp(startTime, endTime));
    }

    /**
     * @Description:获取sqlite中图片资源
     * @Author: Yiming
     * @Date: 2022/12/1
     */
    @CrossOrigin
    @RequestMapping(value = "/img/{fileName}", method = RequestMethod.GET)
    public void getPhoto(@PathVariable String fileName, HttpServletResponse response) {
        multiSourceService.getPhoto(fileName, response);
    }

    /**
    * @Description:停泊区信息
    * @Author: Yiming
    * @Date: 2022/12/1
    */

    @CrossOrigin
    @RequestMapping(value = "/getParkInfoByBox/{top}/{right}/{bottom}/{left}", method = RequestMethod.GET)
    public JsonResult getParkInfoByBox(@PathVariable double top, @PathVariable double right, @PathVariable double bottom, @PathVariable double left) {
        return ResultUtils.success(multiSourceService.getParkInfoByBox(top, right, bottom, left));
    }

    /**
    * @Description:锚地信息
    * @Author: Yiming
    * @Date: 2022/12/1
    */
    @CrossOrigin
    @RequestMapping(value = "/getAnchorInfoByBox/{top}/{right}/{bottom}/{left}", method = RequestMethod.GET)
    public JsonResult getAnchorInfoByBox(@PathVariable double top, @PathVariable double right, @PathVariable double bottom, @PathVariable double left) {
        return ResultUtils.success(multiSourceService.getAnchorInfoByBox(top, right, bottom, left));
    }

    /**
    * @Description:其他信息
    * @Author: Yiming
    * @Date: 2022/12/1
    */
    @CrossOrigin
    @RequestMapping(value = "/getOtherInfoBox/{top}/{right}/{bottom}/{left}", method = RequestMethod.GET)
    public JsonResult getOtherInfoBox(@PathVariable double top, @PathVariable double right, @PathVariable double bottom, @PathVariable double left) {
        return ResultUtils.success(multiSourceService.getOtherInfoBox(top, right, bottom, left));
    }

}
