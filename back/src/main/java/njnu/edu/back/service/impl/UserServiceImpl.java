package njnu.edu.back.service.impl;

import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.result.ResultEnum;
import njnu.edu.back.common.utils.CommonUtils;
import njnu.edu.back.common.utils.Encrypt;
import njnu.edu.back.common.utils.JwtTokenUtil;
import njnu.edu.back.common.utils.LocalUploadUtil;
import njnu.edu.back.dao.UserMapper;
import njnu.edu.back.pojo.User;
import njnu.edu.back.pojo.dto.AddUserDTO;
import njnu.edu.back.service.RedisService;
import njnu.edu.back.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/03/28/21:15
 * @Description:
 */
@Service
public class UserServiceImpl implements UserService {

    @Value("${basedir}")
    String baseDir;

    @Autowired
    UserMapper userMapper;

    @Autowired
    RedisService redisService;

    @Override
    public String login(String email, String password) {
        User user = (User) redisService.get(email);
        if(user == null) {
            user = userMapper.getUserByEmail(email);
        }

        password = Encrypt.md5(password);
        if(user != null) {
            if(user.getPassword().equals(password)) {
                redisService.set(email, user, 60*24*7l);
                return JwtTokenUtil.generateTokenByUser(user);
            } else {
                throw new MyException(ResultEnum.USER_PASSWORD_NOT_MATCH);
            }
        } else {
            throw new MyException(ResultEnum.NO_OBJECT);
        }

    }

    @Override
    public int register(@Valid AddUserDTO addUserDTO) {
        User user = userMapper.getUserByEmail(addUserDTO.getEmail());
        if(user == null) {
            addUserDTO.setPassword(Encrypt.md5(addUserDTO.getPassword()));
            return userMapper.addUser(addUserDTO);
        } else {
            throw new MyException(ResultEnum.EXIST_OBJECT);
        }
    }

    @Override
    public User getUserByEmail(String email) {
        User user = (User) redisService.get(email);
        if(user != null)
            return user;
        else {
            user = userMapper.getUserByEmail(email);
            redisService.set(email, user, 60*24*7l);
            return user;
        }
    }

    @Override
    public Map<String, String> setUserInfo(String email, String name, String contactEmail, String occupation, String department, MultipartFile avatar) {
        User user = new User(null, name, email, null, null, contactEmail, null, occupation, department);
        String uuid = UUID.randomUUID().toString();
        String suffix = avatar.getOriginalFilename().substring(avatar.getOriginalFilename().lastIndexOf(".") + 1);
        String path = baseDir + "other\\avatar\\" + uuid + "." + suffix;
        LocalUploadUtil.uploadAvatar(path, avatar);
        String ip = CommonUtils.getIp();
        user.setAvatar("http://" + ip + ":8002/file/avatar/" + uuid + "." + suffix);
        String token = JwtTokenUtil.generateTokenByUser(userMapper.updateUserInfo(user));
        redisService.del(email);
        Map<String, String> result = new HashMap<>();
        result.put("token", token);
        result.put("avatar", "http://" + ip + ":8002/file/avatar/" + uuid + "." + suffix);
        return result;
    }

    @Override
    public String setUserInfoWithoutAvatar(String email, User user) {
        user.setEmail(email);
        String token = JwtTokenUtil.generateTokenByUser(userMapper.updateUserInfoWithoutAvatar(user));
        redisService.del(email);
        return token;
    }
}
