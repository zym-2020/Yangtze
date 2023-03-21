package njnu.edu.back.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.result.ResultEnum;
import njnu.edu.back.common.utils.FileUtil;
import njnu.edu.back.common.utils.InternetUtil;
import njnu.edu.back.dao.ship.LocusMapper;
import njnu.edu.back.dao.ship.ShipMapper;
import njnu.edu.back.dao.staticdb.*;
import njnu.edu.back.service.MultiSourceService;
import njnu.edu.back.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/12/01/10:46
 * @Description:
 */
@Service
public class MultiSourceServiceImpl implements MultiSourceService {

    @Value("${meteorologyAddress}")
    String meteorologyAddress;

    @Value("${waterLevelAddress}")
    String waterLevelAddress;

    @Autowired
    ShipMapper shipMapper;

    @Autowired
    BuoyMapper buoyMapper;

    @Autowired
    PhotoMapper photoMapper;

    @Autowired
    BuoyMoveMapper buoyMoveMapper;

    @Autowired
    ParkMapper parkMapper;

    @Autowired
    AnchorMapper anchorMapper;

    @Autowired
    OtherMapper otherMapper;

    @Autowired
    StationMapper stationMapper;

    @Autowired
    LocusMapper locusMapper;

    @Autowired
    RedisService redisService;

    @Value("${resourcePath}")
    String resourcePath;

    @Value("${visualAddress}")
    String visualAddress;

    @Value("${meteorologyUrl}")
    String meteorologyUrl;

    @Value("${meteorologyPngUrl}")
    String meteorologyPngUrl;

    @Value("${AISUrl}")
    String AISUrl;

    @Value("${AISListUrl}")
    String AISListUrl;

    @Override
    public List<Map<String, Object>> getBuoyByBox(double top, double right, double bottom, double left) {
        return buoyMapper.getBuoyByBox(top, right, bottom, left);
    }

    @Override
    public List<Map<String, Object>> getBuoyMoveInfoByTimestamp(long startTime, long endTime) {
        return buoyMoveMapper.getInfoByTimestamp(startTime, endTime);
    }

