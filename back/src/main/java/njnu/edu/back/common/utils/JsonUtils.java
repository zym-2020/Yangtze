package njnu.edu.back.common.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.util.Base64;


public class JsonUtils {
    public static void jsonArray2byte(JSONArray jsonArray){
        byte[] byte1=JSONArray.parseObject(jsonArray.toJSONString(),byte[].class);
        String jar=JSONArray.toJSONString(byte1);
        System.out.println(jar);


    }
    // int转byte
    public static byte[] intToBytes(int i) {
        byte[] result = new byte[4];
        result[0] = (byte)((i >> 24) & 0xFF);
        result[1] = (byte)((i >> 16) & 0xFF);
        result[2] = (byte)((i >> 8) & 0xFF);
        result[3] = (byte)(i & 0xFF);
        return result;
    }

}
