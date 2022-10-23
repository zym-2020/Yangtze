package njnu.edu.back.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.result.ResultEnum;
import njnu.edu.back.common.utils.AnalyseUtil;
import njnu.edu.back.common.utils.TileUtil;
import njnu.edu.back.dao.main.AnalyticDataSetMapper;
import njnu.edu.back.dao.main.AnalyticParameterMapper;
import njnu.edu.back.dao.main.FileMapper;
import njnu.edu.back.dao.main.VisualFileMapper;
import njnu.edu.back.pojo.VisualFile;
import njnu.edu.back.pojo.support.TileBox;
import njnu.edu.back.service.AnalyticDataSetService;
import njnu.edu.back.dao.shp.VectorTileMapper;
import njnu.edu.back.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

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

    @Autowired
    AnalyticParameterMapper analyticParameterMapper;

    @Autowired
    FileMapper fileMapper;

    @Autowired
    RedisService redisService;

    @Autowired
    VisualFileMapper visualFileMapper;

    @Value("${basePath}")
    String basePath;

    @Value("${visualAddress}")
    String visualAddress;

    @Value("${analyseAddress}")
    String analyseAddress;


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

    private JSONObject readJsonFile(String path) {
        File file = new File(path);
        if(!file.exists()) {
            throw new MyException(ResultEnum.NO_OBJECT);
        }
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path));
            String jsonString = "";
            String line = "";
            while((line = br.readLine()) != null) {
                jsonString += line;
            }
            br.close();
            JSONObject jsonObject = JSON.parseObject(jsonString);
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        } finally {
            try {
                if(br != null) {
                    br.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
            }
        }
    }

    @Override
    public Map<String, Object> checkState(String key) {
        Integer result = (Integer) redisService.get(key);
        if(result == null) {
            throw new MyException(-2, "计算错误");
        } else {
            if(result == 0) {
                throw new MyException(-1, "正在计算");
            } else if(result == 1) {
                redisService.del(key);
                return analyticDataSetMapper.getInfoById(key);

            } else {
                redisService.del(key);
                throw new MyException(-2, "计算错误");
            }
        }

    }

    @Override
    public String addSection(String projectId, String sectionId, String demId, String email) {
        Map<String, Object> section = analyticDataSetMapper.getInfoById(sectionId);
        String address = (String) section.get("address");
        Map<String, Object> file = fileMapper.findInfoById(demId);
        String sectionPath = basePath + email + "\\project\\" + projectId + "\\" + address;
        String demPath = basePath + file.get("address");
        JSONObject jsonObject = readJsonFile(sectionPath);
        JSONArray jsonArray = jsonObject.getJSONObject("geometry").getJSONArray("coordinates");
        String tempPath = basePath + email + "\\temp\\" + UUID.randomUUID().toString() + ".txt";
        String resultUUID = UUID.randomUUID().toString();
        String resultPath =  basePath + email + "\\project\\" + projectId + "\\" + resultUUID + ".txt";
        String result = UUID.randomUUID().toString();
        redisService.set(result, 0, 60l);
        new Thread() {
            @Override
            @SneakyThrows
            public void run() {
                Process process = AnalyseUtil.saveSectionValue(tempPath, demPath, jsonArray, resultPath);
                int code = process.waitFor();
                if(code == 0) {
                    analyticDataSetMapper.addDraw(result, section.get("fileName") + "_" + file.get("fileName"), resultUUID + ".txt", email, "section", "", projectId);
                    redisService.set(result, 1, 60l);
                } else {
                    redisService.set(result, -1, 60l);
                }
            }
        }.start();
        return result;
    }

    @Override
    public String addSectionCompare(String projectId, String sectionId, String email, List<String> demList) {
        Map<String, Object> section = analyticDataSetMapper.getInfoById(sectionId);
        String address = (String) section.get("address");
        List<Map<String, Object>> files = fileMapper.findInfoListById(demList);
        List<String> rasterPathList = new ArrayList<>();
        for(Map<String, Object> map : files) {
            rasterPathList.add(basePath + map.get("address"));
        }
        String sectionPath = basePath + email + "\\project\\" + projectId + "\\" + address;
        JSONObject jsonObject = readJsonFile(sectionPath);
        JSONArray jsonArray = jsonObject.getJSONObject("geometry").getJSONArray("coordinates");
        String tempPath = basePath + email + "\\temp\\" + UUID.randomUUID().toString() + ".txt";
        String resultUUID = UUID.randomUUID().toString();
        String resultPath =  basePath + email + "\\project\\" + projectId + "\\" + resultUUID + ".txt";
        String result = UUID.randomUUID().toString();
        redisService.set(result, 0, 60l);
        new Thread() {
            @Override
            @SneakyThrows
            public void run() {
                String fileName = "断面比较_" + section.get("fileName");
                Process process = AnalyseUtil.savaSectionContrast(tempPath, rasterPathList, jsonArray, resultPath);
                int code = process.waitFor();
                if(code == 0) {
                    analyticDataSetMapper.addDraw(result, fileName, resultUUID + ".txt", email, "sectionContrast", "", projectId);
                    redisService.set(result, 1, 60l);
                } else {
                    redisService.set(result, -1, 60l);
                }
            }
        }.start();
        return result;
    }

    @Override
    public String addSectionFlush(String projectId, String sectionId, String benchmarkId, String referId, String email) {
        Map<String, Object> section = analyticDataSetMapper.getInfoById(sectionId);
        String sectionPath = basePath + email + "\\project\\" + projectId + "\\" + section.get("address");
        String address = analyticParameterMapper.findAddressByBenchmarkIdAndReferId(benchmarkId, referId, "flush");
        Map<String, Object> benchmark = fileMapper.findInfoById(benchmarkId);
        Map<String, Object> refer = fileMapper.findInfoById(referId);
        String benchmarkPath = basePath + benchmark.get("address");
        String referPath = basePath + refer.get("address");
        JSONObject jsonObject = readJsonFile(sectionPath);
        JSONArray jsonArray = jsonObject.getJSONObject("geometry").getJSONArray("coordinates");
        String tempPath = basePath + email + "\\temp\\" + UUID.randomUUID().toString() + ".txt";
        String resultUUID = UUID.randomUUID().toString();
        String resultPath =  basePath + email + "\\project\\" + projectId + "\\" + resultUUID + ".txt";
        String result = UUID.randomUUID().toString();
        redisService.set(result, 0, 60l);
        new Thread() {
            @Override
            @SneakyThrows
            public void run() {
                Process process = AnalyseUtil.sectionFlush(tempPath, benchmarkPath, referPath, analyseAddress + address, jsonArray, resultPath);
                int code = process.waitFor();
                if(code == 0) {
                    analyticDataSetMapper.addDraw(result, section.get("fileName") + "_断面冲淤", resultUUID + ".txt", email, "sectionFlush", "", projectId);
                    redisService.set(result, 1, 60l);
                } else {
                    redisService.set(result, -1, 60l);
                }
            }
        }.start();
        return result;
    }

    @Override
    public String addRegionFlush(String projectId, String regionId, String benchmarkId, String referId, String email) {
        Map<String, Object> region = analyticDataSetMapper.getInfoById(regionId);
        String regionPath = basePath + email + "\\project\\" + projectId + "\\" + region.get("address");
        String address = analyticParameterMapper.findAddressByBenchmarkIdAndReferId(benchmarkId, referId, "flush");
        JSONObject jsonObject = readJsonFile(regionPath);
        JSONArray jsonArray = jsonObject.getJSONObject("geometry").getJSONArray("coordinates");
        String tempPath = basePath + email + "\\temp\\" + UUID.randomUUID() + ".txt";
        String resultUUID = UUID.randomUUID().toString();
        String tifPath = basePath + email + "\\project\\" + projectId + "\\" +resultUUID + ".tif";
        String pngPath = visualAddress + "png\\" + resultUUID + ".png";
        String coordinatePath = basePath + email + "\\temp\\" + resultUUID + ".json";
        String result = UUID.randomUUID().toString();
        redisService.set(result, 0, 60l);
        new Thread() {
            @Override
            @SneakyThrows
            public void run() {
                Process process = AnalyseUtil.rasterCrop(tempPath, analyseAddress + address, pngPath, tifPath, coordinatePath, jsonArray);
                int code = process.waitFor();
                if(code == 0) {
                    String content = getPngContent("png\\" + resultUUID + ".png", coordinatePath);
                    Map<String, Object> map = visualFileMapper.addVisualFile(new VisualFile(null, resultUUID + ".png", "png", content));
                    analyticDataSetMapper.addDraw(result, region.get("fileName") + "_区域冲淤", resultUUID + ".tif", email, "regionFlush", map.get("id").toString(), projectId);
                    redisService.set(result, 1, 60l);
                } else {
                    redisService.set(result, -1, 60l);
                }
            }
        }.start();
        return result;
    }

    private String getPngContent(String address, String path) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("address", address);
        File file = new File(path);
        if(!file.exists()) {
            throw new MyException(ResultEnum.NO_OBJECT);
        }
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path));
            String jsonString = "";
            String line = "";
            while((line = br.readLine()) != null) {
                jsonString += line;
            }
            br.close();
            JSONObject json = JSON.parseObject(jsonString);
            JSONArray jsonArray = new JSONArray();
            jsonArray.add(json.getJSONArray("ul"));
            jsonArray.add(json.getJSONArray("ur"));
            jsonArray.add(json.getJSONArray("lr"));
            jsonArray.add(json.getJSONArray("ll"));
            jsonObject.put("coordinates", jsonArray);
            file.delete();
            return jsonObject.toJSONString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        }
    }

    @Override
    public Map<String, Object> addElevationFlush(String projectId, String benchmarkId, String referId, String email) {
        Map<String, Object> map = analyticParameterMapper.findByBenchmarkIdAndReferId(benchmarkId, referId, "flush");
        String id = map.get("id").toString();
        String content = map.get("content").toString();
        String fileName = map.get("address").toString();
        String result = analyticDataSetMapper.addDraw("", fileName, id, email, "elevationFlush", content, projectId);
        Map<String, Object> m = new HashMap<>();
        m.put("id", result);
        m.put("fileName", fileName);
        m.put("visualId", content);
        return m;
    }

    @Override
    public Map<String, Object> addFlushContour(String projectId, String benchmarkId, String referId, String email) {
        Map<String, Object> map = analyticParameterMapper.findByBenchmarkIdAndReferId(benchmarkId, referId, "flushContour");
        String id = map.get("id").toString();
        String content = map.get("content").toString();
        String fileName = map.get("address").toString();
        String result = analyticDataSetMapper.addDraw("", fileName, id, email, "flushContour", content, projectId);
        Map<String, Object> m = new HashMap<>();
        m.put("id", result);
        m.put("fileName", fileName);
        m.put("visualId", content);
        return m;
    }

    @Override
    public Map<String, Object> addSlope(String projectId, String demId, String email) {
        Map<String, Object> map = analyticParameterMapper.findSlope(demId);
        String id = map.get("id").toString();
        String content = map.get("content").toString();
        String fileName = map.get("address").toString();
        String result = analyticDataSetMapper.addDraw("", fileName, id, email, "slope", content, projectId);
        Map<String, Object> m = new HashMap<>();
        m.put("id", result);
        m.put("fileName", fileName);
        m.put("visualId", content);
        return m;
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
