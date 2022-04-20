package njnu.edu.back.controller;

import cn.hutool.json.JSONObject;
import njnu.edu.back.common.auth.AuthCheck;
import njnu.edu.back.common.resolver.JwtTokenParser;
import njnu.edu.back.common.result.JsonResult;
import njnu.edu.back.common.result.ResultUtils;
import njnu.edu.back.common.utils.JwtTokenUtil;
import njnu.edu.back.dao.UserMapper;
import njnu.edu.back.proj.dto.AddUserDTO;
import njnu.edu.back.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public JsonResult getUserInfoByToken(@JwtTokenParser(value = "name") String name, @JwtTokenParser(value = "email") String email, @JwtTokenParser(value = "roles") String[] roles) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.putOnce("name", name);
        jsonObject.putOnce("email", email);
        jsonObject.putOnce("roles", roles);
        return ResultUtils.success(jsonObject);
    }

}
