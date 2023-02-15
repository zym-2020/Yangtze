package njnu.edu.back.common.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import njnu.edu.back.dao.staticdb.StationMapper;
import njnu.edu.back.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/12/05/19:15
 * @Description:
 */
@Component
public class TimedTask {

    @Autowired
    RedisService redisService;

    @Autowired
    StationMapper stationMapper;

    private JSONObject weatherHttpOperate(String times, String station) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            HttpEntity httpEntity = new HttpEntity(headers);
            String url = String.format("http://api.data.pmsc.cn/esUdi/getPmscGsocf1h?userId=nanjing_nnu&pwd=jPUN2KxMOeYrbdQB&dataCode=PMSC_GSOCF1H&interfaceId=getPmscGsocf1h&times=%s&elements=*&STATION=%s", times, station);
            ResponseEntity<JSONObject> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, JSONObject.class);
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    @Scheduled(cron = "0 30 6,8,12,20 * * ?")
//    public void executeWeather() throws ParseException {
//        System.out.println("haha");
//        List<String> ids = stationMapper.getAllId();
//        Date date = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
//        SimpleDateFormat ymd = new SimpleDateFormat("yyyyMMdd");
//        date = sdf.parse(sdf.format(date));
//        String times;
//        String oldTimes;
//        if (date.after(sdf.parse("20:00"))) {
//            times = ymd.format(new Date()) + "2000";
//            oldTimes = ymd.format(new Date()) + "1200";
//        } else if (date.after(sdf.parse("12:00"))) {
//            times = ymd.format(new Date()) + "1200";
//            oldTimes = ymd.format(new Date()) + "0800";
//        } else if (date.after(sdf.parse("08:00"))) {
//            times = ymd.format(new Date()) + "0800";
//            oldTimes = ymd.format(new Date()) + "0600";
//        } else {
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(new Date());
//            times = ymd.format(calendar.getTime()) + "0600";
//            calendar.add(Calendar.DATE, -1);
//            oldTimes = ymd.format(calendar.getTime()) + "2000";
//        }
//        JSONArray jsonArray = (JSONArray) redisService.get(oldTimes);
//        JSONArray result = new JSONArray();
//        String station = "";
//        for (int i = 0; i < ids.size(); i++) {
//            if (i % 20 == 0) {
//                station = station + ids.get(i);
//            } else {
//                station = station + "," + ids.get(i);
//            }
//            if ((i + 1) % 20 == 0) {
//                JSONObject jsonObject = weatherHttpOperate(times, station);
//                if (jsonObject != null && jsonObject.getIntValue("returnCode") == 0) {
//                    result.addAll(jsonObject.getJSONArray("DS").getJSONObject(0).getJSONArray("data"));
//                    station = "";
//                } else {
//                    redisService.set(times, jsonArray, 60*25l);
//                    return;
//                }
//            }
//        }
//        redisService.set(times, result, 60*23l);
//    }
}
