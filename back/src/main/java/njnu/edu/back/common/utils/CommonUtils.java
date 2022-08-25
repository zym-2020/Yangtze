package njnu.edu.back.common.utils;

import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.result.ResultEnum;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
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

    public static String getFileSize(long size) {
        String fileSize = "";
        DecimalFormat df = new DecimalFormat("0.0");
        if(size <= 1024) {
            fileSize =  "1.0 KB";
        } else if(size < 1048576) {
            fileSize = df.format(size / 1024) + " KB";
        } else if(size == 1048576) {
            fileSize = "1.0 MB";
        } else if(size > 1048576 && size < 1073741824l) {
            fileSize = df.format(size / (1024 *1024)) + " MB";
        } else if(size > 1048576 && size == 1073741824l) {
            fileSize = "1.0 GB";
        } else if(size > 1073741824 && size < 1099511627776l) {
            fileSize = df.format(size / (1024 * 1024 * 1024)) + " GB";
        } else {
            fileSize = "文件超过1.0 TB";
        }
        return fileSize;
    }
}
