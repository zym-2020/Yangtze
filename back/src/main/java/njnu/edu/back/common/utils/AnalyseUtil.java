package njnu.edu.back.common.utils;

import com.alibaba.fastjson.JSONArray;
import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.result.ResultEnum;
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

    public static Process uploadShpToDataBase(String exePath, String shpPath, String shpName) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        List<String> commands = new ArrayList<>();
        commands.add("cmd.exe");
        commands.add("/c");
        commands.add(exePath + "shp2pgsql");
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
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            String line;
//            while ((line = bufferedReader.readLine()) != null) {
//                System.out.println(line);
//            }
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

}
