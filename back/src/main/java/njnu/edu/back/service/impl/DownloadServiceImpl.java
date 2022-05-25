package njnu.edu.back.service.impl;

import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.result.ResultEnum;
import njnu.edu.back.common.utils.Encrypt;
import njnu.edu.back.dao.DownloadHistoryMapper;
import njnu.edu.back.dao.FileMapper;
import njnu.edu.back.dao.ShareFileMapper;
import njnu.edu.back.pojo.DownloadHistory;
import njnu.edu.back.pojo.ShareFile;
import njnu.edu.back.service.DownloadService;
import njnu.edu.back.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/05/19/15:12
 * @Description:
 */
@Service
public class DownloadServiceImpl implements DownloadService {

    @Autowired
    ShareFileMapper shareFileMapper;

    @Autowired
    FileMapper fileMapper;

    @Autowired
    RedisService redisService;

    @Autowired
    DownloadHistoryMapper downloadHistoryMapper;

    @Value("${encrypt.key}")
    String key;

    @Override
    public void downloadShareFile(HttpServletResponse response, String id, String userId, String ip) {
        String tempId = (String) redisService.get(id);
        if(tempId == null) {
            throw new MyException(-1, "链接已失效");
        } else {
            redisService.del(id);
            id = tempId;
        }
        Map<String, Object> map = shareFileMapper.getOriginAddressAndGetOnline(id);
        if((boolean) map.get("get_online")) {
            String fileName = (String) map.get("origin_name");
            File file = new File((String) map.get("origin_address"));
            if(!file.exists()) {
                throw new MyException(-1, "文件不存在");
            }
            InputStream in = null;
            ServletOutputStream sos = null;

            try {
                response.setContentType("application/octet-stream");
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
                response.addHeader("Content-Length", "" + file.length());
                in = new FileInputStream(new File((String) map.get("origin_address")));
                sos = response.getOutputStream();
                byte[] b = new byte[1024];
                while(in.read(b) > 0) {
                    sos.write(b);
                }
                sos.flush();
                sos.close();
                in.close();
                shareFileMapper.addDownload(id);
                downloadHistoryMapper.addHistory(new DownloadHistory(null, userId, null, ip, id, "origin"));
            } catch (Exception e) {
                e.printStackTrace();
                throw new MyException(-1, "文件下载错误");
            } finally {
                try {
                    if(in != null) {
                        in.close();
                    }
                    if(sos != null) {
                        sos.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
                }
            }
        } else {
            throw new MyException(-1, "没有权限");
        }

    }

    @Override
    public void downloadLocalFile(HttpServletResponse response, String id) {
        String tempId = (String) redisService.get(id);
        if(tempId == null) {
            throw new MyException(-1, "链接已失效");
        } else {
            redisService.del(id);
            id = tempId;
        }
        Map<String, Object> file = fileMapper.findById(id);
        String fileName = (String) file.get("name");
        File f = new File((String) file.get("address"));
        if(!f.exists()) {
            throw new MyException(-1, "文件不存在");
        }
        InputStream in = null;
        ServletOutputStream sos = null;
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.addHeader("Content-Length", "" + f.length());
            in = new FileInputStream(f);
            sos = response.getOutputStream();
            byte[] b = new byte[1024];
            while(in.read(b) > 0) {
                sos.write(b);
            }
            sos.flush();
            sos.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        } finally {
            try {
                if(in != null) {
                    in.close();
                }
                if(sos != null) {
                    sos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
            }
        }
    }

    @Override
    public String getDownloadURL(String id, String userId) {
        String uuid = UUID.randomUUID().toString();
        redisService.set(uuid, id, 30l);
        return Encrypt.encryptByUserId(uuid, userId, key.toCharArray());

    }

}
