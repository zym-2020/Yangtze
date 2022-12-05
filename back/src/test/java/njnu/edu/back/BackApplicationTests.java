package njnu.edu.back;

import njnu.edu.back.common.utils.CommonUtils;
import njnu.edu.back.pojo.User;
import njnu.edu.back.service.FileService;
import njnu.edu.back.service.ProjectService;
import njnu.edu.back.dao.main.ShpCoordinatesMapper;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SpringBootApplication(exclude = MybatisAutoConfiguration.class)
@SpringBootTest(classes = BackApplicationTests.class)
class BackApplicationTests {

    @Test
    void contextLoads() throws ParseException {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        SimpleDateFormat ymd = new SimpleDateFormat("yyyyMMdd");
        date = sdf.parse("05:00");
        String times;
        String oldTimes;
        if (date.after(sdf.parse("20:00"))) {
            times = ymd.format(new Date()) + "2000";
            oldTimes = ymd.format(new Date()) + "1200";
        } else if (date.after(sdf.parse("12:00"))) {
            times = ymd.format(new Date()) + "1200";
            oldTimes = ymd.format(new Date()) + "0800";
        } else if (date.after(sdf.parse("08:00"))) {
            times = ymd.format(new Date()) + "0800";
            oldTimes = ymd.format(new Date()) + "0600";
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            times = ymd.format(calendar.getTime()) + "0600";
            calendar.add(Calendar.DATE, -1);
            oldTimes = ymd.format(calendar.getTime()) + "2000";
        }
        System.out.println(times);
        System.out.println(oldTimes);
    }

    String number2string(int number) {
        String b = String.valueOf(number / 100);
        number = number % 100;
        String s = String.valueOf(number / 10);
        number = number % 10;
        String result = b + s + number;
        return result;
    }

}
