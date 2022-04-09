package njnu.edu.back.controller;

import cn.hutool.json.JSONObject;
import io.minio.errors.*;
import njnu.edu.back.common.result.JsonResult;
import njnu.edu.back.common.result.ResultUtils;
import njnu.edu.back.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/04/07/11:30
 * @Description:
 */

@RequestMapping("/upload")
@RestController
public class UploadController {
    @Autowired
    UploadService uploadService;

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public JsonResult uploadFile(@RequestParam MultipartFile file) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        System.out.println(file.getSize());
        uploadService.uploadFile(file);
        return ResultUtils.success();
    }

    @RequestMapping(value = "/uploadMultipartFile", method = RequestMethod.POST)
    public JsonResult uploadMultipartFile(@RequestBody JSONObject jsonObject) throws ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, IOException, InvalidKeyException, XmlParserException, InvalidResponseException, InternalException {
        return ResultUtils.success(uploadService.getUploadId(null, jsonObject.getStr("objectName"), null, null, jsonObject.getInt("totalPart"), jsonObject.getStr("md5"), "123"));
    }

    @RequestMapping(value = "/mergeMultipartUpload", method = RequestMethod.POST)
    public JsonResult mergeMultipartUpload(@RequestBody JSONObject jsonObject) throws ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, IOException, InvalidKeyException, XmlParserException, InvalidResponseException, InternalException {
        return ResultUtils.success(uploadService.mergeMultipartUpload(jsonObject.getStr("objectName"), jsonObject.getStr("upload"), jsonObject.getInt("totalPart"), jsonObject.getStr("md5"), "123"));
    }

    @RequestMapping(value = "/checkMergeStatus/{uuid}", method = RequestMethod.GET)
    public JsonResult checkMergeStatus(@PathVariable String uuid) {
        return ResultUtils.success(uploadService.checkMergeStatus(uuid));
    }
}
