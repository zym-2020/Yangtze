package njnu.edu.back;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import njnu.edu.back.common.auth.AuthCheck;
import njnu.edu.back.common.utils.*;
import njnu.edu.back.service.FileService;
import njnu.edu.back.service.ProjectService;
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
    ProjectService projectService;

    @Test
    void contextLoads() {
        projectService.computeContour("62ba63044bf8161735590782", "82ca348b-b825-4d80-9b69-300b3562c3e3", "123@qq.com", "10", "test");
//        String exePath = "D:\\App\\postgresql\\bin\\";
//        String shpPath = "E:\\Minio\\data\\test\\123@qq.com\\projects\\62ba63044bf8161735590782";
//        String shpName = "test";
//        Process process = AnalyseUtil.uploadShpToDataBase(exePath, shpPath, shpName);
//        try {
//            int code = process.waitFor();
//            System.out.println(code);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

}
