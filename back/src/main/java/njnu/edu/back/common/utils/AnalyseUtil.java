package njnu.edu.back.common.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/04/20/14:32
 * @Description:
 */

public class AnalyseUtil {

    static String pythonDir = "D:\\zhuomian\\水科院\\python\\";


    public static Process saveSectionValue(String rasterPath, String lon1, String lat1, String lon2, String lat2, String resultPath) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        List<String> commands = new ArrayList<>();
        commands.add("python");
        commands.add(pythonDir + "section.py");
        commands.add(rasterPath);
        commands.add(lon1);
        commands.add(lat1);
        commands.add(lon2);
        commands.add(lat2);
        commands.add(resultPath);
        processBuilder.command(commands);
        return processBuilder.start();
    }

    public static Process saveSectionContrast(String lat1, String lon1, String lat2, String lon2, String resultPath, List<String> address, List<String> name) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(resultPath + ".txt"));
        out.write(lat1 + " " + lon1 + " " + lat2 + " " + lon2 + "\n");
        out.write(resultPath + "\n");
        for (int i = 0; i < address.size(); i++) {
            if(i < address.size() - 1) {
                out.write(address.get(i) + " ");
            } else {
                out.write(address.get(i) + "\n");
            }
        }
        for(String str : name) {
            out.write(str + " ");
        }
        out.close();
        ProcessBuilder processBuilder = new ProcessBuilder();
        List<String> commands = new ArrayList<>();
        commands.add("python");
        commands.add(pythonDir + "SectionContrast.py");
        commands.add(resultPath + ".txt");
        processBuilder.command(commands);
        return processBuilder.start();
    }
}
