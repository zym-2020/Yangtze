package njnu.edu.back.controller;

import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.xpath.internal.operations.Bool;
import njnu.edu.back.common.auth.AuthCheck;
import njnu.edu.back.common.utils.JsonUtils;
import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.resolver.JwtTokenParser;
import njnu.edu.back.common.result.JsonResult;
import njnu.edu.back.common.result.ResultUtils;
import njnu.edu.back.common.utils.Zip2;
import njnu.edu.back.dao.MessageMapper;
import njnu.edu.back.pojo.ShareFile;
import njnu.edu.back.pojo.dto.AddMessageDTO;
import njnu.edu.back.service.DownloadService;
import njnu.edu.back.service.MessageService;
import njnu.edu.back.service.ShareFileService;
import njnu.edu.back.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/message")
public class MessageController {
    @Autowired
    MessageService messageService;
    @Autowired
    UploadService uploadService;
    @Autowired
    DownloadService downloadService;
    @Autowired
    MessageMapper messageMapper;
    @Autowired
    ShareFileService shareFileService;



    @RequestMapping(value = "/addMessage", method = RequestMethod.POST)
    public JsonResult addMessage(@RequestBody AddMessageDTO addMessageDTO, @JwtTokenParser("email") String email) {
        messageService.addMessage(addMessageDTO, addMessageDTO.getFileId(), email);
        return ResultUtils.success();

    }

    @AuthCheck
    @RequestMapping(value = "/pageQuerys/{property}/{flag}/{page}/{size}", method = RequestMethod.GET)
    public JsonResult pageQuerys(@PathVariable String property, @PathVariable boolean flag, @PathVariable int page, @PathVariable int size) {
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
    public JsonResult QueryByUserType(@PathVariable String property, @JwtTokenParser("email") String email) {
        return ResultUtils.success(messageService.QueryByUserType(property, email));
    }

    @AuthCheck
    @RequestMapping(value = "/offlineMessage/{property}/{dataUploadTime}", method = RequestMethod.GET)
    public JsonResult offlineMessage(@PathVariable String property, @PathVariable String dataUploadTime, @JwtTokenParser("email") String email) {
        messageService.offlineMessage(property, dataUploadTime, email);
        return ResultUtils.success();
    }

    @AuthCheck
    @RequestMapping(value = "/offlineUserMessage/{property}/{dataUploadTime}", method = RequestMethod.GET)
    public JsonResult offlineUserMessage(@PathVariable String property, @PathVariable String dataUploadTime, @JwtTokenParser("email") String email) {
        messageService.offlineUserMessage(property, dataUploadTime, email);
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
    public JsonResult responseMessage(@PathVariable String response, @PathVariable String id) {
        messageService.responseMessage(response, id);
        return ResultUtils.success();

    }

    @CrossOrigin
    @RequestMapping(value = "/responseMessage/{response}", method = RequestMethod.GET)
    public byte[] responseBinary(@PathVariable String response, HttpServletResponse servletResponse) {
        int size = 5;

        ByteBuffer buffer = ByteBuffer.allocate(4 * size);
 //       buffer.putFloat(1210.9F);
//        buffer.putInt(68);
//        buffer.putInt(67);
//        buffer.putInt(69);
        byte[] b = buffer.array();
        return b;

    }

    @AuthCheck
    @RequestMapping(value = "/CountReply", method = RequestMethod.GET)
    public Boolean CountReply() {
        Boolean counts;
        if(messageMapper.CountReply() > 0){
            counts=true;
        }
        else{
            counts=false;
        }
        return counts;
    }

    @AuthCheck
    @RequestMapping(value = "/CountUserReply", method = RequestMethod.GET)
    public Boolean CountUserReply() {
        Boolean counts;
        if(messageMapper.CountUserReply() > 0){
            counts=true;
        }
        else{
            counts=false;
        }
        return counts;
    }

    @AuthCheck
    @RequestMapping(value = "/ZipEntryPath/{path}", method = RequestMethod.GET)
    public List ZipEntryPath(@PathVariable String path) {
        List lis=new ArrayList();
        Map<String, Object> result;
        result=shareFileService.getShareFileById(path);
        ShareFile share=(ShareFile)(result.get("list"));
        String suf= new String(".zip");
        //System.out.println((share.getOriginAddress().substring(share.getOriginAddress().length()-4,share.getOriginAddress().length())));
        if((share.getOriginAddress().substring(share.getOriginAddress().length()-4,share.getOriginAddress().length()).equals(suf) ))
                lis= Zip2.read(share.getOriginAddress());
        return lis;
    }
}
