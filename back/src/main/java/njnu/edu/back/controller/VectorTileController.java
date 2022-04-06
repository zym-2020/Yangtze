package njnu.edu.back.controller;

import njnu.edu.back.common.result.JsonResult;
import njnu.edu.back.common.result.ResultUtils;
import njnu.edu.back.common.utils.TileUtil;
import njnu.edu.back.service.VectorTileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/04/05/16:00
 * @Description:
 */

@RequestMapping("/vector")
@RestController
public class VectorTileController {

    @Autowired
    VectorTileService vectorTileService;


    @RequestMapping(value = "/{tableName}/{x}/{y}/{z}", method = RequestMethod.GET)
    public JsonResult getVectorTile(@PathVariable String tableName, @PathVariable int x, @PathVariable int y, @PathVariable int z) {
        return ResultUtils.success(vectorTileService.getVectorTile(tableName, x, y, z));
    }
}
