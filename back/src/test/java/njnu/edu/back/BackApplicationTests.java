package njnu.edu.back;

import njnu.edu.back.common.utils.CommonUtils;
import njnu.edu.back.dao.ship.LocusMapper;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@SpringBootApplication(exclude = MybatisAutoConfiguration.class)
@SpringBootTest(classes = BackApplicationTests.class)
class BackApplicationTests {

    @Autowired
    LocusMapper locusMapper;

    @Test
    void contextLoads() throws ParseException, ExecutionException, InterruptedException {
        System.out.println(locusMapper.existTable("locus20221206"));
//        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            return 1;
//        });
//        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() -> {
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            return 2;
//        });
//        int b = completableFuture2.get();
//        System.out.println(b);
//        int a = completableFuture1.get();
//        System.out.println(a);
//
//        System.out.println(a + b);
    }

}
