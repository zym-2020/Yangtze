package njnu.edu.back.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.result.ResultEnum;
import njnu.edu.back.common.utils.Encrypt;
import njnu.edu.back.common.utils.LocalUploadUtil;
import njnu.edu.back.common.utils.ZipOperate;
import njnu.edu.back.dao.main.DataListMapper;
import njnu.edu.back.dao.main.DataRelationalMapper;
import njnu.edu.back.dao.main.DownloadHistoryMapper;
import njnu.edu.back.pojo.DataList;
import njnu.edu.back.pojo.DownloadHistory;
import njnu.edu.back.service.DataListService;
import njnu.edu.back.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/08/19/17:04
 * @Description:
 */
@Service
public class DataListServiceImpl implements DataListService {
    @Autowired
    DataListMapper dataListMapper;

    @Value("${pictureAddress}")
    String pictureAddress;

    @Autowired
    RedisService redisService;

    @Value("${encrypt.key}")
    String key;

    @Autowired
    DataRelationalMapper dataRelationalMapper;

    @Value("${tempAddress}")
    String tempAddress;

    @Autowired
    DownloadHistoryMapper downloadHistoryMapper;

    @Override
    public void addDataList(MultipartFile avatar, MultipartFile thumbnail, String jsonString, String email) {
        String avatarStr = "";
        String thumbnailStr = "";
        if(!avatar.isEmpty()) {
            String uuid = UUID.randomUUID().toString();
            String suffix = avatar.getOriginalFilename().substring(avatar.getOriginalFilename().lastIndexOf(".") + 1);
            LocalUploadUtil.uploadAvatar(pictureAddress + uuid + "." + suffix, avatar);
            avatarStr = uuid + "." + suffix;
        }
        if(!thumbnail.isEmpty()) {
            String uuid = UUID.randomUUID().toString();
            String suffix = thumbnail.getOriginalFilename().substring(thumbnail.getOriginalFilename().lastIndexOf(".") + 1);
            LocalUploadUtil.uploadAvatar(pictureAddress + uuid + "." + suffix, thumbnail);
            thumbnailStr = uuid + "." +suffix;
        }
        JSONObject jsonObject = JSON.parseObject(jsonString);
        DataList dataList = new DataList();
        dataList.setId(jsonObject.getString("id"));
        dataList.setName(jsonObject.getString("name"));
        dataList.setDescription(jsonObject.getString("description"));
        dataList.setTags(jsonObject.getObject("tags", String[].class));
        dataList.setCreator(email);
        dataList.setDownload(0);
        dataList.setWatch(0);
        dataList.setAvatar(avatarStr);
        dataList.setThumbnail(thumbnailStr);
        dataList.setStatus(1);
        dataList.setLocation(jsonObject.getObject("location", String[].class));
        dataList.setProvider(jsonObject.getString("provider"));
        dataList.setTime(jsonObject.getString("time"));
        dataList.setRange(jsonObject.getString("range"));
        dataList.setType(jsonObject.getString("type"));
        dataList.setProviderAddress(jsonObject.getString("providerAddress"));
        dataList.setProviderEmail(jsonObject.getString("providerEmail"));
        dataList.setProviderPhone(jsonObject.getString("providerPhone"));
        dataList.setGetOnline(jsonObject.getBoolean("getOnline"));
        dataList.setDetail(jsonObject.getString("detail"));
        dataListMapper.addDataList(dataList);
    }

    @Override
    public void updateList(MultipartFile avatar, MultipartFile thumbnail, String jsonString) {
        String avatarStr = "";
        String thumbnailStr = "";
        if(!avatar.isEmpty()) {
            String uuid = UUID.randomUUID().toString();
            String suffix = avatar.getOriginalFilename().substring(avatar.getOriginalFilename().lastIndexOf(".") + 1);
            LocalUploadUtil.uploadAvatar(pictureAddress + uuid + "." + suffix, avatar);
            avatarStr = uuid + "." + suffix;
        }
        if(!thumbnail.isEmpty()) {
            String uuid = UUID.randomUUID().toString();
            String suffix = thumbnail.getOriginalFilename().substring(thumbnail.getOriginalFilename().lastIndexOf(".") + 1);
            LocalUploadUtil.uploadAvatar(pictureAddress + uuid + "." + suffix, thumbnail);
            thumbnailStr = uuid + "." +suffix;
        }
        JSONObject jsonObject = JSON.parseObject(jsonString);
        DataList dataList = new DataList();
        dataList.setName(jsonObject.getString("name"));
        dataList.setDescription(jsonObject.getString("description"));
        dataList.setTags(jsonObject.getObject("tags", String[].class));
        dataList.setAvatar(avatarStr);
        dataList.setThumbnail(thumbnailStr);
        dataList.setProvider(jsonObject.getString("provider"));
        dataList.setTime(jsonObject.getString("time"));
        dataList.setRange(jsonObject.getString("range"));
        dataList.setType(jsonObject.getString("type"));
        dataList.setProviderAddress(jsonObject.getString("providerAddress"));
        dataList.setProviderEmail(jsonObject.getString("providerEmail"));
        dataList.setProviderPhone(jsonObject.getString("providerPhone"));
        dataList.setGetOnline(jsonObject.getBoolean("getOnline"));
        dataList.setDetail(jsonObject.getString("detail"));
        dataListMapper.updateDataList(jsonObject.getString("id"), dataList);
    }

