package njnu.edu.back.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.result.ResultEnum;
import njnu.edu.back.dao.staticdb.*;
import njnu.edu.back.service.MultiSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/12/01/10:46
 * @Description:
 */
@Service
public class MultiSourceServiceImpl implements MultiSourceService {
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
                        } else if (lat > tempTop) {
                            tempTop = lat;
                        } else if (lat < tempBottom) {
                            tempBottom = lat;
                        }
                    }
                    if (!(tempLeft > right || tempRight < left || tempTop < bottom || tempBottom > top)) {
                        result.add(map);
                    }
                } else {
                    double tempTop = jsonArray.getJSONArray(0).getDouble(1);
                    double tempRight = jsonArray.getJSONArray(0).getDouble(0);
                    double tempBottom = jsonArray.getJSONArray(1).getDouble(1);
                    double tempLeft = jsonArray.getJSONArray(1).getDouble(0);
                    if (!(tempLeft > right || tempRight < left || tempTop < bottom || tempBottom > top)) {
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
                } else if (lat > tempTop) {
                    tempTop = lat;
                } else if (lat < tempBottom) {
                    tempBottom = lat;
                }
            }
            if (!(tempLeft > right || tempRight < left || tempTop < bottom || tempBottom > top)) {
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
                } else if (lat > tempTop) {
                    tempTop = lat;
                } else if (lat < tempBottom) {
                    tempBottom = lat;
                }
            }
            if (!(tempLeft > right || tempRight < left || tempTop < bottom || tempBottom > top)) {
                result.add(map);
            }
        }
        return result;
    }
}
