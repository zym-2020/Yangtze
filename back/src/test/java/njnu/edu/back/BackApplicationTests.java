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
        System.out.println(sdf.format(date));

        Date date1 = sdf.parse(sdf.format(date));
        Date date2 = sdf.parse("09:04");
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        if (cal1.after(cal2)) {
            System.out.println("hahah");
        }

    }

}
