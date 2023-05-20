package njnu.edu.back.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.result.ResultEnum;

import java.io.*;

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

}
