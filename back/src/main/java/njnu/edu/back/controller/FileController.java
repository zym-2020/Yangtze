package njnu.edu.back.controller;

import njnu.edu.back.common.resolver.JwtTokenParser;
import njnu.edu.back.common.result.JsonResult;
import njnu.edu.back.common.result.ResultUtils;
import njnu.edu.back.proj.dto.AddFileDTO;
import njnu.edu.back.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/05/02/15:48
 * @Description:
 */
@RestController
@RequestMapping("/file")
public class FileController {
    @Autowired
    FileService fileService;

    @RequestMapping(value = "/addFile", method = RequestMethod.POST)
    public JsonResult addFile(@RequestBody AddFileDTO addFileDTO, @JwtTokenParser("email") String email) {
        fileService.addFile(addFileDTO, email);
        return ResultUtils.success();
    }

    @RequestMapping(value = "/findByLevel/{level}", method = RequestMethod.GET)
    public JsonResult findByLevel(@PathVariable int level) {
        return ResultUtils.success(fileService.findByLevel(level));
    }

    @RequestMapping(value = "/findByParentId/{parentId}", method = RequestMethod.GET)
    public JsonResult findByParentId(@PathVariable String parentId) {
        return ResultUtils.success(fileService.findByParentId(parentId));
    }

    @RequestMapping(value = "/getNoUpload/{MD5}/{total}", method = RequestMethod.GET)
    public JsonResult getNoUpload(@PathVariable String MD5, @JwtTokenParser("email") String email, @PathVariable int total) {
        return ResultUtils.success(fileService.getNoUpload(MD5, email, total));
    }
}
