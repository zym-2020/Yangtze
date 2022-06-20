package njnu.edu.back.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import njnu.edu.back.common.auth.AuthCheck;
import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.resolver.JwtTokenParser;
import njnu.edu.back.common.result.JsonResult;
import njnu.edu.back.common.result.ResultUtils;
import njnu.edu.back.pojo.dto.AddMessageDTO;
import njnu.edu.back.service.DownloadService;
import njnu.edu.back.service.MessageService;
import njnu.edu.back.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin/message")
public class MessageController {
    @Autowired
    MessageService messageService;
    @Autowired
    UploadService uploadService;
    @Autowired
    DownloadService downloadService;

    @RequestMapping(value = "/addMessage", method = RequestMethod.POST)
    public JsonResult addMessage(@RequestBody AddMessageDTO addMessageDTO,@JwtTokenParser("email") String email) {
        messageService.addMessage(addMessageDTO, addMessageDTO.getFileId(),email);
        return ResultUtils.success();

    }

    @AuthCheck
    @RequestMapping(value = "/pageQuerys/{property}/{flag}/{page}/{size}", method = RequestMethod.GET)
    public JsonResult pageQuerys(@PathVariable String property, @PathVariable boolean flag, @PathVariable int page, @PathVariable int size)
    {
        return ResultUtils.success(messageService.pageQuerys(page, size, property, flag));
    }

    @AuthCheck
    @RequestMapping(value = "/QueryByType/{property}", method = RequestMethod.GET)
    public JsonResult QueryByType(@PathVariable String property) {
        return ResultUtils.success(messageService.QueryByType(property));
    }

    @AuthCheck
    @RequestMapping(value = "/QueryByUser/{property}/{type}", method = RequestMethod.GET)
    public JsonResult QueryByUser(@PathVariable String property, @PathVariable String type) {
        return ResultUtils.success(messageService.QueryByUser(property, type));
    }

    @AuthCheck
    @RequestMapping(value = "/QueryByReceiver/{property}", method = RequestMethod.GET)
    public JsonResult QueryByReceiver(@PathVariable String property) {
        return ResultUtils.success(messageService.QueryByReceiver(property));
    }

    @AuthCheck
    @RequestMapping(value = "/QueryByTime/{property}", method = RequestMethod.GET)
    public JsonResult QueryByTime(@PathVariable String property) {
        return ResultUtils.success(messageService.QueryByTime(property));
    }

    @AuthCheck
    @RequestMapping(value = "/QueryByUserEmail", method = RequestMethod.GET)
    public JsonResult QueryByUserEmail(@JwtTokenParser("email") String email) {
        return ResultUtils.success(messageService.QueryByUserEmail(email));
    }

    @AuthCheck
    @RequestMapping(value = "/QueryByUserType/{property}", method = RequestMethod.GET)
    public JsonResult QueryByUserType(@PathVariable String property ,@JwtTokenParser("email") String email) {
        return ResultUtils.success(messageService.QueryByUserType(property,email));
    }

    @AuthCheck
    @RequestMapping(value = "/offlineMessage/{property}/{dataUploadTime}", method = RequestMethod.GET)
    public JsonResult offlineMessage(@PathVariable String property ,@PathVariable String dataUploadTime ,@JwtTokenParser("email") String email){
        messageService.offlineMessage(property,dataUploadTime,email);
        return ResultUtils.success();
    }

    @AuthCheck
    @RequestMapping(value = "/offlineUserMessage/{property}/{dataUploadTime}", method = RequestMethod.GET)
    public JsonResult offlineUserMessage(@PathVariable String property ,@PathVariable String dataUploadTime ,@JwtTokenParser("email") String email){
        messageService.offlineUserMessage(property,dataUploadTime,email);
        return ResultUtils.success();
    }

    @AuthCheck
    @RequestMapping(value = "/QueryHistoryMessage", method = RequestMethod.GET)
    public JsonResult QueryHistoryMessage(@JwtTokenParser("email") String email) {
        return ResultUtils.success(messageService.QueryHistoryMessage(email));
    }

    @AuthCheck
    @RequestMapping(value = "/QueryAllHistoryMessage", method = RequestMethod.GET)
    public JsonResult QueryAllHistoryMessage() {
        return ResultUtils.success(messageService.QueryAllHistoryMessage());
    }

    @AuthCheck
    @RequestMapping(value = "/showMessageDetails/{property}", method = RequestMethod.GET)
    public JsonResult showMessageDetails(@PathVariable String property) {
        return ResultUtils.success(messageService.showMessageDetails(property));
    }

    @AuthCheck
    @RequestMapping(value = "/responseMessage/{response}/{id}", method = RequestMethod.GET)
    public JsonResult responseMessage(@PathVariable String response ,@PathVariable String id ){
        messageService.responseMessage(response,id);
        return ResultUtils.success();
    }

}
