package njnu.edu.back.service.impl;

import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.result.ResultEnum;
import njnu.edu.back.common.utils.Encrypt;
import njnu.edu.back.common.utils.JwtTokenUtil;
import njnu.edu.back.common.utils.LocalUploadUtil;
import njnu.edu.back.dao.main.UserMapper;
import njnu.edu.back.pojo.User;
import njnu.edu.back.service.RedisService;
import njnu.edu.back.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    @Value("${basePath}")
    String basePath;

    @Value("${pictureAddress}")
    String pictureAddress;

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
    public int register(User user) {
        User temp = userMapper.getUserByEmail(user.getEmail());
        if(temp == null) {
            user.setPassword(Encrypt.md5(user.getPassword()));
            LocalUploadUtil.createUserFolder(basePath, user.getEmail());
            user.setRole("member");
            return userMapper.addUser(user);
        } else {
            throw new MyException(ResultEnum.EXIST_OBJECT);
        }
    }

    @Override
    public Map<String, Object> getUserByEmail(String email) {
        User user = (User) redisService.get(email);
        if(user != null)
            return formatUser(user);
        else {
            user = userMapper.getUserByEmail(email);
            redisService.set(email, user, 60*24*7l);
            return formatUser(user);
        }
    }

    private Map<String, Object> formatUser(User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", user.getId());
        map.put("avatar", user.getAvatar());
        map.put("contactEmail", user.getContactEmail());
        map.put("department", user.getDepartment());
        map.put("email", user.getEmail());
        map.put("name", user.getName());
        map.put("occupation", user.getOccupation());
        return map;
    }

    @Override
    public Map<String, String> setUserInfo(String email, String name, String contactEmail, String occupation, String department, MultipartFile avatar) {
        User user = new User(null, name, email, null, null, contactEmail, "", occupation, department);
        Map<String, String> result = new HashMap<>();
        if(!avatar.isEmpty()) {
            String uuid = UUID.randomUUID().toString();
            String suffix = avatar.getOriginalFilename().substring(avatar.getOriginalFilename().lastIndexOf(".") + 1);
            String path = pictureAddress + uuid + "." + suffix;
            LocalUploadUtil.uploadAvatar(path, avatar);
            user.setAvatar(uuid + "." + suffix);
        }
        User resultUser = userMapper.updateUserInfo(user);
        String token = JwtTokenUtil.generateTokenByUser(resultUser);
        redisService.del(email);
        result.put("token", token);
        result.put("avatar", resultUser.getAvatar());
        return result;
    }

    @Override
    public String getAvatarURL(String email) {
        return userMapper.getAvatarURL(email);
    }
}
