package njnu.edu.back.service.impl;

import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.result.ResultEnum;
import njnu.edu.back.common.utils.Encrypt;
import njnu.edu.back.common.utils.JwtTokenUtil;
import njnu.edu.back.dao.UserMapper;
import njnu.edu.back.pojo.User;
import njnu.edu.back.pojo.dto.AddUserDTO;
import njnu.edu.back.service.RedisService;
import njnu.edu.back.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/03/28/21:15
 * @Description:
 */
@Service
public class UserServiceImpl implements UserService {

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
}
