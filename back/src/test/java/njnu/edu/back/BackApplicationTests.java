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
import java.util.List;
import java.util.Map;

@SpringBootTest
class BackApplicationTests {

    @Autowired
    FileService fileService;

    @Test
    void contextLoads() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.putOnce("files", new ArrayList<String>());
        List<String> lists = new ArrayList<>();

        lists.add("b070d37d-702f-424b-8cb1-a0f291513893");
        lists.add("49699ef0-39e0-4e86-b4ec-5b4f961adaa7");
        jsonObject.putOnce("folders", lists);

        fileService.compressFile(jsonObject, "E:\\Minio\\data\\test\\123@qq.com\\upload\\test.zip");

    }

}
