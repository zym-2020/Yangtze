package njnu.edu.back.common.utils;

import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.result.ResultEnum;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/05/11/20:47
 * @Description:
 */
public class CommonUtils {

    public static String getIp() {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            return addr.getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(-1, "获取ip错误");
        }

    }

    /**
    * @Description:获取几天前或几天后日期
    * @Author: Yiming
    * @Date: 2022/5/31
    */

    public static String getDate(int number) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, number);
        date = calendar.getTime();
        return sdf.format(date);
    }

    /**
    * @Description:GBK编码转utf-8
    * @Author: Yiming
    * @Date: 2022/6/10
    */
    public static String changeCharSet(String str) {
        try {
            if(str != null) {
                return new String(getUTF8BytesFromGBKString(str), "UTF-8");
            }
            return str;
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(-1, "错误的编码格式");
        }
    }

    private static byte[] getUTF8BytesFromGBKString(String gbkStr) {
        int n = gbkStr.length();
        byte[] utfBytes = new byte[3 * n];
        int k = 0;
        for (int i = 0; i < n; i++) {
            int m = gbkStr.charAt(i);
            if (m < 128 && m >= 0) {
                utfBytes[k++] = (byte) m;
                continue;
            }
            utfBytes[k++] = (byte) (0xe0 | (m >> 12));
            utfBytes[k++] = (byte) (0x80 | ((m >> 6) & 0x3f));
            utfBytes[k++] = (byte) (0x80 | (m & 0x3f));
        }
        if (k < utfBytes.length) {
            byte[] tmp = new byte[k];
            System.arraycopy(utfBytes, 0, tmp, 0, k);
            return tmp;
        }
        return utfBytes;
    }

    public static String getRandomCharStr(int n) {
        String codes = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuilder randomStr = new StringBuilder();
        for (int i = 0; i < n; i++) {
            randomStr.append(codes.charAt(random.nextInt(62)));
        }
        return randomStr.toString();
    }

}
