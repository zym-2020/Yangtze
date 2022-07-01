package njnu.edu.back;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import njnu.edu.back.common.auth.AuthCheck;
import njnu.edu.back.common.utils.*;
import njnu.edu.back.service.FileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class BackApplicationTests {

    @Autowired
    FileService fileService;

    @Test
    void contextLoads() {
        String[] str = new String[] {"-144187208","19335499","-144149073","19354795"};
        byte[] test = RemoteRequest.getShips(str, "15", "0", "78385.16133947353", "1");
        List<Map<String, Object>> maps = TranslateShipResponseUtil.translate(str, "15", "0", "78385.16133947353", "1");
        System.out.println(maps.size());

    }

}
