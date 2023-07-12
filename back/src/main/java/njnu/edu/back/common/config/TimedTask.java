package njnu.edu.back.common.config;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import njnu.edu.back.common.utils.FileUtil;
import njnu.edu.back.common.utils.ProcessUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/12/05/19:15
 * @Description:
 */
@Component
@Slf4j
public class TimedTask {
    @Value("${resourcePath}")
    String resourcePath;

    static String pythonStr = "python";

    @Scheduled(cron = "0 40 * * * ?")
    public void executePrediction() throws IOException, InterruptedException {
        String stationNameJson = resourcePath + "station_name.json";
        String predictionPath = resourcePath + "prediction/";
        Map<String, List<JSONObject>> map = FileUtil.getPredictionStationList(stationNameJson);
        for(List<JSONObject> list : map.values()) {
            for (int i = 0; i < list.size(); i++) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-ddHH");
                String model = predictionPath + list.get(i).getString("name_en") + "/encapsulation.py";
                String output = predictionPath + list.get(i).getString("name_en") + "/result.json";
                String timeParam = simpleDateFormat.format(new Date()) + ":00:00";
                List<String> c = new ArrayList<>();
                c.add("cmd");
                c.add("/c");
                c.add(pythonStr + " " + model + " " + timeParam + " " + output);
                Process process = new ProcessBuilder().command(c).start();
                ProcessUtil.readProcessOutput(process.getInputStream(), System.out);
                int code = process.waitFor();
                process.destroy();

            }
        }
    }

}
