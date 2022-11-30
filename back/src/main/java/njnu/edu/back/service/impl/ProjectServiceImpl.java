package njnu.edu.back.service.impl;


import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.utils.AnalyseUtil;
import njnu.edu.back.common.utils.LocalUploadUtil;
import njnu.edu.back.dao.main.AnalyticDataSetMapper;
import njnu.edu.back.dao.main.ProjectFileMapper;
import njnu.edu.back.dao.main.ProjectMapper;

import njnu.edu.back.service.ProjectService;
import njnu.edu.back.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.util.*;


/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/04/07/18:27
 * @Description:
 */
@Service
public class ProjectServiceImpl implements ProjectService {

    @Value("${basePath}")
    String basePath;


    @Value("${pictureAddress}")
    String pictureAddress;

    @Autowired
    ProjectMapper projectMapper;

    @Autowired
    AnalyticDataSetMapper analyticDataSetMapper;

    @Autowired
    RedisService redisService;

    @Autowired
    ProjectFileMapper projectFileMapper;


    @Override
    public String addProject(String projectName, MultipartFile file, boolean isPublic, String email) {
        String avatar = "";
        if(!file.isEmpty()) {
            String uuid = UUID.randomUUID().toString();
            String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
            LocalUploadUtil.uploadAvatar(pictureAddress + uuid + "." + suffix, file);
            avatar = uuid + "." + suffix;
        }

        String projectId = projectMapper.addProject(projectName, email, avatar, new String[]{}, "mapbox://styles/johnnyt/cl9miecpn001t14rspop38nyk", isPublic);
        File f = new File(basePath + email + "/project/" + projectId);
        f.mkdirs();
        return projectId;
    }

    @Override
    public Map<String, Object> getAll(String keyword, int page, int size) {
        if(!keyword.equals("")) {
            keyword = "%" + keyword + "%";
        }
        List<Map<String, Object>> list = projectMapper.fuzzyQuery(keyword, size, page * size, 1);
        int total = projectMapper.fuzzyCount(keyword, 1);
        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        return result;
    }

    @Override
    public Map<String, Object> getAllByEmail(String email, int page, int size, int status) {
        List<Map<String, Object>> list = projectMapper.fuzzyQueryByEmail(email, size, page * size, status);
        int total = projectMapper.fuzzyCountByEmail(email, status);

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        return result;
    }

    @Override
    public Map<String, Integer> getCount(String email) {
        int allTotal = projectMapper.fuzzyCountByEmail(email, 0);
        int publicTotal = projectMapper.fuzzyCountByEmail(email, 1);
        int privateTotal = projectMapper.fuzzyCountByEmail(email, -1);
        Map<String, Integer> map = new HashMap<>();
        map.put("allTotal", allTotal);
        map.put("publicTotal", publicTotal);
        map.put("privateTotal", privateTotal);
        return map;
    }

    @Override
    public Map<String, Object> getProjectInfo(String projectId) {
        return projectMapper.getProjectInfo(projectId);
    }

    @Override
    public void addData(String projectId, List<Map<String, String>> list) {
        if(list.size() != 0) {
            projectFileMapper.addRecord(projectId, list);
        }
    }

    @Override
    public List<Map<String, Object>> getData(String projectId) {
        return projectFileMapper.getData(projectId);
    }

    @Override
    public void delData(String projectId, String dataListId, String fileId) {
        Map<String, Object> map = projectMapper.getProjectInfo(projectId);
        List<String> list = Arrays.asList((String[]) map.get("layerManage"));
        boolean flag = false;
        for(String id : list) {
            if(id.equals(projectId)) {
                list.remove(id);
                flag = true;
                break;
            }
        }
        if(flag) {
            projectMapper.updateLayer(projectId, list.toArray(new String[]{}));
        }
        projectFileMapper.delData(projectId, dataListId, fileId);
    }

    @Override
    public void updateLayer(String projectId, List<String> list) {
        String[] layers = list.toArray(new String[]{});
        projectMapper.updateLayer(projectId, layers);
    }

