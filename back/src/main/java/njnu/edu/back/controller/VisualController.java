package njnu.edu.back.controller;

import njnu.edu.back.common.result.ResultUtils;
import njnu.edu.back.service.VisualService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

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

    @RequestMapping(value = "/getRaster/{visualId}/{x}/{y}/{z}", method = RequestMethod.GET)
    public void getAvatar(@PathVariable String visualId, @PathVariable int x, @PathVariable int y, @PathVariable int z, HttpServletResponse response) {
        visualService.getRaster(visualId, x, y, z, response);
    }

    /**
    * @Description:pg入库的矢量文件可视化
    * @Author: Yiming
    * @Date: 2022/8/22
    */
    
    @RequestMapping(value = "/getVectorTiles/{visualId}/{x}/{y}/{z}", method = RequestMethod.GET)
    public void getVectorTiles(@PathVariable String visualId, @PathVariable int x, @PathVariable int y, @PathVariable int z, HttpServletResponse response) {
        visualService.getVectorTiles(visualId, x, y, z, response);
    }

    /**
    * @Description:图片，照片资源的可视化
    * @Author: Yiming
    * @Date: 2022/8/22
    */
    @RequestMapping(value = "/getPhoto/{visualId}", method = RequestMethod.GET)
    public void getPhoto(@PathVariable String visualId, HttpServletResponse response) {
        visualService.getPhoto(visualId, response);
    }
}
