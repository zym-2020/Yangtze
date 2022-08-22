package njnu.edu.back.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import njnu.edu.back.common.auth.AuthCheck;
import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.resolver.JwtTokenParser;
import njnu.edu.back.common.result.JsonResult;
import njnu.edu.back.common.result.ResultUtils;
import njnu.edu.back.common.utils.PolygonCheck;
import njnu.edu.back.dao.main.ShareFileMapper;
import njnu.edu.back.dao.main.ShpCoordinatesMapper;
import njnu.edu.back.dao.main.TideStationMapper;
import njnu.edu.back.pojo.dto.UpdateShareFileAndFileMetaDTO;
import njnu.edu.back.service.ShareFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/05/17/17:08
 * @Description:
 */
@RestController
@RequestMapping("/share")
public class ShareFileController {
    @Autowired
    ShareFileService shareFileService;

    @Autowired
    ShpCoordinatesMapper shpCoordinatesMapper;

    @Autowired
    TideStationMapper tideStationMapper;

    @Autowired
    ShareFileMapper shareFileMapper;


    @AuthCheck
    @RequestMapping(value = "/getShpCoordinates/{jsonString}", method = RequestMethod.GET)
    public JsonResult getOtherCoordinates(@PathVariable String jsonString) {
        Map<String,Object> map=JSONObject.parseObject(jsonString, Map.class);
        return ResultUtils.success();
    }

    @AuthCheck
    @RequestMapping(value = "/getShpByCoordinates/{arrayString}", method = RequestMethod.GET)
    public JsonResult getShpByCoordinates(@PathVariable String arrayString) {
        //Map<String,Object> map=JSONObject.parseObject(jsonString, Map.class);
        String[] arr=arrayString.split(",");
        List<Map<String, Object>> map = shpCoordinatesMapper.QueryCoordinates();
        List<Map<String, Object>> map2= PolygonCheck.getCoorShp(arr,map);
        return ResultUtils.success(map2);
    }


    @AuthCheck
    @RequestMapping(value = "/QueryCoordinatesByName/{name}", method = RequestMethod.GET)
    public JsonResult QueryCoordinatesByName(@PathVariable String name) {
        //Map<String,Object> map=JSONObject.parseObject(jsonString, Map.class);
        List<Map<String, Object>> lis= shpCoordinatesMapper.QueryCoordinatesByName(name);
        return ResultUtils.success(lis);
    }

    @AuthCheck
    @RequestMapping(value = "/QueryHeightByName/{name}", method = RequestMethod.GET)
    public JsonResult QueryHeightByName(@PathVariable String name) {
        //Map<String,Object> map=JSONObject.parseObject(jsonString, Map.class);
        List<Map<String, Object>> lis= tideStationMapper.QueryHeightByName(name);
        return ResultUtils.success(lis);
    }




}
