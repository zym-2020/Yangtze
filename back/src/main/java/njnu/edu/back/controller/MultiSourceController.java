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

    @RequestMapping(value = "/getMeteorology", method = RequestMethod.GET)
    public JsonResult getMeteorology() {
        return ResultUtils.success(multiSourceService.getMeteorology());
    }

    /**
    * @Description:获取meteorology图片
    * @Author: Yiming
    * @Date: 2023/2/23
    */
    @CrossOrigin
    @RequestMapping(value = "/getMeteorologyPng/{fileName}", method = RequestMethod.GET)
    public void getMeteorologyPng(@PathVariable String fileName, HttpServletResponse response) {
        multiSourceService.getMeteorologyPng(fileName, response);
    }


    /**
    * @Description:获取区域船只信息
    * @Author: Yiming
    * @Date: 2022/12/6
    */
    @CrossOrigin
    @RequestMapping(value = "/getShipInfoByBox/{top}/{right}/{bottom}/{left}", method = RequestMethod.GET)
    public List<Map<String, Object>> getShipInfoByBox(@PathVariable double top, @PathVariable double right, @PathVariable double bottom, @PathVariable double left) {
        return multiSourceService.getShipInfoByBox(top, right, bottom, left);
    }

    @CrossOrigin
    @RequestMapping(value = "/getShipInfoByMMSI/{mmsi}", method = RequestMethod.GET)
    public JsonResult getShipInfoByMMSI(@PathVariable String mmsi) {
        return ResultUtils.success(multiSourceService.getShipInfoByMMSI(mmsi));
    }

    @CrossOrigin
    @RequestMapping(value = "/record/{mmsi}", method = RequestMethod.GET)
    public List<Map<String, Object>> record (@PathVariable String mmsi) {
        return multiSourceService.record(mmsi);
    }

    /**
    * @Description:作为临时接口，做出船的动态效果
    * @Author: Yiming
    * @Date: 2022/12/7
    */
    @CrossOrigin
    @RequestMapping(value = "/getShipInfoByBoxAndTime/{top}/{right}/{bottom}/{left}/{startTime}/{endTime}", method = RequestMethod.GET)
    public List<Map<String, Object>> getShipInfoByBoxAndTime(@PathVariable double top, @PathVariable double right, @PathVariable double bottom, @PathVariable double left, @PathVariable String startTime, @PathVariable String endTime) {
        return multiSourceService.getShipInfoByBoxAndTime(top, right, bottom, left, startTime, endTime);
    }


    /**
    * @Description:桥梁信息
    * @Author: Yiming
    * @Date: 2023/2/22
    */
    @RequestMapping(value = "/getBridgeInfo", method = RequestMethod.GET)
    public JsonResult getBridgeInfo() {
        return ResultUtils.success(multiSourceService.getBridgeInfo());
    }

    /**
     * @Description:海图栅格可视化
     * @Author: Yiming
     * @Date: 2023/2/20
     */
    @CrossOrigin
    @RequestMapping(value = "/seaChart/{type}/{x}/{y}/{z}", method = RequestMethod.GET)
    public void seaChart(@PathVariable String type, @PathVariable String x, @PathVariable String y, @PathVariable String z, HttpServletResponse response) {
        multiSourceService.seaChart(type, x, y, z, response);
    }

    @RequestMapping(value = "/getStationByBox/{top}/{right}/{bottom}/{left}", method = RequestMethod.GET)
    public JsonResult getStationByBox(@PathVariable Double top, @PathVariable Double right, @PathVariable Double bottom, @PathVariable Double left) {
        return ResultUtils.success(multiSourceService.getStationByBox(top, right, bottom, left));
    }

    @RequestMapping(value = "/getWaterLevelByStationAndTime/{type}/{station}/{startTime}/{endTime}", method = RequestMethod.GET)
    public JsonResult getWaterLevelByStationAndTime(@PathVariable String type, @PathVariable String station, @PathVariable String startTime, @PathVariable String endTime) {
        return ResultUtils.success(multiSourceService.getWaterLevelByStationAndTime(type, station, startTime, endTime));
    }

    /**
    * @Description:查询各类图层的list
    * @Author: Yiming
    * @Date: 2023/2/27
    */
    @RequestMapping(value = "/pageList", method = RequestMethod.GET)
    public JsonResult pageList(@RequestParam String type, @RequestParam int page, @RequestParam int size, @RequestParam String keyword) {
        return ResultUtils.success(multiSourceService.pageList(type, page, size, keyword));
    }

}
