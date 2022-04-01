package njnu.edu.back.common.utils;

import njnu.edu.back.common.exception.MyException;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/04/01/10:24
 * @Description:
 */
public class Encrypt {

    /**
    * @Description:md5加密
    * @Author: Yiming
    * @Date: 2022/4/1
    */
    public static String md5(String pwd) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(pwd.getBytes());   //计算MD5函数

            return new BigInteger(1, messageDigest.digest()).toString(16);          //转成16进制数
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new MyException(-99, "md5加密出错!");
        }
    }
}
