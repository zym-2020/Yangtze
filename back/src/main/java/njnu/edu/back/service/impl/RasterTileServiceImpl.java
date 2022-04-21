package njnu.edu.back.service.impl;

import njnu.edu.back.dao.RasterRelationshipMapper;
import njnu.edu.back.service.RasterTileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/04/11/10:55
 * @Description:
 */
@Service
public class RasterTileServiceImpl implements RasterTileService {

    @Autowired
    RasterRelationshipMapper rasterRelationshipMapper;

    @Value("${basedir}")
    String baseDir;

    @Override
    public void getRaster(int rasterId, String x, String y, String z, HttpServletResponse response) {
        int temp = ((int) Math.pow(2, Integer.parseInt(z)) - Integer.parseInt(y)) - 1;
        y = Integer.toString(temp);
        String address = rasterRelationshipMapper.getAddress(rasterId);
        String path = address + "\\tiles\\" + z + "\\" + x + "\\" + y + ".png";
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
            } else {
                in = new FileInputStream(new File(baseDir + "123@qq.com\\upload\\raster\\color.dem\\tiles\\9\\428\\304.png"));
                sos = response.getOutputStream();
                byte[] b = new byte[1024];
                while(in.read(b) != -1) {
                    sos.write(b);
                }
                sos.flush();
            }

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
