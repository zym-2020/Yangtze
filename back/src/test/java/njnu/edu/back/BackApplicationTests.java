package njnu.edu.back;

import njnu.edu.back.common.utils.CommonUtils;
import njnu.edu.back.service.FileService;
import njnu.edu.back.service.ProjectService;
import njnu.edu.back.common.utils.PolygonCheck;
import njnu.edu.back.dao.main.ShpCoordinatesMapper;
import njnu.edu.back.service.ShareFileService;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootApplication(exclude = MybatisAutoConfiguration.class)
@SpringBootTest(classes = BackApplicationTests.class)
class BackApplicationTests {

    @Autowired
    ProjectService projectService;
    FileService fileService;
    @Autowired
    ShareFileService shareFileService;
    @Autowired
    ShpCoordinatesMapper shpCoordinatesMapper;
//    @Autowired
//    ShareFile shareFile;

    @Test
    void contextLoads() {
        String str = CommonUtils.getRandomCharStr(8);
        System.out.println(str);
    }

}
