package njnu.edu.back;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import njnu.edu.back.common.auth.AuthCheck;
import njnu.edu.back.common.utils.CommonUtils;
import njnu.edu.back.common.utils.PolygonCheck;
import njnu.edu.back.common.utils.Zip2;
import njnu.edu.back.common.utils.ZipOperate;
import njnu.edu.back.dao.ShpCoordinatesMapper;
import njnu.edu.back.pojo.ShareFile;
import njnu.edu.back.service.FileService;
import njnu.edu.back.service.ShareFileService;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URLDecoder;
import java.util.*;

@SpringBootApplication(exclude = MybatisAutoConfiguration.class)
@SpringBootTest(classes = BackApplicationTests.class)
class BackApplicationTests {

    @Autowired
    FileService fileService;
    @Autowired
    ShareFileService shareFileService;
    @Autowired
    ShpCoordinatesMapper shpCoordinatesMapper;
//    @Autowired
//    ShareFile shareFile;

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
