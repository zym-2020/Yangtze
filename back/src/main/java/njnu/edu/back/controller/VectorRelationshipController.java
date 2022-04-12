package njnu.edu.back.controller;

import njnu.edu.back.common.result.JsonResult;
import njnu.edu.back.common.result.ResultUtils;
import njnu.edu.back.service.VectorRelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
}
