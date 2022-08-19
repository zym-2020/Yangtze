package njnu.edu.back.controller;

import njnu.edu.back.common.auth.AuthCheck;
import njnu.edu.back.common.result.JsonResult;
import njnu.edu.back.common.result.ResultUtils;
import njnu.edu.back.dao.shp.JsonRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jsonRecord")
public class JsonRecordController {
    @Autowired
    JsonRecordMapper jsonRecordMapper;
@CrossOrigin
    @AuthCheck
    @RequestMapping(value="/QueryByName/{name}",method= RequestMethod.GET)
    public JsonResult QueryByName(@PathVariable  String name){
        return ResultUtils.success(jsonRecordMapper.QueryByName(name));

    }

    @CrossOrigin
    @AuthCheck
    @RequestMapping(value="/QueryByNewName/{name}",method= RequestMethod.GET)
    public JsonResult QueryByNewName(@PathVariable  String name){
        return ResultUtils.success(jsonRecordMapper.QueryByName(name));

    }




}
