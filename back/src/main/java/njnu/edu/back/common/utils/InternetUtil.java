package njnu.edu.back.common.utils;

import com.alibaba.fastjson.JSONObject;
import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.result.ResultEnum;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/02/22/23:08
 * @Description:
 */
public class InternetUtil {

    /**
    * @Description:发送https形式请求
    * @Author: Yiming
    * @Date: 2023/2/22
    */

    public  static String doPost(String url, String map, String charset) {
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        try {
            httpClient = new SSLClient();
            httpPost = new HttpPost(url);
            //设置参数
            httpPost.addHeader("Accept", "application/json");
            httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");
            StringEntity stringEntity = new StringEntity(map);
            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);
            HttpResponse response = httpClient.execute(httpPost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, charset);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static String doPost(String url, Map<String,String> map, String charset) {
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        try {
            httpClient = new SSLClient();
            httpPost = new HttpPost(url);
            //设置参数
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            Iterator iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> elem = (Map.Entry<String, String>) iterator.next();
                list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
            }
            if (list.size() > 0) {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, charset);
                entity.setContentType("application/json");
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json;charset=utf-8");
                httpPost.setEntity(entity);
            }
            HttpResponse response = httpClient.execute(httpPost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, charset);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static String doGet(String url, Map<String, String> map, String charset) throws Exception {
        HttpClient httpClient = null;
        HttpGet httpGet = null;
        String result = null;
        try {
            httpClient = new SSLClient();
            URIBuilder uriBuilder = new URIBuilder(url);
            for (Map.Entry<String,String> entry : map.entrySet()) {
                uriBuilder.addParameter(entry.getKey(), entry.getValue());
            }
            httpGet = new HttpGet(uriBuilder.build());
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity, charset);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception();
        }
    }

    /**
    * @Description:下载气象图片
    * @Author: Yiming
    * @Date: 2023/2/23
    */
    public static void downloadMeteorologyPng(String url, String path) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        org.springframework.http.HttpEntity httpEntity = new org.springframework.http.HttpEntity(headers);
        ResponseEntity<byte[]> result = restTemplate.exchange(url, HttpMethod.GET, httpEntity, byte[].class);

        FileOutputStream outs = null;
        try {
            outs = new FileOutputStream(path);
            outs.write(result.getBody());
            outs.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
    * @Description:对url中的中文和空格进行编码
    * @Author: Yiming
    * @Date: 2023/2/24
    */

    public static String encodeSpaceChinese(String str, String charset) throws UnsupportedEncodingException {
        //匹配中文和空格的正则表达式
        String zhPattern = "[\u4e00-\u9fa5 ]+";
        Pattern p = Pattern.compile(zhPattern);
        Matcher m = p.matcher(str);
        StringBuffer b = new StringBuffer();
        while (m.find())
        {
            m.appendReplacement(b, URLEncoder.encode(m.group(0), charset));
        }
        m.appendTail(b);
        return b.toString().replaceAll("\\+", "%20");
    }

    /**
    * @Description:http协议
    * @Author: Yiming
    * @Date: 2023/3/13
    */
    public static <T>T httpHandle(String url, MultiValueMap<String, Object> param, Class<T> c, String method) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        org.springframework.http.HttpEntity httpEntity = new org.springframework.http.HttpEntity(param, headers);
        ResponseEntity<T> result;
        if (method.equals("post")) {
            result = restTemplate.exchange(url, HttpMethod.POST, httpEntity, c);
        } else if (method.equals("get")) {
            result = restTemplate.exchange(url, HttpMethod.GET, httpEntity, c);
        } else if (method.equals("delete")) {
            result = restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, c);
        } else if (method.equals("put")) {
            result = restTemplate.exchange(url, HttpMethod.PUT, httpEntity, c);
        } else {
            throw new Exception();
        }
        return result.getBody();
    }
}
