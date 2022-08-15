package njnu.edu.back.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.result.ResultEnum;
import njnu.edu.back.pojo.support.Point;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/04/20/14:32
 * @Description:
 */

public class AnalyseUtil {

    static String pythonDir = "D:\\zhuomian\\水科院\\python\\";


    public static Process saveSectionValue(String tempPath, String rasterPath, JSONArray jsonArray, String resultPath) throws IOException {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(tempPath));
            out.write(rasterPath + "\n");
            out.write(resultPath + "\n");
            out.write(jsonArray.size() + "\n");
            for(int i = 0; i < jsonArray.size(); i++) {
                out.write(jsonArray.getObject(i, JSONArray.class).getString(0) + "," + jsonArray.getObject(i, JSONArray.class).getString(1) + "\n");
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        } finally {
            if(out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
                }
            }
        }

        ProcessBuilder processBuilder = new ProcessBuilder();
        List<String> commands = new ArrayList<>();
        commands.add("python");
        commands.add(pythonDir + "section.py");
        commands.add(tempPath);
        processBuilder.command(commands);
        return processBuilder.start();
    }

    public static Process createContour(String exePath, String interval, String rasterPath, String resultPath) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        List<String> commands = new ArrayList<>();
        commands.add(exePath + "gdal_contour.exe");
        commands.add("-b");
        commands.add("1");
        commands.add("-a");
        commands.add("DEEP");
        commands.add("-i");
        commands.add(interval);
        commands.add(rasterPath);
        commands.add(resultPath);
        processBuilder.command(commands);
        try {
            Process start = processBuilder.start();
            return start;
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        }
    }

    public static Process uploadShpToDataBase(String exePath, String shpPath, String shpName, String srid) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        List<String> commands = new ArrayList<>();
        commands.add("cmd.exe");
        commands.add("/c");
        commands.add(exePath + "shp2pgsql");
        commands.add("-s");
        commands.add(srid);
        commands.add("-c");
        commands.add(shpPath + "\\" + shpName + ".shp");
        commands.add(shpName);
        commands.add("|");
        commands.add(exePath + "psql");
        commands.add("-U");
        commands.add("postgres");
        commands.add("-d");
        commands.add("shp_data");
        processBuilder.environment().put("PGPASSWORD", "123");
        processBuilder.command(commands);
        try {
            Process process = processBuilder.start();
            return process;
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        }
    }

    private static void showOutput(final InputStream src, final PrintStream dest) {
        new Thread(new Runnable() {
            public void run() {
                Scanner sc = new Scanner(src);
                while (sc.hasNextLine()) {
                    dest.println(sc.nextLine());
                }
            }
        }).start();
    }

    /**
    * @Description:判断生成等深线的四个shp文件是否已经存在
    * @Author: Yiming
    * @Date: 2022/7/19
    */
    public static boolean shpFileIsExist(String path, String fileName) {
        File dbf = new File(path + "\\" + fileName + ".dbf");
        File prj = new File(path + "\\" + fileName + ".prj");
        File shp = new File(path + "\\" + fileName + ".shp");
        File shx = new File(path + "\\" + fileName + ".shx");
        if(dbf.exists() && prj.exists() && shp.exists() && shx.exists()) {
            return true;
        } else {
            return false;
        }
    }

    public static Process rasterCrop(String tempPath, String rasterPath, String outputPng, String outputJson, JSONArray jsonArray) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(tempPath));
            out.write(rasterPath + "\n");
            out.write(outputPng + "\n");
            out.write(outputJson + "\n");
            out.write(jsonArray.getJSONArray(0).size() - 1 + "\n");
            for(int i = 0; i < jsonArray.getJSONArray(0).size() - 1; i++) {
                out.write(jsonArray.getJSONArray(0).getJSONArray(i).getString(0) + "," + jsonArray.getJSONArray(0).getJSONArray(i).getString(1) + "\n");
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        } finally {
            try {
                if(out != null) {
                    out.close();
                }
            }catch (Exception e) {
                e.printStackTrace();
                throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
            }
        }
        ProcessBuilder processBuilder = new ProcessBuilder();
        List<String> commands = new ArrayList<>();
        commands.add("python");
        commands.add(pythonDir + "raster_clip.py");
        commands.add(tempPath);
        processBuilder.command(commands);
        try {
            return processBuilder.start();
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        }
    }

    public static List<Point> getPoints(String outputJson) {
        File file = new File(outputJson);
        if(!file.exists()) {
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        }
        Reader reader = null;
        try {
            reader = new InputStreamReader(new FileInputStream(file), "Utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            reader.close();
            String jsonStr = sb.toString();
            JSONObject jsonObject = JSON.parseObject(jsonStr);
            List<Point> points = new ArrayList<>();
            points.add(new Point(jsonObject.getJSONArray("ul").getDouble(0), jsonObject.getJSONArray("ul").getDouble(1)));
            points.add(new Point(jsonObject.getJSONArray("ur").getDouble(0), jsonObject.getJSONArray("ur").getDouble(1)));
            points.add(new Point(jsonObject.getJSONArray("lr").getDouble(0), jsonObject.getJSONArray("lr").getDouble(1)));
            points.add(new Point(jsonObject.getJSONArray("ll").getDouble(0), jsonObject.getJSONArray("ll").getDouble(1)));
            return points;
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        } finally {
            try {
                if(reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
            }
        }
    }

}
