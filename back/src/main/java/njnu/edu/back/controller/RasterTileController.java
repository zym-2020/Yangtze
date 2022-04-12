package njnu.edu.back.controller;

import njnu.edu.back.service.RasterTileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/04/08/14:19
 * @Description:
 */
@RestController
@RequestMapping("/raster")
public class RasterTileController {

    @Autowired
    RasterTileService rasterTileService;

    @RequestMapping(value = "/getRaster/{rasterId}/{x}/{y}/{z}", method = RequestMethod.GET)
    public void getRaster(@PathVariable int rasterId, @PathVariable String x, @PathVariable String y, @PathVariable String z, HttpServletResponse response) {
        rasterTileService.getRaster(rasterId, x, y, z, response);
    }
}
