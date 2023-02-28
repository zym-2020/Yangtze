package njnu.edu.back.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.result.ResultEnum;
import njnu.edu.back.common.utils.Encrypt;
import njnu.edu.back.common.utils.JwtTokenUtil;
import njnu.edu.back.common.utils.LocalUploadUtil;
import njnu.edu.back.dao.main.DataListMapper;
import njnu.edu.back.dao.main.FileMapper;
import njnu.edu.back.dao.main.ProjectMapper;
import njnu.edu.back.dao.main.UserMapper;
import njnu.edu.back.pojo.User;
import njnu.edu.back.service.RedisService;
import njnu.edu.back.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
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

    @Autowired
    ProjectMapper projectMapper;

    @Autowired
    DataListMapper dataListMapper;

    @Autowired
    FileMapper fileMapper;

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
            userMapper.addUser(user);
            return 1;
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
        map.put("department", user.getDepartment());
        map.put("email", user.getEmail());
        map.put("name", user.getName());
        map.put("occupation", user.getOccupation());
        return map;
    }

    @Override
    public Map<String, String> setUserInfo(String email, String name, String occupation, String department, MultipartFile avatar) {
        User user = new User(null, name, email, null, null, "", occupation, department);
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
    public Map<String, Object> getUserInfo(String email) {
        User user = (User) redisService.get(email);
        Map<String, Object> map = new HashMap<>();
        if(user == null) {
            user = userMapper.getUserByEmail(email);
            redisService.set(email, user, 60*24*7l);
        }
        map.put("id", user.getId());
        map.put("name", user.getName());
        map.put("email", user.getEmail());
        map.put("role", user.getRole());
        map.put("avatar", user.getAvatar());
        return map;
    }

    @Override
    public Map<String, Integer> getResourceCount(String email) {
        int fileTotal = fileMapper.getCountByEmail(email);
        int dataListTotal = dataListMapper.countPageQueryByEmail(email, "", "all");
        int projectTotal = projectMapper.fuzzyCountByEmail(email, 0);
        Map<String, Integer> map = new HashMap<>();
        map.put("fileTotal", fileTotal);
        map.put("dataListTotal", dataListTotal);
        map.put("projectTotal", projectTotal);
        return map;
    }

    @Override
    public Map<String, Object> getAllUserInfo(String role, Integer page, Integer size, String keyword) {
        Map<String, Object> result = new HashMap<>();
        if (page == null || size == null) {
            throw new MyException(-99, "参数错误");
        }
        if (!role.equals("admin")) {
            throw new MyException(-99, "没有权限");
        }
        if (keyword != null) {
            keyword = "%" + keyword + "%";
        }
        List<Map<String, Object>> list = userMapper.getAllUserInfo(size, page * size, keyword);
        result.put("list", list);
        result.put("total", userMapper.countAll(keyword));
        return result;
    }

    @Override
    public void resetPassword(String role, String id, String password) {
        if (!role.equals("admin")) {
            throw new MyException(-99, "没有权限");
        }
        password = Encrypt.md5(password);
        String email = userMapper.resetPassword(id, password);
        User user = (User) redisService.get(email);
        if (user != null) {
            user.setPassword(password);
            redisService.set(email, user, 60*24*7l);
        }
    }

    @Override
    public void delete(String id, String role) {
        if (!role.equals("admin")) {
            throw new MyException(-99, "没有权限");
        }
        Map<String, Object> map = userMapper.findById(id);
        if (map.get("role").equals("admin")) {
            throw new MyException(-99, "没有权限");
        }
        userMapper.deleteById(id);
        User user = (User) redisService.get((String) map.get("email"));
        if (user != null) {
            redisService.del((String) map.get("email"));
        }
    }

    @Override
    public void batchDelete(List<String> ids, String role) {
        if (ids.size() == 0) {
            return;
        }
        if (!role.equals("admin")) {
            throw new MyException(-99, "没有权限");
        }
        List<Map<String, Object>> list = userMapper.findByIds(ids);
        for (Map<String, Object> map : list) {
            if (map.get("role").equals("admin")) {
                throw new MyException(-99, "没有权限");
            }
            User user = (User) redisService.get((String) map.get("email"));
            if (user != null) {
                redisService.del((String) map.get("email"));
            }
        }
        userMapper.batchDelete(ids);
    }

    @Override
    public String adminAddUser(MultipartFile file, String jsonString, String role) {
        if (!role.equals("admin")) {
            throw new MyException(-99, "没有权限");
        }
        JSONObject jsonObject = JSON.parseObject(jsonString);
        User temp = userMapper.getUserByEmail(jsonObject.getString("email"));
        if (temp == null) {
            String avatar = "";
            if (!file.isEmpty()) {
                String uuid = UUID.randomUUID().toString();
                String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
                LocalUploadUtil.uploadAvatar(pictureAddress + uuid + "." + suffix, file);
                avatar = uuid + "." + suffix;
            }

            User user = new User(null, jsonObject.getString("name"), jsonObject.getString("email"), Encrypt.md5(jsonObject.getString("password")), "member", avatar, jsonObject.getString("occupation"), jsonObject.getString("department"));
            return userMapper.addUser(user);
        } else {
            throw new MyException(ResultEnum.EXIST_OBJECT);
        }

    }
}
