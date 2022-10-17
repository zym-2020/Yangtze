package njnu.edu.back.service.impl;

import com.alibaba.fastjson.JSONObject;
import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.result.ResultEnum;
import njnu.edu.back.common.utils.TileUtil;
import njnu.edu.back.dao.main.AnalyticDataSetMapper;
import njnu.edu.back.pojo.support.TileBox;
import njnu.edu.back.service.AnalyticDataSetService;
import njnu.edu.back.dao.shp.VectorTileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

    @Value("${basePath}")
    String basePath;

    @Value("${visualAddress}")
    String visualAddress;

    @Override
    public List<Map<String, Object>> getAnalyticData(String projectId) {
        return analyticDataSetMapper.getAnalyticData(projectId);
    }

    @Override
    public String addDraw(JSONObject jsonObject, String email) {
        String projectId = jsonObject.getString("projectId");
        String fileName = jsonObject.getString("fileName");
        String visualType = jsonObject.getString("visualType");
        String jsonString = jsonObject.getJSONObject("geoJson").toJSONString();
        String path = basePath + email + "\\project\\" + projectId;
        String visualPath = visualAddress + "geoJson";
        String uuid = UUID.randomUUID().toString();
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter(path + "\\" + uuid + ".json");
            bw = new BufferedWriter(fw);
            bw.write(jsonString);
            bw.flush();
            fw.close();
            bw.close();
            fw = new FileWriter(visualPath + "\\" + uuid + ".json");
            bw = new BufferedWriter(fw);
            bw.write(jsonString);
            bw.flush();
            fw.close();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        }
        return analyticDataSetMapper.addDraw(uuid, fileName, uuid + ".json", email, visualType, "", projectId);
    }

    @Override
    public void delAnalyticData(String id) {
        analyticDataSetMapper.delAnalyticData(id);
    }

    //    @Override
//    public List<Map<String, Object>> findDataByType(String type) {
//        List<String> types = new ArrayList<>();
//        if(type.equals("riverBed")) {
//            types.add("riverBed");
//            types.add("deepHorizonLine");
//            return analyticDataSetMapper.findDataByType(types);
//        } else if(type.equals("satellite")) {
//            types.add("satellite");
//            return analyticDataSetMapper.findDataByType(types);
//        } else {
//            types.add("hydrology");
//            return analyticDataSetMapper.findDataByType(types);
//        }
//    }
//
//    @Override
//    public void getRaster(String id, String x, String y, String z, HttpServletResponse response) {
//        int temp = ((int) Math.pow(2, Integer.parseInt(z)) - Integer.parseInt(y)) - 1;
//        y = Integer.toString(temp);
//        String address = (String) analyticDataSetMapper.findById(id).get("address");
//        String path = address + "\\tiles\\" + z + "\\" + x + "\\" + y + ".png";
//        ServletOutputStream sos = null;
//        InputStream in = null;
//        response.setContentType("image/png");
//        try {
//            File file = new File(path);
//            if(file.exists()) {
//                in = new FileInputStream(file);
//                sos = response.getOutputStream();
//                byte[] b = new byte[1024];
//                int len;
//                while((len = in.read(b)) != -1) {
//                    sos.write(b, 0, len);
//                }
//            } else {
//                in = new FileInputStream(new File(baseDir + "123@qq.com\\upload\\raster\\color.dem\\tiles\\9\\428\\304.png"));
//                sos = response.getOutputStream();
//                byte[] b = new byte[1024];
//                int len;
//                while((len = in.read(b)) != -1) {
//                    sos.write(b, 0, len);
//                }
//                sos.flush();
//            }
//
//        } catch (IOException e) {
////            e.printStackTrace();
//        } finally {
//            if(in != null) {
//                try {
//                    in.close();
////                    sos.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }
//    }
//
//    @Override
//    public void getSlope(String rasterId, String x, String y, String z, HttpServletResponse response) {
//        int temp = ((int) Math.pow(2, Integer.parseInt(z)) - Integer.parseInt(y)) - 1;
//        y = Integer.toString(temp);
//        String address = (String) analyticDataSetMapper.findById(rasterId).get("address");
//        String path = address + "\\slope\\" + z + "\\" + x + "\\" + y + ".png";
//        ServletOutputStream sos = null;
//        InputStream in = null;
//        response.setContentType("image/png");
//        try {
//            File file = new File(path);
//            if(file.exists()) {
//                in = new FileInputStream(file);
//                sos = response.getOutputStream();
//                byte[] b = new byte[1024];
//                int len;
//                while((len = in.read(b)) != -1) {
//                    sos.write(b, 0, len);
//                }
//            } else {
//                in = new FileInputStream(new File(baseDir + "123@qq.com\\upload\\raster\\color.dem\\tiles\\9\\428\\304.png"));
//                sos = response.getOutputStream();
//                byte[] b = new byte[1024];
//                int len;
//                while((len = in.read(b)) != -1) {
//                    sos.write(b, 0, len);
//                }
//                sos.flush();
//                sos.close();
//            }
//
//        } catch (IOException e) {
////            e.printStackTrace();
//        } finally {
//            if(in != null) {
//                try {
//                    in.close();
////                    sos.close();
//                } catch (Exception e) {
////                    e.printStackTrace();
//                }
//            }
//
//        }
//    }
//
//    @Override
//    public Object getVectorTile(String tableName, int x, int y, int z) {
//        TileBox tileBox = TileUtil.tile2boundingBox(x, y, z, tableName);
//        Object result = vectorTileMapper.getVictorTile(tileBox);
//        return result;
//    }
}
