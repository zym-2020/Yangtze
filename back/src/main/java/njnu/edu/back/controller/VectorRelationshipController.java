package njnu.edu.back.controller;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import njnu.edu.back.common.auth.AuthCheck;
import njnu.edu.back.common.resolver.JwtTokenParser;
import njnu.edu.back.common.result.JsonResult;
import njnu.edu.back.common.result.ResultUtils;
import njnu.edu.back.common.utils.GeoToolsUtil;
import njnu.edu.back.service.VectorRelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/04/11/20:00
 * @Description:
 */
@RestController
@RequestMapping("/vectorRelationship")
public class VectorRelationshipController {
    @Autowired
    VectorRelationshipService vectorRelationshipService;

    @RequestMapping(value = "/pageQuery/{size}/{page}", method = RequestMethod.GET)
    public JsonResult pageQuery(@PathVariable int size, @PathVariable int page) {
        return ResultUtils.success(vectorRelationshipService.pageQuery(size, page));
    }

    @AuthCheck
    @RequestMapping(value = "/newShape", method = RequestMethod.POST)
    public JsonResult newShape(@RequestBody JSONObject jsonObject, @JwtTokenParser("email") String email) {
        JSONArray jsonArray = jsonObject.getJSONArray("geoJson");
        String fileName = jsonObject.getStr("fileName");
        String type = jsonObject.getStr("type");
        String source = jsonObject.getStr("source");
        String projectName = jsonObject.getStr("projectName");
        String category = jsonObject.getStr("category");
        String meta = jsonObject.getStr("meta");
        return ResultUtils.success(vectorRelationshipService.newShape(jsonArray, fileName, type, email, source, projectName, category, meta));
    }

    @AuthCheck
    @RequestMapping(value = "/checkState/{uuid}", method = RequestMethod.GET)
    public JsonResult checkState(@PathVariable String uuid) {
        return ResultUtils.success(vectorRelationshipService.checkState(uuid));
    }

}
