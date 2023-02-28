package njnu.edu.back;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootApplication(exclude = MybatisAutoConfiguration.class)
@SpringBootTest(classes = BackApplicationTests.class)
class BackApplicationTests {


    @Test
    void contextLoads() throws Exception {

    }

}