    @Override
    public List<Map<String, Object>> getLayersInfo(String projectId) {
        Map<String, Object> map = projectMapper.getProjectInfo(projectId);
        List<String> list = Arrays.asList((String[]) map.get("layerManage"));
        List<Map<String, Object>> result = new ArrayList<>();
        if(list.size() != 0) {
            List<Map<String, Object>> temp1 = projectMapper.getLayersInfo(list);
            List<Map<String, Object>> temp2 = analyticDataSetMapper.getAnalyticData(projectId);
            List<Map<String, Object>> temp = new ArrayList<>();
            temp.addAll(temp1);
            temp.addAll(temp2);
            for(String id : list) {
                for(Map<String, Object> m : temp) {
                    if(m.get("id").toString().equals(id)) {
                        result.add(m);
                        break;
                    }
                }
            }
        }
        return result;
    }

    @Override
    public void updateBasemap(String projectId, String url) {
        projectMapper.updateBasemap(projectId, url);
    }

    @Override
    public void updatePublicState(String projectId, boolean b) {
        projectMapper.updatePublicState(projectId, b);
    }

    @Override
    public String updateProjectInfo(MultipartFile file, String projectName, String id, boolean isPublic) {
        String avatar = "";
        if(!file.isEmpty()) {
            String uuid = UUID.randomUUID().toString();
            String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
            LocalUploadUtil.uploadAvatar(pictureAddress + uuid + "." + suffix, file);
            avatar = uuid + "." + suffix;
        }
        return projectMapper.updateProjectInfo(id, projectName, isPublic, avatar);
    }

    @Override
    public void deleteProject(String projectId, String email, String role) {
        Map<String, Object> map = projectMapper.getProjectInfo(projectId);
        String creator = (String) map.get("creator");
        if(creator.equals(email) || role.equals("admin")) {
            projectMapper.deleteProject(projectId);
            analyticDataSetMapper.delAnalyticDataByProjectId(projectId);
            projectFileMapper.delByProjectId(projectId);
            LocalUploadUtil.deleteFolder(basePath + creator + "/project/" + projectId);
        } else {
            throw new MyException(-99, "没有权限");
        }
    }

    @Override
    public Map<String, Object> getAllByAdmin(String keyword, int page, int size, String role) {
        if(!role.equals("admin")) {
            throw new MyException(-99, "没有权限");
        }
        if(!keyword.equals("")) {
            keyword = "%" + keyword + "%";
        }
        List<Map<String, Object>> list = projectMapper.fuzzyQuery(keyword, size, size * page, 0);
        int total = projectMapper.fuzzyCount(keyword, 0);
        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        return result;
    }

    @Override
    public String copyProject(String projectId, String creator, String projectName, boolean isPublic, MultipartFile file, String email) {
        String avatar = "";
        if(!file.isEmpty()) {
            String uuid = UUID.randomUUID().toString();
            String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
            LocalUploadUtil.uploadAvatar(pictureAddress + uuid + "." + suffix, file);
            avatar = uuid + "." + suffix;
        }
        List<Map<String, Object>> analyticData = analyticDataSetMapper.getAnalyticData(projectId);
        String newProjectId = projectMapper.addProject(projectName, email, avatar, new String[]{}, "mapbox://styles/johnnyt/cl9miecpn001t14rspop38nyk", isPublic);
        File f = new File(basePath + email + "/project/" + newProjectId);
        f.mkdirs();
        copyAnalyticDataset(analyticData, email, newProjectId, projectId, creator);
        List<Map<String, Object>> list = projectFileMapper.getData(projectId);
        List<Map<String, String>> fileList = new ArrayList<>();
        for(Map<String, Object> m : list) {
            Map<String, String> temp = new HashMap<>();
            temp.put("fileId", m.get("fileId").toString());
            temp.put("dataListId", m.get("dataListId").toString());
            fileList.add(temp);
        }
        projectFileMapper.addRecord(newProjectId, fileList);
        return newProjectId;
    }

    private void copyAnalyticDataset(List<Map<String, Object>> list, String email, String projectId, String oldProjectId, String creator) {
        for(int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            String address = map.get("address").toString();
            String fileName = map.get("fileName").toString();
            String visualType = map.get("visualType").toString();
            String visualId = map.get("visualId").toString();
            int index = address.lastIndexOf(".");
            String newAddress;
            if(index != -1) {
                newAddress = UUID.randomUUID().toString() + address.substring(address.lastIndexOf("."));
                AnalyseUtil.copyFile(basePath + creator + "/project/" + oldProjectId + "/" + address, basePath + email + "/project/" + projectId + "/" + newAddress);
            } else {
                newAddress = address;
            }
            analyticDataSetMapper.addDraw("", fileName, newAddress, email, visualType, visualId, projectId);
        }
    }

}
