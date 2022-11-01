package njnu.edu.back.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import net.lingala.zip4j.ZipFile;
import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.result.ResultEnum;
import njnu.edu.back.common.utils.AnalyseUtil;
import njnu.edu.back.common.utils.Encrypt;
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
import java.net.URLEncoder;
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

    @Value("${encrypt.key}")
    String key;

    @Value("${tempAddress}")
    String tempAddress;


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
        String path = basePath + email + "/project/" + projectId;
        String visualPath = visualAddress + "geoJson";
        String uuid = UUID.randomUUID().toString();
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter(path + "/" + uuid + ".json");
            bw = new BufferedWriter(fw);
            bw.write(jsonString);
            bw.flush();
            fw.close();
            bw.close();
            fw = new FileWriter(visualPath + "/" + uuid + ".json");
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
        String sectionPath = basePath + email + "/project/" + projectId + "/" + address;
        String demPath = basePath + file.get("address");
        JSONObject jsonObject = readJsonFile(sectionPath);
        JSONArray jsonArray = jsonObject.getJSONObject("geometry").getJSONArray("coordinates");
        String tempPath = basePath + email + "/temp/" + UUID.randomUUID().toString() + ".txt";
        String resultUUID = UUID.randomUUID().toString();
        String resultPath =  basePath + email + "/project/" + projectId + "/" + resultUUID + ".txt";
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
        String sectionPath = basePath + email + "/project/" + projectId + "/" + address;
        JSONObject jsonObject = readJsonFile(sectionPath);
        JSONArray jsonArray = jsonObject.getJSONObject("geometry").getJSONArray("coordinates");
        String tempPath = basePath + email + "/temp/" + UUID.randomUUID().toString() + ".txt";
        String resultUUID = UUID.randomUUID().toString();
        String resultPath =  basePath + email + "/project/" + projectId + "/" + resultUUID + ".txt";
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
        String sectionPath = basePath + email + "/project/" + projectId + "/" + section.get("address");
        String address = analyticParameterMapper.findAddressByBenchmarkIdAndReferId(benchmarkId, referId, "flush");
        Map<String, Object> benchmark = fileMapper.findInfoById(benchmarkId);
        Map<String, Object> refer = fileMapper.findInfoById(referId);
        String benchmarkPath = basePath + benchmark.get("address");
        String referPath = basePath + refer.get("address");
        JSONObject jsonObject = readJsonFile(sectionPath);
        JSONArray jsonArray = jsonObject.getJSONObject("geometry").getJSONArray("coordinates");
        String tempPath = basePath + email + "/temp/" + UUID.randomUUID().toString() + ".txt";
        String resultUUID = UUID.randomUUID().toString();
        String resultPath =  basePath + email + "/project/" + projectId + "/" + resultUUID + ".txt";
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
        String regionPath = basePath + email + "/project/" + projectId + "/" + region.get("address");
        String address = analyticParameterMapper.findAddressByBenchmarkIdAndReferId(benchmarkId, referId, "flush");
        JSONObject jsonObject = readJsonFile(regionPath);
        JSONArray jsonArray = jsonObject.getJSONObject("geometry").getJSONArray("coordinates");
        String tempPath = basePath + email + "/temp/" + UUID.randomUUID() + ".txt";
        String resultUUID = UUID.randomUUID().toString();
        String tifPath = basePath + email + "/project/" + projectId + "/" +resultUUID + ".tif";
        String pngPath = visualAddress + "png/" + resultUUID + ".png";
        String coordinatePath = basePath + email + "/temp/" + resultUUID + ".json";
        String result = UUID.randomUUID().toString();
        redisService.set(result, 0, 60l);
        new Thread() {
            @Override
            @SneakyThrows
            public void run() {
                Process process = AnalyseUtil.rasterCrop(tempPath, analyseAddress + address, pngPath, tifPath, coordinatePath, jsonArray);
                int code = process.waitFor();
                if(code == 0) {
                    String content = getPngContent("png/" + resultUUID + ".png", coordinatePath);
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

    @Override
    public String computeVolume(double deep, String projectId, String regionId, String demId, String email) {
        Map<String, Object> region = analyticDataSetMapper.getInfoById(regionId);
        String regionPath = basePath + email + "/project/" + projectId + "/" + region.get("address");
        JSONObject jsonObject = readJsonFile(regionPath);
        JSONArray jsonArray = jsonObject.getJSONObject("geometry").getJSONArray("coordinates");
        String tempPath = basePath + email + "/temp/" + UUID.randomUUID() + ".txt";
        Map<String, Object> file = fileMapper.findInfoById(demId);
        String demPath = basePath + file.get("address");
        String resultId = UUID.randomUUID().toString();
        String visualId = UUID.randomUUID().toString();
        String result = UUID.randomUUID().toString();
        String resultPath = basePath + email + "/project/" + projectId + "/" + resultId + ".json";
        String visualPath = visualAddress + "volume/" + visualId + ".json";
        redisService.set(result, 0, 60l);
        new Thread() {
            @Override
            @SneakyThrows
            public void run() {
                Process process = AnalyseUtil.computeVolume(tempPath, deep, demPath, resultPath, visualPath, jsonArray);
                int code = process.waitFor();
                if(code == 0) {
                    String content = "volume/" + visualId + ".json";
                    Map<String, Object> map = visualFileMapper.addVisualFile(new VisualFile(null, visualId + ".json", "volume", content));
                    analyticDataSetMapper.addDraw(result, region.get("fileName") + "_容积", resultId + ".json", email, "volume", map.get("id").toString(), projectId);
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

    @Override
    public void rename(String id, String name) {
        analyticDataSetMapper.rename(id, name);
    }

    @Override
    public String getUrl(String id, String userId) {
        String uuid = UUID.randomUUID().toString();
        redisService.set(uuid, id, 30l);
        return Encrypt.encryptByUserId(uuid, userId, key.toCharArray());
    }

    @Override
    public void downloadAnalyticData(String userId, String id, HttpServletResponse response) {
        String tempId = (String) redisService.get(id);
        if(tempId == null) {
            throw new MyException(-1, "链接已失效");
        } else {
            redisService.del(id);
            id = tempId;
        }
        Map<String, Object> map = analyticDataSetMapper.getInfoById(id);
        if(map == null) {
            throw new MyException(ResultEnum.NO_OBJECT);
        }
        String address = (String) map.get("address");
        String type = (String) map.get("visualType");
        String path;
        if(type.equals("elevationFlush") || type.equals("flushContour") || type.equals("slope")) {
            Map<String, Object> analyticParameter = analyticParameterMapper.findInfoById(address);
            path = analyseAddress + analyticParameter.get("address");
            File file = new File(path);
            File[] filesArray = file.listFiles();
            ZipFile zipFile = new ZipFile(tempAddress + analyticParameter.get("address") + ".zip");
            InputStream in = null;
            ServletOutputStream sos = null;
            try {
                List<File> files = Arrays.asList(filesArray);
                zipFile.addFiles(files);
                in = new FileInputStream(tempAddress + analyticParameter.get("address") + ".zip");
                sos = response.getOutputStream();
                response.setContentType("application/octet-stream");
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(analyticParameter.get("address") + ".zip", "UTF-8"));
                byte[] b = new byte[1024];
                int len;
                while((len = in.read(b)) > 0) {
                    sos.write(b, 0, len);
                }
                sos.flush();
                sos.close();
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
                throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
            }
        } else {
            InputStream in = null;
            ServletOutputStream sos = null;
            try {
                response.setContentType("application/octet-stream");
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(address, "UTF-8"));
                in = new FileInputStream(basePath + map.get("creator") + "/project/" + map.get("projectId") + "/" + address);
                sos = response.getOutputStream();
                byte[] b = new byte[1024];
                int len;
                while((len = in.read(b)) > 0) {
                    sos.write(b, 0, len);
                }
                sos.flush();
                sos.close();
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
                throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
            }
        }
    }


}
