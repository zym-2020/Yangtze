package njnu.edu.back.controller;

import njnu.edu.back.common.auth.AuthCheck;
import njnu.edu.back.common.resolver.JwtTokenParser;
import njnu.edu.back.common.result.JsonResult;
import njnu.edu.back.common.result.ResultUtils;
import njnu.edu.back.pojo.UploadRecord;
import njnu.edu.back.service.UploadRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/06/01/22:25
 * @Description:
 */
@RestController
@RequestMapping("/uploadRecord")
public class UploadRecordController {
    @Autowired
    UploadRecordService uploadRecordService;

    @AuthCheck
    @RequestMapping(value = "/addRecord", method = RequestMethod.POST)
    public JsonResult addRecord(@RequestBody UploadRecord uploadRecord, @JwtTokenParser("email") String email) {
        uploadRecordService.addRecord(uploadRecord, email);
        return ResultUtils.success();
    }

    @AuthCheck
    @RequestMapping(value = "/getRecords", method = RequestMethod.GET)
    public JsonResult getRecords(@JwtTokenParser("email") String email) {
        return ResultUtils.success(uploadRecordService.getRecords(email));
    }

    @AuthCheck
    @RequestMapping(value = "/delRecord/{id}", method = RequestMethod.DELETE)
    public JsonResult delRecord(@PathVariable String id) {
        uploadRecordService.delRecord(id);
        return ResultUtils.success();
    }
}
