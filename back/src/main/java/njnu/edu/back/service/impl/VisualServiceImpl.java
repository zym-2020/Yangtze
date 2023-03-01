package njnu.edu.back.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.result.ResultEnum;
import njnu.edu.back.common.utils.FileUtil;
import njnu.edu.back.common.utils.LocalUploadUtil;
import njnu.edu.back.common.utils.TileUtil;
import njnu.edu.back.dao.main.AnalyticDataSetMapper;
import njnu.edu.back.dao.main.FileMapper;
import njnu.edu.back.dao.main.VisualFileMapper;
import njnu.edu.back.dao.shp.VectorTileMapper;
import njnu.edu.back.dao.staticdb.PhotoMapper;
import njnu.edu.back.pojo.VisualFile;
import njnu.edu.back.pojo.support.TileBox;
import njnu.edu.back.service.VisualService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/08/22/14:31
 * @Description:
 */
@Repository
public class VisualServiceImpl implements VisualService {

    @Value("${pictureAddress}")
    String pictureAddress;

    @Value("${visualAddress}")
    String visualAddress;

    @Value("${basePath}")
    String basePath;

    @Value("${mapAddress}")
    String mapAddress;

    @Value("${tempAddress}")
    String tempAddress;

    @Autowired
    VisualFileMapper visualFileMapper;

    @Autowired
    VectorTileMapper vectorTileMapper;

    @Autowired
    FileMapper fileMapper;

    @Autowired
    AnalyticDataSetMapper analyticDataSetMapper;


    @Override
    public void getAvatar(String fileName, HttpServletResponse response) {
        InputStream in = null;
        ServletOutputStream sos = null;
        try {
            in = new FileInputStream(pictureAddress + fileName);
            sos = response.getOutputStream();
            byte[] bytes = new byte[1024];
            int len;
            while ((len = in.read(bytes)) > -1) {
                sos.write(bytes, 0, len);
            }
            sos.flush();
            sos.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (sos != null) {
                    sos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
            }
        }
    }

