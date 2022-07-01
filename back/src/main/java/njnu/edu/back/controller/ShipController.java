package njnu.edu.back.controller;

import njnu.edu.back.common.utils.RemoteRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/06/22/23:06
 * @Description:
 */
@RestController
@RequestMapping("/ship")
public class ShipController {

//    @CrossOrigin
//    @RequestMapping(value = "/getShip", method = RequestMethod.GET)
//    public byte[] getShip() {
////        String[] str = new String[] {"-144187208","19335499","-144149073","19354795"};
////        return RemoteRequest.getShips(str, "15", "0", "78385.16133947353", "1");
//        byte[] b = "   A   B".getBytes();
//        System.out.println(b.length);
//        return b;
//    }
    @CrossOrigin
    @RequestMapping(value = "/getShip", method = RequestMethod.GET)
    public void getShip(HttpServletResponse response) {
//        String[] str = new String[] {"-144187208","19335499","-144149073","19354795"};
//        return RemoteRequest.getShips(str, "15", "0", "78385.16133947353", "1");
        byte[] a = new byte[8];
        a[3] = 'A';
        a[7] = 'B';
//        byte[] b = "   A   B".getBytes();
        System.out.println(a.length);
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(a);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
