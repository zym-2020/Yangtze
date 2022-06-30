package njnu.edu.back.controller;

import njnu.edu.back.common.auth.AuthCheck;
import njnu.edu.back.common.result.JsonResult;
import njnu.edu.back.common.result.ResultUtils;
import njnu.edu.back.service.ShipBinaryService;
import njnu.edu.back.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.nio.ByteBuffer;

@RestController
@RequestMapping("/admin/message")
public class ShipBinaryController {
    @Autowired
    ShipBinaryService shipBinaryService;

    @Autowired
    ShipService shipService;

    @CrossOrigin
    @RequestMapping(value = "/responseBinary/{response}", method = RequestMethod.GET)
    public byte[] responseBinary(@PathVariable String response, HttpServletResponse servletResponse) {
            int size = 3;
            int field =7;
            int intArray[]  =new int [3];
            int a[]={1,2,3,4,5,6,7,8,9,10,11,12,13,14};
        byte[] b =shipBinaryService.getShipBinary(2,field,a);
        byte[] b2=shipBinaryService.getShipBinary2(2,field,b);
            return b2;
    }

    @AuthCheck
    @RequestMapping(value = "/QueryByMsi/{mmsi}", method = RequestMethod.GET)
    public JsonResult QueryByMsi(@PathVariable int mmsi) {
        return ResultUtils.success(shipService.QueryByMsi(mmsi));
    }

}
