package njnu.edu.back.common.utils;

import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.result.ResultEnum;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
}
