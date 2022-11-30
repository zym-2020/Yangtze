package njnu.edu.back.controller;

import cn.hutool.json.JSONObject;
import njnu.edu.back.common.auth.AuthCheck;
import njnu.edu.back.common.resolver.JwtTokenParser;
import njnu.edu.back.common.result.JsonResult;
import njnu.edu.back.common.result.ResultUtils;
import njnu.edu.back.pojo.User;
import njnu.edu.back.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/03/28/21:40
 * @Description:
 */

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public JsonResult login(@RequestBody JSONObject jsonObject) {
        return ResultUtils.success(userService.login(jsonObject.getStr("email"), jsonObject.getStr("password")));
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public JsonResult register(@RequestBody User user) {
        return ResultUtils.success(userService.register(user));
    }

    @AuthCheck
    @RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
    public JsonResult getUserInfo(@JwtTokenParser("email") String email) {
        return ResultUtils.success(userService.getUserInfo(email));
    }

    @AuthCheck
    @RequestMapping(value = "/getUserByEmail", method = RequestMethod.GET)
    public JsonResult getUserByEmail(@JwtTokenParser("email") String email) {
        return ResultUtils.success(userService.getUserByEmail(email));
    }

    @AuthCheck
    @RequestMapping(value = "/setUserInfo", method = RequestMethod.PATCH)
    public JsonResult setUserInfo(@JwtTokenParser("email") String email, @RequestParam MultipartFile avatar, @RequestParam String name, @RequestParam String occupation, @RequestParam String department) {
        return ResultUtils.success(userService.setUserInfo(email, name, occupation, department, avatar));
    }

    @AuthCheck
    @RequestMapping(value = "/getResourceCount", method = RequestMethod.GET)
    public JsonResult getResourceCount(@JwtTokenParser("email") String email) {
        return ResultUtils.success(userService.getResourceCount(email));
    }


}