    @Override
    public void getPhoto(String fileName, HttpServletResponse response) {
        byte[] bytes = (byte[]) photoMapper.getPhoto(fileName);
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            ServletOutputStream sos = response.getOutputStream();
            int off = 0;
            while (off < bytes.length) {
                if (off + 1024 <= bytes.length) {
                    sos.write(bytes, off, 1024);
                } else {
                    sos.write(bytes, off, bytes.length - off);
                }
                off += 1024;
            }
            sos.flush();
            sos.close();

        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        }
    }

    @Override
    public List<Map<String, Object>> getParkInfoByBox(double top, double right, double bottom, double left) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<Map<String, Object>> parkList = parkMapper.getAllInfo();
        for (Map<String, Object> map : parkList) {
            JSONObject jsonObject = JSON.parseObject((String) map.get("qyfw"));
            if (jsonObject.getString("type").equals("point")) {
                JSONArray jsonArray = jsonObject.getJSONArray("point");
                double lon = jsonArray.getDouble(0);
                double lat = jsonArray.getDouble(1);
                if (lon > left && lon < right && lat > bottom && lat < top) {
                    map.replace("qyfw", jsonObject);
                    result.add(map);
                }
            } else {
                JSONArray jsonArray = jsonObject.getJSONArray("points");
                if (jsonArray.size() > 2) {
                    double tempTop = jsonArray.getJSONArray(0).getDouble(1);
                    double tempBottom = jsonArray.getJSONArray(0).getDouble(1);
                    double tempRight = jsonArray.getJSONArray(0).getDouble(0);
                    double tempLeft = jsonArray.getJSONArray(0).getDouble(0);
                    for (int i = 1; i < jsonArray.size(); i++) {
                        double lon = jsonArray.getJSONArray(i).getDouble(0);
                        double lat = jsonArray.getJSONArray(i).getDouble(1);
                        if (lon > tempRight) {
                            tempRight = lon;
                        } else if (lon < tempLeft) {
                            tempLeft = lon;
                        }
                        if (lat > tempTop) {
                            tempTop = lat;
                        } else if (lat < tempBottom) {
                            tempBottom = lat;
                        }
                    }
                    if (!(tempLeft > right || tempRight < left || tempTop < bottom || tempBottom > top)) {
                        map.replace("qyfw", jsonObject);
                        result.add(map);
                    }
                } else {
                    double tempTop = jsonArray.getJSONArray(0).getDouble(1);
                    double tempRight = jsonArray.getJSONArray(0).getDouble(0);
                    double tempBottom = jsonArray.getJSONArray(1).getDouble(1);
                    double tempLeft = jsonArray.getJSONArray(1).getDouble(0);
                    if (!(tempLeft > right || tempRight < left || tempTop < bottom || tempBottom > top)) {
                        map.replace("qyfw", jsonObject);
                        result.add(map);
                    }
                }
            }
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> getAnchorInfoByBox(double top, double right, double bottom, double left) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<Map<String, Object>> anchorList = anchorMapper.getAllInfo();
        for (Map<String, Object> map : anchorList) {
            JSONObject jsonObject = JSON.parseObject((String) map.get("qyfw"));
            JSONArray jsonArray = jsonObject.getJSONArray("points");
            double tempTop = jsonArray.getJSONArray(0).getDouble(1);
            double tempBottom = jsonArray.getJSONArray(0).getDouble(1);
            double tempRight = jsonArray.getJSONArray(0).getDouble(0);
            double tempLeft = jsonArray.getJSONArray(0).getDouble(0);
            for (int i = 1; i < jsonArray.size(); i++) {
                double lon = jsonArray.getJSONArray(i).getDouble(0);
                double lat = jsonArray.getJSONArray(i).getDouble(1);
                if (lon > tempRight) {
                    tempRight = lon;
                } else if (lon < tempLeft) {
                    tempLeft = lon;
                }
                if (lat > tempTop) {
                    tempTop = lat;
                } else if (lat < tempBottom) {
                    tempBottom = lat;
                }
            }
            if (!(tempLeft > right || tempRight < left || tempTop < bottom || tempBottom > top)) {
                map.replace("qyfw", jsonObject);
                result.add(map);
            }
        }

        return result;
    }

    @Override
    public List<Map<String, Object>> getOtherInfoBox(double top, double right, double bottom, double left) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<Map<String, Object>> otherList = otherMapper.getAllInfo();
        for (Map<String, Object> map : otherList) {
            JSONObject jsonObject = JSON.parseObject((String) map.get("qyfw"));
            JSONArray jsonArray = jsonObject.getJSONArray("points");
            double tempTop = jsonArray.getJSONArray(0).getDouble(1);
            double tempBottom = jsonArray.getJSONArray(0).getDouble(1);
            double tempRight = jsonArray.getJSONArray(0).getDouble(0);
            double tempLeft = jsonArray.getJSONArray(0).getDouble(0);
            for (int i = 1; i < jsonArray.size(); i++) {
                double lon = jsonArray.getJSONArray(i).getDouble(0);
                double lat = jsonArray.getJSONArray(i).getDouble(1);
                if (lon > tempRight) {
                    tempRight = lon;
                } else if (lon < tempLeft) {
                    tempLeft = lon;
                }
                if (lat > tempTop) {
                    tempTop = lat;
                } else if (lat < tempBottom) {
                    tempBottom = lat;
                }
            }
            if (!(tempLeft > right || tempRight < left || tempTop < bottom || tempBottom > top)) {
                map.replace("qyfw", jsonObject);
                result.add(map);
            }
        }
        return result;
    }

    @Override
    public JSONArray getMeteorology() {
        String path = resourcePath + "meteorology/meteorology.json";
        JSONArray jsonArray = FileUtil.readJsonArray(path);
        List<CompletableFuture> list = new ArrayList<>();
        JSONArray result = new JSONArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            int code = jsonArray.getJSONObject(i).getIntValue("region");
            CompletableFuture future = CompletableFuture.supplyAsync(() -> {
                Map<String, String> map = new HashMap<>();
                map.put("adcode", String.valueOf(code));
                try {
                    String str = InternetUtil.doGet(meteorologyUrl, map, "utf-8");
                    JSONArray temp = JSON.parseObject(str).getJSONArray("data");
                    for (int j = 0; j < temp.size(); j++) {
                        File f = new File(resourcePath + "meteorology/png/" + temp.getJSONObject(j).getString("type") + ".png");
                        if (!f.exists()) {
                            String url = meteorologyPngUrl + temp.getJSONObject(j).getString("type") + ".png";
                            String pngPath = resourcePath + "meteorology/png/" + temp.getJSONObject(j).getString("type") + ".png";
                            try {
                                InternetUtil.downloadMeteorologyPng(url, pngPath);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        result.add(temp.getJSONObject(j));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            });
            list.add(future);
        }
        CompletableFuture all = CompletableFuture.allOf(list.toArray(new CompletableFuture[list.size()]));
        try {
            all.get();
        } catch (Exception e) {
            System.out.println(e);
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        }
        return result;
    }

    @Override
    public void getMeteorologyPng(String fileName, HttpServletResponse response) {
        String path = resourcePath + "meteorology/png/" + fileName;
        File file = new File(path);
        if (!file.exists()) {
            throw new MyException(ResultEnum.NO_OBJECT);
        }
        try {
            InputStream inputStream = new FileInputStream(path);
            ServletOutputStream outputStream = response.getOutputStream();
            byte[] bytes = new byte[1024];
            int len;
            while((len = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        }
    }

    @Override
    public List<Map<String, Object>> getShipInfoByBox(double top, double right, double bottom, double left) {
//        Date date = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//        String tableName = "locus" + sdf.format(date);
        String tableName = "locus20221207";
        return locusMapper.getShipByBox(tableName, top, right, bottom, left);
    }

    @Override
    public Map<String, Object> getShipInfoByMMSI(String mmsi) {
        return getShipInfoByMMSI(mmsi);
    }

    @Override
    public List<Map<String, Object>> record(String mmsi) {
//        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
//        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date date = new Date();
//        String todayTable = "locus" + sdf1.format(date);
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(date);
//        cal.add(Calendar.DATE, -1);
//        String yesterdayTable = "locus" + sdf1.format(cal.getTime());
        String todayTable = "locus20221206";
        String yesterdayTable = "locus20221207";
//        String time = sdf2.format(cal.getTime());
        String time = "2022-12-06 14:55:00";
        if (locusMapper.existTable(yesterdayTable) == 0) {
            return locusMapper.record(todayTable, mmsi, "");
        } else {
            CompletableFuture<List<Map<String, Object>>> completableFuture = CompletableFuture.supplyAsync(() -> locusMapper.record(yesterdayTable, mmsi, time));
            List<Map<String, Object>> mapList = locusMapper.record(todayTable, mmsi, "");
            try {
                mapList.addAll(completableFuture.get());
                return mapList;
            } catch (Exception e) {
                e.printStackTrace();
                throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
            }
        }
    }

    @Override
    public List<Map<String, Object>> getShipInfoByBoxAndTime(double top, double right, double bottom, double left, String startTime, String endTime) {
        String tableName = "locus20221207";
        startTime = "2022-12-07 " + startTime;
        endTime = "2022-12-07 " + endTime;
        return locusMapper.getShipInfoByBoxAndTime(tableName, top, right, bottom, left, startTime, endTime);
    }

    @Override
    public List<JSONObject> queryBoxShip(double top, double right, double bottom, double left) {
        List<JSONObject> list = new ArrayList<>();
        JSONObject jsonObject;
        String url = MessageFormat.format(AISUrl, left, bottom, right, top);
        try {
            jsonObject = InternetUtil.httpHandle(url, new LinkedMultiValueMap<>(), JSONObject.class, "get");
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        }

        if (jsonObject.getIntValue("status") == 200) {
            JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONObject("hits").getJSONArray("hits");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject json = JSON.parseObject(jsonArray.getJSONObject(i).getJSONObject("_source").getString("info"));
                JSONObject j = new JSONObject();
                j.put("mmsi", json.getString("mmsi"));
                j.put("update_time", json.getString("jssj"));
                j.put("register_time", json.getString("cjsj"));
                j.put("name", json.getString("cbmc"));
                j.put("name_cn", json.getString("zwmc"));
                j.put("direction", json.getString("cbhx"));
                j.put("velocity", json.getString("dqhs"));
                j.put("length", json.getString("cd"));
                j.put("width", json.getString("kd"));
                j.put("lon", json.getString("zbjd"));
                j.put("lat", json.getString("zbwd"));
                list.add(j);
            }
        } else {
            throw new MyException(-99, "接口请求过于频繁");
        }

        return list;
    }

    @Override
    public JSONArray getBridgeInfo() {
        return FileUtil.readJsonArray(resourcePath + "bridge.json");
    }

    @Override
    public void seaChart(String type, String x, String y, String z, HttpServletResponse response) {
        String path;
        if (type.equals("map")) {
            path = resourcePath + "shipSpiderRes/depth_" + z + "/map/" + y + "_" + x + "_map.png";
        } else if(type.equals("mark")) {
            path = resourcePath + "shipSpiderRes/depth_" + z + "/mark/" + y + "_" + x + "_mark.png";
        } else if (type.equals("yangtze")) {
            path = MessageFormat.format(resourcePath + "yangtzeTiles/{0}/{1}/{2}.png", z, x, y);
        } else {
            throw new MyException(-99, "type参数错误");
        }
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
        }
    }

    @Override
    public JSONArray getStationByBox(Double top, Double right, Double bottom, Double left) {
        JSONArray result = new JSONArray();
        JSONArray jsonArray = FileUtil.readJsonArray(resourcePath + "station_name.json");
        for (int i = 0; i < jsonArray.size(); i++) {
            if(jsonArray.getJSONObject(i).getDouble("lon") > left && jsonArray.getJSONObject(i).getDouble("lon") < right && jsonArray.getJSONObject(i).getDouble("lat") > bottom && jsonArray.getJSONObject(i).getDouble("lat") < top) {
                result.add(jsonArray.getJSONObject(i));
            }
        }
        return result;
    }

    @Override
    public JSONArray getStationList() {
        return FileUtil.readJsonArray(resourcePath + "station_name.json");
    }

    @Override
    public JSONArray getWaterLevelByStationAndTime(String type, String station, String startTime, String endTime) {
        String prefix;
        if (type.equals("yangtze")) {
            prefix = "YangtzeDownstream";
        } else if (type.equals("hubei")) {
            prefix = "hubei";
        } else if (type.equals("anhui")) {
            prefix = "anhui";
        } else if (type.equals("jiangsu")) {
            prefix = "jiangsu";
        } else if (type.equals("zhejiang")) {
            prefix = "zhejiang";
        } else {
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        }

        try {
            String url = waterLevelAddress + "/" + prefix + "/getInfoByStationAndTime/" + station + "/" + startTime + "/" + endTime;
            url = InternetUtil.encodeSpaceChinese(url, "UTF-8");

            String res = InternetUtil.doGet(url, new HashMap<>(), "utf-8");
            return JSON.parseObject(res).getJSONArray("data");
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ResultEnum.REMOTE_SERVICE_ERROR);
        }
    }

    @Override
    public Map<String, Object> pageList(String type, int page, int size, String keyword) {
        Map<String, Object> map = new HashMap<>();
        String name = keyword.equals("all") ? "" : keyword;
        if (!keyword.equals("all")) {
            keyword = "%" + keyword + "%";
        }
        if (type.equals("ship")) {
            map.put("total", shipMapper.count(keyword));
            map.put("list", shipMapper.pageQuery(size, size * page, keyword));
        } else if (type.equals("buoy")) {
            map.put("total", buoyMapper.count(keyword));
            map.put("list", buoyMapper.pageQuery(size, size * page, keyword));
        } else if (type.equals("park")) {
            map.put("total", parkMapper.count(keyword));
            map.put("list", parkMapper.pageQuery(size, size * page, keyword));
        } else if (type.equals("anchor")) {
            map.put("total", anchorMapper.count(keyword));
            map.put("list", anchorMapper.pageQuery(size, size * page, keyword));
        } else if (type.equals("bridge")) {
            JSONArray jsonArray = FileUtil.readJsonArray(resourcePath + "bridge.json");
            List<JSONObject> list = new ArrayList<>();
            for (int i = size * page, count = 0; i < jsonArray.size() && count < size; i++, count++) {
                list.add(jsonArray.getJSONObject(i));
            }
            map.put("total", jsonArray.size());
            map.put("list", list);
        } else if (type.equals("station")) {
            JSONArray jsonArray = FileUtil.readJsonArray(resourcePath + "station_name.json");
            List<JSONObject> list = new ArrayList<>();
            for (int i = size * page, count = 0; i < jsonArray.size() && count < size; i++, count++) {
                list.add(jsonArray.getJSONObject(i));
            }
            map.put("total", jsonArray.size());
            map.put("list", list);
        } else if (type.equals("realShip")) {
            String url = MessageFormat.format(AISListUrl, page * size, size, name);
            JSONObject jsonObject;
            try {
                jsonObject = InternetUtil.httpHandle(url, new LinkedMultiValueMap<>(), JSONObject.class, "get");
                JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONObject("hits").getJSONArray("hits");
                map.put("total", jsonObject.getJSONObject("data").getJSONObject("hits").getIntValue("total"));
                List<JSONObject> list = new ArrayList<>();
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject json = JSON.parseObject(jsonArray.getJSONObject(i).getJSONObject("_source").getString("info"));
                    list.add(json);
                }
                map.put("list", list);
            } catch (Exception e) {
                e.printStackTrace();
                throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
            }
        } else {
            throw new MyException(ResultEnum.NO_OBJECT);
        }
        return map;
    }
}
