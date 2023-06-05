package njnu.edu.back.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/12/01/10:46
 * @Description:
 */
public interface MultiSourceService {
    List<Map<String, Object>> getBuoyByBox(double top, double right, double bottom, double left);

    void getPhoto(String fileName, HttpServletResponse response);

    List<Map<String, Object>> getParkInfoByBox(double top, double right, double bottom, double left);

    List<Map<String, Object>> getAnchorInfoByBox(double top, double right, double bottom, double left);

    List<Map<String, Object>> getOtherInfoBox(double top, double right, double bottom, double left);

    JSONArray getMeteorology();

    void getMeteorologyPng(String fileName, HttpServletResponse response);

    List<Map<String, Object>> getShipInfoByBox(double top, double right, double bottom, double left);

    Map<String, Object> getShipInfoByMMSI(String mmsi);

    List<Map<String, Object>> record(String mmsi);

    List<Map<String, Object>> getShipInfoByBoxAndTime(double top, double right, double bottom, double left, String startTime, String endTime);

    List<JSONObject> queryBoxShip(double top, double right, double bottom, double left);

    List<Map<String, Object>> getAllBridgeInfo();

    void seaChart(String type, String x, String y, String z, HttpServletResponse response);

    List<Map<String, Object>> getStationByBox(Double top, Double right, Double bottom, Double left);

    JSONArray getWaterLevelByStationAndTime(String type, String station, String startTime, String endTime);

    Map<String, Object> pageList(String type, int page, int size, String keyword);

    List<Map<String, Object>> getAllStation();

    JSONObject getPrediction(String stationName) throws Exception;
}
