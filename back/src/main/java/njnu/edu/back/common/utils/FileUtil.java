package njnu.edu.back.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.result.ResultEnum;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/02/22/16:15
 * @Description:
 */
@Slf4j
public class FileUtil {
    public static JSONObject readJson(String path) {
        File file = new File(path);
        if (!file.exists()) {
            throw new MyException(ResultEnum.NO_OBJECT);
        }
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path));
            String jsonString = "";
            String line = "";
            while ((line = br.readLine()) != null) {
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
                if (br != null) {
                    br.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
            }
        }
    }

    public static JSONArray readJsonArray(String path) {
        File file = new File(path);
        if (!file.exists()) {
            throw new MyException(ResultEnum.NO_OBJECT);
        }
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path));
            String jsonString = "";
            String line = "";
            while ((line = br.readLine()) != null) {
                jsonString += line;
            }
            br.close();
            JSONArray jsonArray = JSON.parseArray(jsonString);
            return jsonArray;
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
    }

    public static String readTextFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            throw new MyException(ResultEnum.NO_OBJECT);
        }
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path));
            String jsonString = "";
            String line = "";
            while ((line = br.readLine()) != null) {
                jsonString += line;
            }
            br.close();
            return jsonString;
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
    }

    public static void saveFile(String content, String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            write.write(content);
            write.flush();
            write.close();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public static Map<String, List<JSONObject>> getPredictionStationList(String stationJson) {
        JSONArray jsonArray = readJsonArray(stationJson);
        Map<String, List<JSONObject>> map = new HashMap<>();
        List<JSONObject> yangtze = new ArrayList<>();
        List<JSONObject> jiangsu = new ArrayList<>();
        List<JSONObject> zhejiang = new ArrayList<>();
        List<JSONObject> anhui = new ArrayList<>();
        List<JSONObject> hubei = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if (jsonObject.getBoolean("prediction")) {
                switch (jsonObject.getString("type")) {
                    case "yangtze":
                        yangtze.add(jsonObject);
                        break;
                    case "jiangsu":
                        jiangsu.add(jsonObject);
                        break;
                    case "zhejiang":
                        zhejiang.add(jsonObject);
                    case "anhui":
                        anhui.add(jsonObject);
                    case "hubei":
                        hubei.add(jsonObject);
                }
            }
        }
        map.put("yangtze", yangtze);
        map.put("jiangsu", jiangsu);
        map.put("zhejiang", zhejiang);
        map.put("anhui", anhui);
        map.put("hubei", hubei);
        return map;
    }

}
