package njnu.edu.back.controller;

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

    @RequestMapping(value = "/getRaster/{x}/{y}/{z}", method = RequestMethod.GET)
    public void getRaster(@PathVariable String x, @PathVariable String y, @PathVariable String z, HttpServletResponse response) {
        int temp = ((int) Math.pow(2, Integer.parseInt(z)) - Integer.parseInt(y)) - 1;
        y = Integer.toString(temp);
        String path = "E:\\Minio\\data\\test\\tiles2\\" + z + "\\" + x + "\\" + y + ".png";
        ServletOutputStream sos = null;
        InputStream in = null;
        response.setContentType("image/png");
        try {
            File file = new File(path);
            if(file.exists()) {
                in = new FileInputStream(file);
                sos = response.getOutputStream();
                byte[] b = new byte[1024];
                while(in.read(b) != -1) {
                    sos.write(b);
                }
                sos.flush();
            }
//            else {
////                sos = response.getOutputStream();
////                sos.write(new byte[1024]);
////                sos.flush();
//                in = new FileInputStream(new File("E:\\Minio\\data\\test\\tiles1\\8\\214\\152.png"));
//                sos = response.getOutputStream();
//                byte[] b = new byte[1024];
//                while(in.read(b) != -1) {
//                    sos.write(b);
//                }
//                sos.flush();
//            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(in != null) {
                try {
                    in.close();
                    sos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
