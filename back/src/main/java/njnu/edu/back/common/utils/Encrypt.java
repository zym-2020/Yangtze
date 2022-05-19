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

    /**
    * @Description:仅仅适用于text与id相同长度
    * @Author: Yiming
    * @Date: 2022/5/19
    */

    public static String encryptByUserId(String text, String id, char[] key) {
        String temp = "";

        char[] chars1 = text.toCharArray();
        char[] chars2 = id.toCharArray();
        for(int i = 0; i < text.length(); i++) {
            int a = (chars1[i] + chars2[i]) % 16;
            int b = (chars1[i] + chars2[i]) / 16;
            temp += key[a];
            temp += key[b];
        }
        return temp;
    }
}
