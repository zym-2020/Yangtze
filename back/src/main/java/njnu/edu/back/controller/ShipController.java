package njnu.edu.back.controller;

import njnu.edu.back.common.auth.AuthCheck;
import njnu.edu.back.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static njnu.edu.back.common.utils.Byte2btye.getShipBinary;

@RestController
@RequestMapping("/ship")
public class ShipController {
    @Autowired
    ShipService shipService;

    @CrossOrigin
    @RequestMapping(value = "/QueryCode", method = RequestMethod.GET)
    public byte[] QueryCode() {
        return (getShipBinary(shipService.QueryCode()));
    }
}
