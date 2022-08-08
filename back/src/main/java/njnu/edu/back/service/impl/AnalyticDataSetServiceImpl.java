package njnu.edu.back.service.impl;

import njnu.edu.back.dao.AnalyticDataSetMapper;
import njnu.edu.back.service.AnalyticDataSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/06/14/21:33
 * @Description:
 */
@Service
public class AnalyticDataSetServiceImpl implements AnalyticDataSetService {
    @Autowired
    AnalyticDataSetMapper analyticDataSetMapper;

    @Value("${basedir}")
    String baseDir;

    @Override
    public List<Map<String, Object>> findDataByType(String type) {
        return analyticDataSetMapper.findDataByType(type);
    }

    @Override
    public void getRaster(String id, String x, String y, String z, HttpServletResponse response) {
        int temp = ((int) Math.pow(2, Integer.parseInt(z)) - Integer.parseInt(y)) - 1;
        y = Integer.toString(temp);
        String address = (String) analyticDataSetMapper.findById(id).get("address");
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
            } else {
                in = new FileInputStream(new File(baseDir + "123@qq.com\\upload\\raster\\color.dem\\tiles\\9\\428\\304.png"));
                sos = response.getOutputStream();
                byte[] b = new byte[1024];
                while(in.read(b) != -1) {
                    sos.write(b);
                }
                sos.flush();
            }

        } catch (IOException e) {
//            e.printStackTrace();
        } finally {
            if(in != null) {
                try {
                    in.close();
//                    sos.close();                  //此处不需要关闭这个io流，springboot开启的io流不需要手动关闭
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