    @Override
    public Map<String, Object> getFileInfo(String id) {
        return dataListMapper.getFileInfo(id);
    }

    @Override
    public Map<String, Object> getFileInfoAndUserInfo(String id) {
        return dataListMapper.getFileInfoAndUserInfo(id);
    }

    @Override
    public void addWatchCount(String id) {
        dataListMapper.addWatchCount(id);
    }

    @Override
    public Map<String, Object> fuzzyQuery(int page, int size, String keyword, String[] tags, String property, Boolean flag) {
        if(!keyword.equals("")) {
            keyword = "%" + keyword + "%";
        }
        int total = dataListMapper.countFuzzyQuery(keyword, tags, 1);

        List<Map<String, Object>> list = dataListMapper.fuzzyQuery(size * page, size, keyword, tags, property, flag, 1);
        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("list", list);
        return result;
    }

    @Override
    public Map<String, Object> fuzzyQueryAdmin(int page, int size, String keyword, String[] tags, String property, Boolean flag) {
        if(!keyword.equals("")) {
            keyword = "%" + keyword + "%";
        }
        int total = dataListMapper.countFuzzyQuery(keyword, tags, 2);
        List<Map<String, Object>> list = dataListMapper.fuzzyQuery(size * page, size, keyword, tags, property, flag, 2);
        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("list", list);
        return result;
    }

    @Override
    public Map<String, Object> deleteByAdmin(int page, int size, String keyword, String[] tags, String property, Boolean flag, String id) {
        dataListMapper.deleteById(id);
        return fuzzyQueryAdmin(page, size, keyword, tags, property, flag);
    }

    @Override
    public Map<String, Object> pageQueryByEmail(String email, int size, int page) {
        List<Map<String, Object>> list = dataListMapper.pageQueryByEmail(email, size, size * page);
        int count = dataListMapper.countPageQueryByEmail(email);
        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("total", count);
        return map;
    }

    @Override
    public void updateStatusById(String id, int status) {
        dataListMapper.updateStatusById(id, status);
    }

    @Override
    public Map<String, Object> deleteAsMember(String id, String email, int page, int size) {
        Map<String, Object> fileInfo = dataListMapper.getFileInfo(id);
        if(!fileInfo.get("creator").equals(email)) {
            throw new MyException(-99, "没有权限！");
        }
        dataListMapper.deleteById(id);
        return pageQueryByEmail(email, size, page);
    }

    @Override
    public String getDownloadURL(String id, String userId) {
        String uuid = UUID.randomUUID().toString();
        redisService.set(uuid, id, 30l);
        return Encrypt.encryptByUserId(uuid, userId, key.toCharArray());
    }

    @Override
    public void downloadAll(String userId, String id, HttpServletRequest request, HttpServletResponse response) {
        String ip = request.getRemoteAddr();
        String tempId = (String) redisService.get(id);
        if(tempId == null) {
            throw new MyException(-1, "链接已失效");
        } else {
            redisService.del(id);
            id = tempId;
        }
        List<Map<String, Object>> list = dataRelationalMapper.findFilesByDataListId(id);
        String destination = tempAddress + id + ".zip";
        ZipOperate.compressFile(destination, list);
        InputStream in = null;
        ServletOutputStream sos = null;
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(id + ".zip", "UTF-8"));
            in = new FileInputStream(destination);
            sos = response.getOutputStream();
            byte[] bytes = new byte[1024];
            while((in.read(bytes)) > -1) {
                sos.write(bytes);
            }
            sos.flush();
            sos.close();
            in.close();
            downloadHistoryMapper.addHistory(new DownloadHistory(null, userId, null, ip, id, "all"));
            dataListMapper.addDownloadCount(id);
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
    public List<Map<String, Object>> findFiles(String dataListId) {
        return dataRelationalMapper.findFilesByDataListId(dataListId);
    }
}
