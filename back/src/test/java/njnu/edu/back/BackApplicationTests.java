package njnu.edu.back;

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
//        projectService.computeContour("62ba63044bf8161735590782", "82ca348b-b825-4d80-9b69-300b3562c3e3", "123@qq.com", "10", "test");
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
    @Test
    void ZipTest() {
        String[] arrayString=new String[]{"120.94504780070741","31.8827951581777","121.75976706971329"
                ,"31.577425323748756","121.42242190482239","31.556917644841192","121.4103872635423"
                ,"31.889607558842187","120.94504780070741","31.8827951581777"
        };
        List<Map<String, Object>> map = shpCoordinatesMapper.QueryCoordinates();
        List<Map<String, Object>> map2=PolygonCheck.getCoorShp(arrayString,map);
        System.out.println(map2);

        //List lie=new List()
    }

//    @Test
//    void ZipTest(){
//        List lis;
//        //lis= Zip2.read("E:\\水科院\\数据\\辅助数据\\Background02.zip");
//        Map<String, Object> result = new HashMap<>();
//        result=shareFileService.getShareFileById("e814fbc0-c8d1-40d1-943a-cc6038ae855f");
//        ShareFile share=(ShareFile)(result.get("list"));
//        System.out.println(share.getOriginAddress());
//        lis= Zip2.read(share.getOriginAddress());
//        System.out.println(lis);
//    }

}
