package njnu.edu.back.common.utils;

import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.result.ResultEnum;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/06/22/17:34
 * @Description:
 */
public class RemoteRequest {

    /**
    * @Description:查询船只位置信息
    * @Author: Yiming
    * @Date: 2022/6/22
    */
    public static byte[] getShips(String[] bbox, String zoom, String mmsi, String ref, String showName) {
        String url = "https://www.vesselfinder.com/api/pub/mp2" + getUrl(bbox, zoom, mmsi, ref, showName);
        try {
            URL serverUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) serverUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            if(conn.getResponseCode() == 200) {
                return IOUtils.toByteArray(conn.getInputStream());
            } else {
                throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        }

    }

    private static String getUrl(String[] bbox, String zoom, String mmsi, String ref, String showName) {
        return "?bbox=" + bbox[0] + "%2C" + bbox[1] + "%2C" + bbox[2] + "%2C" + bbox[3] + "&zoom=" + zoom + "&mmsi=" + mmsi + "&ref=" + ref + "&show_names=" + showName;
    }

}
