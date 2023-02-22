package njnu.edu.back.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.result.ResultEnum;
import njnu.edu.back.common.utils.FileUtil;
import njnu.edu.back.dao.ship.LocusMapper;
import njnu.edu.back.dao.staticdb.*;
import njnu.edu.back.service.MultiSourceService;
import njnu.edu.back.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
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
    public JSONArray getMeteorologyBox(double top, double right, double bottom, double left) {
        boolean flag = false;
        try {
            SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
            Date date1 = sdf1.parse(sdf1.format(new Date()));
            Date date2 = sdf1.parse("09:04");
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal1.setTime(date1);
            cal2.setTime(date2);
            if (cal1.after(cal2)) {
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        }
        String key;
        if (flag) {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
            key = "meteorology-" + sdf.format(date);
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1); //得到前一天
            Date date = calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
            key = "meteorology-" + sdf.format(date);
        }

        JSONArray jsonArray = (JSONArray) redisService.get(key);
        if (jsonArray == null) {
            File file = new File(meteorologyAddress);
            if(!file.exists()) {
                throw new MyException(ResultEnum.NO_OBJECT);
            }
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(meteorologyAddress));
                String jsonString = "";
                String line = "";
                while((line = br.readLine()) != null) {
                    jsonString += line;
                }
                br.close();
                jsonArray = JSON.parseArray(jsonString);
                redisService.set(key, jsonArray, 60*25l);
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

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            double lon = jsonObject.getDouble("longitude");
            double lat = jsonObject.getDouble("latitude");
            if (lon > right || lon < left || lat > top || lat < bottom) {
                jsonArray.remove(i);
                i--;
            }
        }
        return jsonArray;
    }

    @Override
    public List<Map<String, Object>> getStationBox(double top, double right, double bottom, double left) {
        List<Map<String, Object>> result = stationMapper.getStationByBox(top, right, bottom, left);
        try {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            SimpleDateFormat ymd = new SimpleDateFormat("yyyyMMdd");
            date = sdf.parse(sdf.format(date));
            String times;
            String oldTimes;
            if (date.after(sdf.parse("20:30"))) {
                times = ymd.format(new Date()) + "2000";
                oldTimes = ymd.format(new Date()) + "1200";
            } else if (date.after(sdf.parse("12:30"))) {
                times = ymd.format(new Date()) + "1200";
                oldTimes = ymd.format(new Date()) + "0800";
            } else if (date.after(sdf.parse("08:30"))) {
                times = ymd.format(new Date()) + "0800";
                oldTimes = ymd.format(new Date()) + "0600";
            } else {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                times = ymd.format(calendar.getTime()) + "0600";
                calendar.add(Calendar.DATE, -1);
                oldTimes = ymd.format(calendar.getTime()) + "2000";
            }
            JSONArray jsonArray = (JSONArray) redisService.get(times);
            if (jsonArray == null || jsonArray.size() == 0) {
                jsonArray = (JSONArray) redisService.get(oldTimes);
            }
            if (jsonArray == null || jsonArray.size() == 0) {
                throw new MyException(-99, "远程接口错误");
            } else {
                for (int i = 0; i < result.size(); i++) {
                    for (int j = 0; j < jsonArray.size(); j++) {
                        if (result.get(i).get("id").equals(jsonArray.getJSONObject(j).get("STATION")) && jsonArray.getJSONObject(j).get("TIME_STEP").equals("000")) {
                            result.get(i).put("info", jsonArray.getJSONObject(j));
                        }
                    }
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        }
    }

    @Override
    public List<JSONObject> getWeatherInfoById(String id) {
        try {
            List<JSONObject> list = new ArrayList<>();
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            SimpleDateFormat ymd = new SimpleDateFormat("yyyyMMdd");
            date = sdf.parse(sdf.format(date));
            String times;
            String oldTimes;
            if (date.after(sdf.parse("20:30"))) {
                times = ymd.format(new Date()) + "2000";
                oldTimes = ymd.format(new Date()) + "1200";
            } else if (date.after(sdf.parse("12:30"))) {
                times = ymd.format(new Date()) + "1200";
                oldTimes = ymd.format(new Date()) + "0800";
            } else if (date.after(sdf.parse("08:30"))) {
                times = ymd.format(new Date()) + "0800";
                oldTimes = ymd.format(new Date()) + "0600";
            } else {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                times = ymd.format(calendar.getTime()) + "0600";
                calendar.add(Calendar.DATE, -1);
                oldTimes = ymd.format(calendar.getTime()) + "2000";
            }
            JSONArray jsonArray = (JSONArray) redisService.get(times);
            if (jsonArray == null || jsonArray.size() == 0) {
                jsonArray = (JSONArray) redisService.get(oldTimes);
            }
            if (jsonArray == null || jsonArray.size() == 0) {
                throw new MyException(-99, "远程接口错误");
            } else {
                for (int i = 0; i < jsonArray.size(); i++) {
                    if (jsonArray.getJSONObject(i).getString("STATION").equals(id)) {
                        list.add(jsonArray.getJSONObject(i));
                    }
                }
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
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
    public JSONArray getBridgeInfo() {
        return FileUtil.readJsonArray(resourcePath + "bridge.json");
    }
}
