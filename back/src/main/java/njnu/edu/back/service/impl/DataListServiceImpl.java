package njnu.edu.back.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import njnu.edu.back.common.utils.LocalUploadUtil;
import njnu.edu.back.dao.main.DataListMapper;
import njnu.edu.back.pojo.DataList;
import njnu.edu.back.service.DataListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
        dataList.setName(jsonObject.getString("name"));
        dataList.setDescription(jsonObject.getString("description"));
        dataList.setTags(jsonObject.getObject("tags", String[].class));
        dataList.setCreator(email);
        dataList.setDownload(0);
        dataList.setWatch(0);
        dataList.setAvatar(avatarStr);
        dataList.setThumbnail(thumbnailStr);
        dataList.setStatus(0);
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
    public Map<String, Object> fuzzyQueryAdmin(int page, int size, String keyword, String[] tags, String property, Boolean flag, int status) {
        if(!keyword.equals("")) {
            keyword = "%" + keyword + "%";
        }
        int total = dataListMapper.countFuzzyQuery(keyword, tags, status);
        List<Map<String, Object>> list = dataListMapper.fuzzyQuery(size * page, size, keyword, tags, property, flag, status);
        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("list", list);
        return result;
    }

    @Override
    public Map<String, Object> deleteShareFileById(int page, int size, String keyword, String[] tags, String property, Boolean flag, int status, String id) {
        dataListMapper.deleteById(id);
        return fuzzyQueryAdmin(page, size, keyword, tags, property, flag, status);
    }
}
