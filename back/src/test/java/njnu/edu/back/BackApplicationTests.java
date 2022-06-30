package njnu.edu.back;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import njnu.edu.back.common.auth.AuthCheck;
import njnu.edu.back.common.utils.CommonUtils;
import njnu.edu.back.common.utils.ZipOperate;
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
        List<Map<String, Object>> fileList = new ArrayList<>();
        Map<String, Object> temp1 = new HashMap<>();
        Map<String, Object> temp2 = new HashMap<>();
        temp1.put("path", "test/test.xml ");
        temp1.put("address", "D:\\zhuomian\\test.xml");
        fileList.add(temp1);
        ZipOperate.compressFile("D:\\zhuomian\\test.zip", fileList);

    }

}
