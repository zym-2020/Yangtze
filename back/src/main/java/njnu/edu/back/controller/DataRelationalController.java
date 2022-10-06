package njnu.edu.back.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import njnu.edu.back.common.result.JsonResult;
import njnu.edu.back.common.result.ResultUtils;
import njnu.edu.back.service.DataRelationalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/08/30/10:02
 * @Description:
 */
@RestController
@RequestMapping("/relational")
public class DataRelationalController {
    @Autowired
    DataRelationalService dataRelationalService;

    @CrossOrigin
    @RequestMapping(value = "/addRelational", method = RequestMethod.POST)
    public JsonResult addRelational(@RequestBody JSONObject jsonObject) {
        String dataListId = jsonObject.getString("dataListId");
        List<String> fileIdList = jsonObject.getObject("fileIdList", List.class);
        dataRelationalService.addRelational(dataListId, fileIdList);
        return ResultUtils.success();
    }

    @RequestMapping(value = "/updateRelational", method = RequestMethod.PATCH)
    public JsonResult updateRelational(@RequestBody JSONObject jsonObject) {
        String dataListId = jsonObject.getString("dataListId");
        List<String> fileIdList = jsonObject.getObject("fileIdList", List.class);
        dataRelationalService.updateRelational(dataListId, fileIdList);
        return ResultUtils.success();
    }
}
