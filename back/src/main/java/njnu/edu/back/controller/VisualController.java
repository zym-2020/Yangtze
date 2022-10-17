package njnu.edu.back.controller;

import com.alibaba.fastjson.JSONObject;
import njnu.edu.back.common.result.JsonResult;
import njnu.edu.back.common.result.ResultUtils;
import njnu.edu.back.pojo.VisualFile;
import njnu.edu.back.service.VisualService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/08/22/14:29
 * @Description:
 */
@RestController
@RequestMapping("/visual")
public class VisualController {
    @Autowired
    VisualService visualService;

    /**
    * @Description:获取用户头像，项目头像，缩略图等
    * @Author: Yiming
    * @Date: 2022/8/22
    */

    @RequestMapping(value = "/getAvatar/{fileName}", method = RequestMethod.GET)
    public void getAvatar(@PathVariable String fileName, HttpServletResponse response) {
        visualService.getAvatar(fileName, response);
    }

    /**
    * @Description:已切片栅格可视化
    * @Author: Yiming
    * @Date: 2022/8/22
    */

    @CrossOrigin
    @RequestMapping(value = "/getRaster/{visualId}/{x}/{y}/{z}", method = RequestMethod.GET)
    public void getAvatar(@PathVariable String visualId, @PathVariable int x, @PathVariable int y, @PathVariable int z, HttpServletResponse response) {
        visualService.getRaster(visualId, x, y, z, response);
    }

    /**
    * @Description:pg入库的矢量文件可视化
    * @Author: Yiming
    * @Date: 2022/8/22
    */

    @CrossOrigin
    @RequestMapping(value = "/getVectorTiles/{visualId}/{x}/{y}/{z}", method = RequestMethod.GET)
    public void getVectorTiles(@PathVariable String visualId, @PathVariable int x, @PathVariable int y, @PathVariable int z, HttpServletResponse response) {
        visualService.getVectorTiles(visualId, x, y, z, response);
    }

    /**
    * @Description:图片，照片资源的可视化
    * @Author: Yiming
    * @Date: 2022/8/22
    */
    @RequestMapping(value = "/getPhoto/{fileId}", method = RequestMethod.GET)
    public void getPhoto(@PathVariable String fileId, HttpServletResponse response) {
        visualService.getPhoto(fileId, response);
    }

    /**
    * @Description:地图png可视化，包括两部分，获取坐标、获取png资源
    * @Author: Yiming
    * @Date: 2022/8/23
    */
    @CrossOrigin
    @RequestMapping(value = "/getCoordinates/{visualId}", method = RequestMethod.GET)
    public JsonResult getCoordinates(@PathVariable String visualId) {
        return ResultUtils.success(visualService.getCoordinates(visualId));
    }

    @CrossOrigin
    @RequestMapping(value = "/getPngResource/{visualId}", method = RequestMethod.GET)
    public void getPngResource(@PathVariable String visualId, HttpServletResponse response) {
        visualService.getPngResource(visualId, response);
    }

    /**
    * @Description:潮位站excel数据可视化
    * @Author: Yiming
    * @Date: 2022/9/1
    */
    @RequestMapping(value = "/getTide/{visualId}", method = RequestMethod.GET)
    public JsonResult getTide(@PathVariable String visualId) {
        return ResultUtils.success(visualService.getTide(visualId));
    }

    /**
    * @Description:流速流向excel数据可视化
    * @Author: Yiming
    * @Date: 2022/9/6
    */
    @CrossOrigin
    @RequestMapping(value = "/getRateDirection/{visualId}", method = RequestMethod.GET)
    public JsonResult getRateDirection(@PathVariable String visualId) {
        return ResultUtils.success(visualService.getRateDirection(visualId));
    }

    /**
    * @Description:含沙量excel数据可视化
    * @Author: Yiming
    * @Date: 2022/9/6
    */
    @CrossOrigin
    @RequestMapping(value = "/getSandContent/{visualId}", method = RequestMethod.GET)
    public JsonResult getSandContent(@PathVariable String visualId) {
        return ResultUtils.success(visualService.getSandContent(visualId));
    }

    /**
    * @Description:流量输沙率flowSand_Z类型excel数据可视化
    * @Author: Yiming
    * @Date: 2022/9/7
    */
    @CrossOrigin
    @RequestMapping(value = "/getFlowSand_Z/{visualId}", method = RequestMethod.GET)
    public JsonResult getFlowSand_Z(@PathVariable String visualId) {
        return ResultUtils.success(visualService.getFlowSand_Z(visualId));
    }

    /**
    * @Description:含盐度salinity类型excel数据可视化
    * @Author: Yiming
    * @Date: 2022/9/7
    */
    @CrossOrigin
    @RequestMapping(value = "/getSalinity/{visualId}", method = RequestMethod.GET)
    public JsonResult getSalinity(@PathVariable String visualId) {
        return ResultUtils.success(visualService.getSalinity(visualId));
    }

    /**
    * @Description:悬移质suspension类型excel数据可视化
    * @Author: Yiming
    * @Date: 2022/9/9
    */

    @RequestMapping(value = "/getSuspension/{visualId}", method = RequestMethod.GET)
    public JsonResult getSuspension(@PathVariable String visualId) {
        return ResultUtils.success(visualService.getSuspension(visualId));
    }

    /**
    * @Description:geoJson数据
    * @Author: Yiming
    * @Date: 2022/10/14
    */
    @RequestMapping(value = "/getGeoJson/{fileId}", method = RequestMethod.GET)
    public JsonResult getGeoJson(@PathVariable String fileId) {
        return ResultUtils.success(visualService.getGeoJson(fileId));
    }



















    /**
    * @Description:方便离线插入数据
    * @Author: Yiming
    * @Date: 2022/8/24
    */

    @RequestMapping(value = "/addVisualFile", method = RequestMethod.POST)
    public JsonResult addVisualFile(@RequestBody VisualFile visualFile) {
        visualService.addVisualFile(visualFile);
        return ResultUtils.success();
    }

    /**
    * @Description:方便插入可视化json与excel同名的数据数据
    * @Author: Yiming
    * @Date: 2022/9/6
    */
    @RequestMapping(value = "/addSameNameVisualFile", method = RequestMethod.POST)
    public JsonResult addRateDirection(@RequestBody JSONObject jsonObject) {
        visualService.addSameNameVisualFile(jsonObject.getString("type"), jsonObject.getString("address"));
        return ResultUtils.success();
    }
}
