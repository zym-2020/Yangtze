package njnu.edu.back.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.result.ResultEnum;
import njnu.edu.back.pojo.support.Point;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.channels.FileChannel;
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

@Slf4j
public class AnalyseUtil {

    static String pythonDir = "D:\\zhuomian\\水科院\\python\\";
//    static String pythonDir = "/home/zym/python/";
    static String pythonStr = "python";
//    static String pythonStr = "python3";

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
        log.info(pythonStr + " " + pythonDir + "section.py " + tempPath);
        ProcessBuilder processBuilder = new ProcessBuilder();
        List<String> commands = new ArrayList<>();
        commands.add(pythonStr);
        commands.add(pythonDir + "section.py");
        commands.add(tempPath);
        processBuilder.command(commands);
        return processBuilder.start();
    }

    public static Process savaSectionContrast(String tempPath, List<String> rasterPathList, JSONArray jsonArray, String resultPath) throws IOException {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(tempPath));
            out.write(rasterPathList.size() + "\n");
            for(String path : rasterPathList) {
                out.write(path + "\n");
            }
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
        commands.add(pythonStr);
        commands.add(pythonDir + "SectionContrast.py");
        commands.add(tempPath);
        processBuilder.command(commands);
        return processBuilder.start();

    }

    public static Process sectionFlush(String tempPath, String benchmarkPath, String referPath, String demPath, JSONArray jsonArray, String resultPath) throws IOException {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(tempPath));
            out.write(benchmarkPath + "\n");
            out.write(referPath + "\n");
            out.write(demPath + "\n");
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
        commands.add(pythonStr);
        commands.add(pythonDir + "section_flush.py");
        commands.add(tempPath);
        processBuilder.command(commands);
        return processBuilder.start();
    }


    public static Process computeVolume(String tempPath, double deep, String rasterPath, String resultPath, String visualPath, JSONArray jsonArray) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(tempPath));
            out.write(deep + "\n");
            out.write(jsonArray.getJSONArray(0).size() - 1 + "\n");
            for(int i = 0; i < jsonArray.getJSONArray(0).size() - 1; i++) {
                out.write(jsonArray.getJSONArray(0).getJSONArray(i).getString(0) + "," + jsonArray.getJSONArray(0).getJSONArray(i).getString(1) + "\n");
            }
            out.write(rasterPath + "\n");
            out.write(resultPath + "\n");
            out.write(visualPath + "\n");
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        }
        ProcessBuilder processBuilder = new ProcessBuilder();
        List<String> commands = new ArrayList<>();
        commands.add(pythonStr);
        commands.add(pythonDir + "dem.py");
        commands.add(tempPath);
        processBuilder.command(commands);
        try {
            return processBuilder.start();
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        }
    }



    public static Process rasterCrop(String tempPath, String rasterPath, String outputPng, String outputTif, String outputJson, JSONArray jsonArray) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(tempPath));
            out.write(rasterPath + "\n");
            out.write(outputPng + "\n");
            out.write(outputJson + "\n");
            out.write(outputTif + "\n");
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
        commands.add(pythonStr);
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

    public static void copyFile(String filePath, String destination) {
        File file = new File(filePath);
        if(!file.exists()) {
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        }
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(file).getChannel();
            outputChannel = new FileOutputStream(new File(destination)).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
            outputChannel.close();
            inputChannel.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        }
    }

    public static boolean deleteFolder(String path) {
        File file = new File(path);
        if(!file.exists()) {
            return false;
        } else {
            if(file.isFile()) {
                return deleteFile(path);
            } else {
                return deleteDirectory(path);
            }
        }
    }

    private static boolean deleteFile(String path) {
        File file = new File(path);
        if(file.exists() && file.isFile()) {
            file.delete();
            return true;
        }
        return false;
    }

    private static boolean deleteDirectory(String path) {
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!path.endsWith(File.separator)) {
            path = path + File.separator;
        }
        File dirFile = new File(path);
        //如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        File[] files = dirFile.listFiles();
        for(File f : files) {
            if(f.isFile()) {
                flag = deleteFile(f.getAbsolutePath());
                if(!flag) break;
            } else {
                flag = deleteDirectory(f.getAbsolutePath());
                if(!flag) break;
            }
        }
        if(!flag) return false;

        if(dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }

}
