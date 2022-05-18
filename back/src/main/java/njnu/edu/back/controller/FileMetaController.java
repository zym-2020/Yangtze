package njnu.edu.back.controller;

import njnu.edu.back.common.auth.AuthCheck;
import njnu.edu.back.common.result.JsonResult;
import njnu.edu.back.common.result.ResultUtils;
import njnu.edu.back.service.FileMetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/05/17/22:26
 * @Description:
 */
@RestController
@RequestMapping("/fileMeta")
public class FileMetaController {
    @Autowired
    FileMetaService fileMetaService;

    @AuthCheck
    @RequestMapping(value = "/getFileMetaById/{id}", method = RequestMethod.GET)
    public JsonResult getFileMetaById(@PathVariable String id) {
        return ResultUtils.success(fileMetaService.getFileMetaById(id));
    }
}
