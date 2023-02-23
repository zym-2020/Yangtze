package njnu.edu.back;

import njnu.edu.back.common.utils.CommonUtils;
import njnu.edu.back.common.utils.InternetUtil;
import njnu.edu.back.common.utils.ProcessUtil;
import njnu.edu.back.common.utils.ZipOperate;
import njnu.edu.back.dao.ship.LocusMapper;
import njnu.edu.back.pojo.User;
import njnu.edu.back.service.FileService;
import njnu.edu.back.service.MultiSourceService;
import njnu.edu.back.service.ProjectService;
import njnu.edu.back.dao.main.ShpCoordinatesMapper;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@SpringBootApplication(exclude = MybatisAutoConfiguration.class)
@SpringBootTest(classes = BackApplicationTests.class)
class BackApplicationTests {


    @Test
    void contextLoads() throws Exception {

    }

}