    @Override
    public void getRaster(String visualId, int x, int y, int z, HttpServletResponse response) {
        Map<String, Object> raster = visualFileMapper.findById(visualId);
        y = (int) Math.pow(2, z) - y - 1;
        String path = visualAddress + raster.get("content") + "/" + z + "/" + x + "/" + y + ".png";
        InputStream in = null;
        ServletOutputStream sos = null;
        try {
            response.setContentType("image/png");
            sos = response.getOutputStream();
            File file = new File(path);
            if (!file.exists()) {
                in = new FileInputStream(visualAddress + "blank.png");
            } else {
                in = new FileInputStream(path);
            }
            byte[] bytes = new byte[1024];
            int len;
            while ((len = in.read(bytes)) > -1) {
                sos.write(bytes, 0, len);
            }
            sos.flush();
            sos.close();
            in.close();
        } catch (Exception e) {
//            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (sos != null) {
                    sos.close();
                }
            } catch (Exception e) {
//                throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
            }
        }
    }


    @Override
    public void getVectorTiles(String visualId, int x, int y, int z, HttpServletResponse response) {
        Map<String, Object> map = visualFileMapper.findById(visualId);
        TileBox tileBox = TileUtil.tile2boundingBox(x, y, z, (String) map.get("content"));
        tileBox.setVisualId(visualId);
        byte[] bytes;
        if (map.get("type").equals("pointVectorTile") || map.get("type").equals("lineVectorTile") || map.get("type").equals("polygonVectorTile")) {
            bytes = (byte[]) vectorTileMapper.getVictorTile(tileBox);
        } else {
            bytes = (byte[]) vectorTileMapper.getVictorTile3D(tileBox);
        }
        ServletOutputStream sos = null;
        try {
            response.setContentType("application/octet-stream");
            sos = response.getOutputStream();
            sos.write(bytes);
        } catch (Exception e) {

        } finally {
            try {
                if (sos != null) {
                    sos.close();
                }
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void getPhoto(String fileId, HttpServletResponse response) {
        Map<String, Object> map = fileMapper.findInfoById(fileId);
        InputStream in = null;
        ServletOutputStream sos = null;
        try {
            in = new FileInputStream(basePath + map.get("address"));
            sos = response.getOutputStream();
            byte[] bytes = new byte[1024];
            int len;
            while ((len = in.read(bytes)) > -1) {
                sos.write(bytes, 0, len);
            }
            sos.flush();
            sos.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (sos != null) {
                    sos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
            }
        }
    }

    @Override
    public JSONArray getCoordinates(String visualId) {
        Map<String, Object> map = visualFileMapper.findById(visualId);
        String content = (String) map.get("content");
        JSONObject jsonObject = JSON.parseObject(content);
        return jsonObject.getJSONArray("coordinates");
    }

    @Override
    public void getPngResource(String visualId, HttpServletResponse response) {
        Map<String, Object> map = visualFileMapper.findById(visualId);
        String content = (String) map.get("content");
        JSONObject jsonObject = JSON.parseObject(content);
        String address = visualAddress + jsonObject.getString("address");
        InputStream in = null;
        ServletOutputStream sos = null;
        try {
            in = new FileInputStream(address);
            sos = response.getOutputStream();
            response.setContentType("image/png");
            byte[] bytes = new byte[1024];
            int len;
            while ((len = in.read(bytes)) > -1) {
                sos.write(bytes, 0, len);
            }
            sos.flush();
            sos.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (sos != null) {
                    sos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
            }
        }
    }

    @Override
    public JSONObject getContent(String visualId) {
        Map<String, Object> map = visualFileMapper.findById(visualId);
        return JSON.parseObject(map.get("content").toString());
    }

    @Override
    public JSONObject getTide(String visualId) {
        Map<String, Object> map = visualFileMapper.findById(visualId);
        String path = visualAddress + map.get("content");
        return FileUtil.readJson(path);
    }

    @Override
    public JSONObject getRateDirection(String visualId) {
        Map<String, Object> map = visualFileMapper.findById(visualId);
        String path = visualAddress + map.get("content");
        return FileUtil.readJson(path);
    }

    @Override
    public JSONObject getSandContent(String visualId) {
        Map<String, Object> map = visualFileMapper.findById(visualId);
        String path = visualAddress + map.get("content");
        return FileUtil.readJson(path);
    }

    @Override
    public JSONObject getFlowSand_Z(String visualId) {
        Map<String, Object> map = visualFileMapper.findById(visualId);
        String path = visualAddress + map.get("content");
        return FileUtil.readJson(path);
    }

    @Override
    public JSONObject getSalinity(String visualId) {
        Map<String, Object> map = visualFileMapper.findById(visualId);
        String path = visualAddress + map.get("content");
        return FileUtil.readJson(path);
    }

    @Override
    public JSONObject getSuspension(String visualId) {
        Map<String, Object> map = visualFileMapper.findById(visualId);
        String path = visualAddress + map.get("content");
        return FileUtil.readJson(path);
    }

    @Override
    public JSONObject getGeoJson(String fileId) {
        String path = visualAddress + "geoJson/" + fileId + ".json";
        return FileUtil.readJson(path);
    }

    @Override
    public JSONObject getAnalyticGeoJson(String fileId) {
        Map<String, Object> map = analyticDataSetMapper.getInfoById(fileId);
        String path = basePath + map.get("creator") + "/project/" + map.get("projectId") + "/" + map.get("address");
        return FileUtil.readJson(path);
    }

    @Override
    public Map<String, Object> getSection(String fileId) {
        Map<String, Object> section = analyticDataSetMapper.getInfoById(fileId);
        String address = (String) section.get("address");
        String path = basePath + section.get("creator") + "/project/" + section.get("projectId") + "/" + address;
        File file = new File(path);
        if (!file.exists()) {
            throw new MyException(ResultEnum.NO_OBJECT);
        }
        BufferedReader br = null;
        List<Double> list = new ArrayList<>();
        try {
            br = new BufferedReader(new FileReader(path));

            String value = "";
            while ((value = br.readLine()) != null) {
                if (!value.equals("-3.4028235e+38")) {
                    list.add(Double.valueOf(value));
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        return map;
    }

    @Override
    public List<List<Double>> getSectionContrast(String fileId) {
        Map<String, Object> section = analyticDataSetMapper.getInfoById(fileId);
        String address = (String) section.get("address");
        String path = basePath + section.get("creator") + "/project/" + section.get("projectId") + "/" + address;
        File file = new File(path);
        List<List<Double>> result = new ArrayList<>();
        if (!file.exists()) {
            throw new MyException(ResultEnum.NO_OBJECT);
        }
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path));
            int number = Integer.parseInt(br.readLine());
            for (int i = 0; i < number; i++) {
                List<Double> list = new ArrayList<>();
                String value = "";
                while ((value = br.readLine()) != null) {
                    if (value.equals("")) {
                        break;
                    }
                    if (!value.equals("-3.4028235e+38")) {
                        list.add(Double.valueOf(value));
                    } else {
                        list.add(0.0);
                    }
                }
                result.add(list);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        }
        return result;
    }

    @Override
    public Map<String, Object> getSectionFlush(String fileId) {
        Map<String, Object> sectionFlush = analyticDataSetMapper.getInfoById(fileId);
        String address = (String) sectionFlush.get("address");
        String path = basePath + sectionFlush.get("creator") + "/project/" + sectionFlush.get("projectId") + "/" + address;
        File file = new File(path);
        Map<String, Object> result = new HashMap<>();
        if (!file.exists()) {
            throw new MyException(ResultEnum.NO_OBJECT);
        }
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path));

            List<List<Double>> list = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                List<Double> temp = new ArrayList<>();
                String value = "";
                while ((value = br.readLine()) != null) {
                    if (value.equals("")) {
                        break;
                    }
                    if (!value.equals("-3.4028235e+38")) {
                        temp.add(Double.valueOf(value));
                    } else {
                        temp.add(0.0);
                    }
                }
                list.add(temp);
            }
            br.close();
            result.put("benchmark", list.get(0));
            result.put("refer", list.get(1));
            result.put("flush", list.get(2));
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        }
    }

    @Override
    public JSONObject getVolume(String fileId) {
        Map<String, Object> volume = analyticDataSetMapper.getInfoById(fileId);
        String visualId = volume.get("visualId").toString();
        Map<String, Object> visualMap = visualFileMapper.findById(visualId);
        String address = visualMap.get("content").toString();
        String path = visualAddress + address;

        return FileUtil.readJson(path);
    }

    @Override
    public JSONObject getTianDiTu() {
//        String jsonString = "{\"version\":8,\"sources\":{\"tdtVec\":{\"type\":\"raster\",\"tiles\":[\"http://t0.tianditu.com/vec_w/wmts?tk=35a94ab5985969d0b93229c30db6abd6&SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=vec&STYLE=default&TILEMATRIXSET=w&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}&FORMAT=tiles\"],\"tileSize\":256},\"txt\":{\"type\":\"raster\",\"tiles\":[\"http://t0.tianditu.com/cva_w/wmts?tk=35a94ab5985969d0b93229c30db6abd6&SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cva&STYLE=default&TILEMATRIXSET=w&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}&FORMAT=tiles\"],\"tileSize\":256}},\"layers\":[{\"id\":\"tdtVec\",\"type\":\"raster\",\"source\":\"tdtVec\"},{\"id\":\"txt\",\"type\":\"raster\",\"source\":\"txt\"}]}";
        String path = mapAddress + "tianditu.json";
        return FileUtil.readJson(path);
    }

    @Override
    public JSONObject getTianDiTuImage() {
        String path = mapAddress + "tianditu-image.json";
        return FileUtil.readJson(path);
    }





    @Override
    public void uploadParts(String uid, String number, MultipartFile file) {
        File folder = new File(tempAddress + uid);
        if (number.equals("0")) {
            folder.mkdirs();
        }
        if (!folder.exists()) {
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        }
        InputStream ins = null;
        FileOutputStream outs = null;
        try {
            ins = file.getInputStream();
            outs = new FileOutputStream(tempAddress + uid + "/" + number);
            int len;
            byte[] bytes = new byte[1024];
            while ((len = ins.read(bytes)) != -1) {
                outs.write(bytes, 0, len);
            }
            outs.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        } finally {
            try {
                if (outs != null) {
                    outs.close();
                }
                if (ins != null) {
                    ins.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
            }
        }
    }

    @Override
    public String mergeParts(String uid, Integer total, String type, String name) {
        File folder = new File(tempAddress + uid);
        if (!folder.exists()) {
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        } else {
            if (folder.list().length != total) {
                throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
            }
        }
        if (type.equals("png") || type.equals("movePng")) {
            String suffix = name.substring(name.lastIndexOf("."));
            String fileName = UUID.randomUUID() + suffix;
            String to = visualAddress + "png/" + fileName;
            LocalUploadUtil.merge(tempAddress + uid, to, total);
            return fileName;
        } else if (type.equals("rasterTile")) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            long number = timestamp.getTime();
            String suffix = name.substring(name.lastIndexOf("."));
            String to = tempAddress + "rasterTile" + number + suffix;
            LocalUploadUtil.merge(tempAddress + uid, to, total);
            return "rasterTile" + number + suffix;
        } else if (type.equals("polygonVectorTile3D") || type.equals("pointVectorTile3D") || type.equals("lineVectorTile3D") || type.equals("polygonVectorTile") || type.equals("pointVectorTile") || type.equals("lineVectorTile")) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            long number = timestamp.getTime();
            String suffix = name.substring(name.lastIndexOf("."));
            String to = tempAddress + "shp" + number + suffix;
            LocalUploadUtil.merge(tempAddress + uid, to, total);
            return "shp" + number + ".shp";
        } else {
            String suffix = name.substring(name.lastIndexOf("."));
            String fileName = UUID.randomUUID() + suffix;
            String to;
            if (type.equals("flowSand_Z")) {
                to = visualAddress + "flowSand/" + fileName;
            } else {
                to = visualAddress + type + "/" + fileName;
            }
            LocalUploadUtil.merge(tempAddress + uid, to, total);
            return fileName;
        }
    }

}
