package njnu.edu.back.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import njnu.edu.back.common.result.JsonResult;
import njnu.edu.back.common.result.ResultUtils;
import njnu.edu.back.service.MultiSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

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
    public List<Map<String, Object>> getBuoyByBox(@PathVariable double top, @PathVariable double right, @PathVariable double bottom, @PathVariable double left) {
        return multiSourceService.getBuoyByBox(top, right, bottom, left);
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
    public List<Map<String, Object>> getParkInfoByBox(@PathVariable double top, @PathVariable double right, @PathVariable double bottom, @PathVariable double left) {
        return multiSourceService.getParkInfoByBox(top, right, bottom, left);
    }

    /**
    * @Description:锚地信息
    * @Author: Yiming
    * @Date: 2022/12/1
    */
    @CrossOrigin
    @RequestMapping(value = "/getAnchorInfoByBox/{top}/{right}/{bottom}/{left}", method = RequestMethod.GET)
    public List<Map<String, Object>> getAnchorInfoByBox(@PathVariable double top, @PathVariable double right, @PathVariable double bottom, @PathVariable double left) {
        return multiSourceService.getAnchorInfoByBox(top, right, bottom, left);
    }

    /**
    * @Description:其他信息
    * @Author: Yiming
    * @Date: 2022/12/1
    */
    @CrossOrigin
    @RequestMapping(value = "/getOtherInfoBox/{top}/{right}/{bottom}/{left}", method = RequestMethod.GET)
    public List<Map<String, Object>> getOtherInfoBox(@PathVariable double top, @PathVariable double right, @PathVariable double bottom, @PathVariable double left) {
        return multiSourceService.getOtherInfoBox(top, right, bottom, left);
    }

    @CrossOrigin
    @RequestMapping(value = "/getMeteorologyBox/{top}/{right}/{bottom}/{left}", method = RequestMethod.GET)
    public JSONArray getMeteorologyBox(@PathVariable double top, @PathVariable double right, @PathVariable double bottom, @PathVariable double left) {
        return multiSourceService.getMeteorologyBox(top, right, bottom, left);
    }

    @CrossOrigin
    @RequestMapping(value = "/getStationBox/{top}/{right}/{bottom}/{left}", method = RequestMethod.GET)
    public List<Map<String, Object>> getStationBox(@PathVariable double top, @PathVariable double right, @PathVariable double bottom, @PathVariable double left) {
        return multiSourceService.getStationBox(top, right, bottom, left);
    }

    @CrossOrigin
    @RequestMapping(value = "/getWeatherInfoById/{id}", method = RequestMethod.GET)
    public JsonResult getWeatherInfoById(@PathVariable String id) {
        return ResultUtils.success(multiSourceService.getWeatherInfoById(id));
    }

}
