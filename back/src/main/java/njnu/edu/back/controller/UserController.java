package njnu.edu.back.controller;

import cn.hutool.json.JSONObject;
import njnu.edu.back.common.auth.AuthCheck;
import njnu.edu.back.common.resolver.JwtTokenParser;
import njnu.edu.back.common.result.JsonResult;
import njnu.edu.back.common.result.ResultUtils;
import njnu.edu.back.pojo.User;
import njnu.edu.back.pojo.dto.AddUserDTO;
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
    public JsonResult register(@RequestBody AddUserDTO addUserDTO) {
        return ResultUtils.success(userService.register(addUserDTO));
    }

    @AuthCheck
    @RequestMapping(value = "/getUserInfoByToken", method = RequestMethod.GET)
    public JsonResult getUserInfoByToken(@JwtTokenParser("id") String id, @JwtTokenParser(value = "name") String name, @JwtTokenParser(value = "email") String email, @JwtTokenParser(value = "roles") String[] roles) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.putOnce("id", id);
        jsonObject.putOnce("name", name);
        jsonObject.putOnce("email", email);
        jsonObject.putOnce("roles", roles);
        return ResultUtils.success(jsonObject);
    }

    @AuthCheck
    @RequestMapping(value = "/getUserByEmail", method = RequestMethod.GET)
    public JsonResult getUserByEmail(@JwtTokenParser("email") String email) {
        return ResultUtils.success(userService.getUserByEmail(email));
    }

    @AuthCheck
    @RequestMapping(value = "/setUserInfo", method = RequestMethod.PATCH)
    public JsonResult setUserInfo(@JwtTokenParser("email") String email, @RequestParam MultipartFile avatar, @RequestParam String name, @RequestParam String contactEmail, @RequestParam String occupation, @RequestParam String department) {
        return ResultUtils.success(userService.setUserInfo(email, name, contactEmail,occupation, department, avatar));
    }

    @AuthCheck
    @RequestMapping(value = "/setUserInfoWithoutAvatar", method = RequestMethod.PATCH)
    public JsonResult setUserInfoWithoutAvatar(@JwtTokenParser("email") String email, @RequestBody User user) {
        return ResultUtils.success(userService.setUserInfoWithoutAvatar(email, user));
    }

}
