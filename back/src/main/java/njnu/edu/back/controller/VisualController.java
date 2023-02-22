package njnu.edu.back.controller;

import com.alibaba.fastjson.JSONObject;
import njnu.edu.back.common.result.JsonResult;
import njnu.edu.back.common.result.ResultUtils;
import njnu.edu.back.pojo.VisualFile;
import njnu.edu.back.service.VisualService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @CrossOrigin
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
    public void getRaster(@PathVariable String visualId, @PathVariable int x, @PathVariable int y, @PathVariable int z, HttpServletResponse response) {
        visualService.getRaster(visualId, x, y, z, response);
    }

    /**
    * @Description:海图栅格可视化
    * @Author: Yiming
    * @Date: 2023/2/20
    */
    @CrossOrigin
    @RequestMapping(value = "/seaChart/{x}/{y}/{z}", method = RequestMethod.GET)
    public void seaChart(@PathVariable String x, @PathVariable String y, @PathVariable String z, HttpServletResponse response) {
        visualService.seaChart(x, y, z, response);
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
    @CrossOrigin
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
    @CrossOrigin
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
    @CrossOrigin
    @RequestMapping(value = "/getSuspension/{visualId}", method = RequestMethod.GET)
    public JsonResult getSuspension(@PathVariable String visualId) {
        return ResultUtils.success(visualService.getSuspension(visualId));
    }

    /**
    * @Description:geoJson数据
    * @Author: Yiming
    * @Date: 2022/10/14
    */
    @CrossOrigin
    @RequestMapping(value = "/getGeoJson/{fileId}", method = RequestMethod.GET)
    public JsonResult getGeoJson(@PathVariable String fileId) {
        return ResultUtils.success(visualService.getGeoJson(fileId));
    }

    @RequestMapping(value = "/getAnalyticGeoJson/{fileId}", method = RequestMethod.GET)
    public JsonResult getAnalyticGeoJson(@PathVariable String fileId) {
        return ResultUtils.success(visualService.getAnalyticGeoJson(fileId));
    }

    @RequestMapping(value = "/getSection/{fileId}", method = RequestMethod.GET)
    public JsonResult getSection(@PathVariable String fileId) {
        return ResultUtils.success(visualService.getSection(fileId));
    }

    @RequestMapping(value = "/getSectionContrast/{fileId}", method = RequestMethod.GET)
    public JsonResult getSectionContrast(@PathVariable String fileId) {
        return ResultUtils.success(visualService.getSectionContrast(fileId));
    }

    @RequestMapping(value = "/getSectionFlush/{fileId}", method = RequestMethod.GET)
    public JsonResult getSectionFlush(@PathVariable String fileId) {
        return ResultUtils.success(visualService.getSectionFlush(fileId));
    }

    @RequestMapping(value = "/getVolume/{fileId}", method = RequestMethod.GET)
    public JsonResult getVolume(@PathVariable String fileId) {
        return ResultUtils.success(visualService.getVolume(fileId));
    }

    @CrossOrigin
    @RequestMapping(value = "/getTianDiTu", method = RequestMethod.GET)
    public JSONObject getTianDiTu() {
        return visualService.getTianDiTu();
    }

    @CrossOrigin
    @RequestMapping(value = "/getTianDiTuImage", method = RequestMethod.GET)
    public JSONObject getTianDiTuImage() {
        return visualService.getTianDiTuImage();
    }


















    /**
    * @Description:可视化衍生数据上传——分片上传
    * @Author: Yiming
    * @Date: 2023/1/13
    */
    @RequestMapping(value = "/uploadParts/{uid}/{number}", method = RequestMethod.POST)
    public JsonResult uploadParts(@PathVariable String uid, @PathVariable String number, @RequestParam MultipartFile file) {
        visualService.uploadParts(uid, number, file);
        return ResultUtils.success();
    }
    /**
    * @Description: 可视化衍生数据上传——分片合并
    * @Author: Yiming
    * @Date: 2023/1/13
    */
    @RequestMapping(value = "/mergeParts/{uid}/{total}/{type}/{name}", method = RequestMethod.POST)
    public JsonResult mergeParts(@PathVariable String uid, @PathVariable Integer total, @PathVariable String type, @PathVariable String name) {
        return ResultUtils.success(visualService.mergeParts(uid, total, type, name));
    }

}
