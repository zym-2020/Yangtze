package njnu.edu.back.service.impl;

import cn.hutool.json.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.result.ResultEnum;
import njnu.edu.back.common.utils.AnalyseUtil;
import njnu.edu.back.common.utils.CommonUtils;
import njnu.edu.back.dao.ProjectMapper;
import njnu.edu.back.dao.RasterRelationshipMapper;
import njnu.edu.back.pojo.Project;
import njnu.edu.back.pojo.RasterRelationship;
import njnu.edu.back.pojo.dto.AddProject;
import njnu.edu.back.pojo.support.projectJson.ProjectJsonBean;
import njnu.edu.back.pojo.support.projectJson.Resource;
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


    @Autowired
    ProjectMapper projectMapper;

    @Autowired
    RasterRelationshipMapper rasterRelationshipMapper;
    
    @Autowired
    RedisService redisService;

    @Value("${basedir}")
    String baseDir;

    @Value("${pythondir}")
    String pythonDir;

    @Override
    public Map<String, Object> addProject(AddProject addProject, String email, MultipartFile multipartFile) {
        addProject.setCreator(email);
        String path = baseDir + email + "\\projects\\" + addProject.getProjectName();
        File file = new File(path);
        file.mkdir();
        InputStream in = null;
        FileOutputStream out = null;
        String ip = "";
        String uuid = UUID.randomUUID().toString();
        String suffix = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1);
        try {
            in = multipartFile.getInputStream();

            File outFile = new File(baseDir + "other\\avatar");
            if(!outFile.exists()) {
                outFile.mkdir();
            }
            out = new FileOutputStream(outFile + "\\" + uuid + "." + suffix);
            int len = -1;
            byte[] bytes = new byte[1024];
            while((len = in.read(bytes)) != -1) {
                out.write(bytes, 0 ,len);
            }
            ip = CommonUtils.getIp();
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        } finally {
            try {
                if(in != null) {
                    in.close();
                }
                if(out != null) {
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
            }
        }
        addProject.setAvatar("http://" + ip + ":8002/file/avatar/" + uuid + "." + suffix);
        return projectMapper.addProject(addProject);
    }

    @Override
    public Map<String, Object> addProjectWithoutAvatar(AddProject addProject, String email) {
        addProject.setCreator(email);
        String path = baseDir + email + "\\projects\\" + addProject.getProjectName();
        File file = new File(path);
        file.mkdir();
        addProject.setAvatar("");
        return projectMapper.addProject(addProject);
    }

    @Override
    public String getResultById(String id) {
        return projectMapper.getResult(id);
    }

    @Override
    public List<Map<String, Object>> getProjectsByEmail(String email) {
        return projectMapper.getProjectsByEmail(email);
    }

    @Override
    public int setResult(ProjectJsonBean result, String id) {
        return projectMapper.setResult(JSONObject.toJSONString(result), id);
    }

    @Override
    public void saveSectionValue(String DEMId, Double lat1, Double lon1, Double lat2, Double lon2, String sectionName, String email, String projectName) {
        RasterRelationship rasterRelationship = rasterRelationshipMapper.getById(DEMId);
        String path = rasterRelationship.getAddress() + "\\" + rasterRelationship.getFileName();
        String resultPath = baseDir + email + "\\projects\\" + projectName + "\\断面形态";
        File file = new File(resultPath);
        if(!file.exists()) {
            file.mkdir();
        }
        redisService.set(email + projectName + sectionName + rasterRelationship.getFileName(), 0, 60*24l);
        new Thread() {
            @Override
            @SneakyThrows
            public void run() {
                Process start = AnalyseUtil.saveSectionValue(path, lat1.toString(), lon1.toString(), lat2.toString(), lon2.toString(), resultPath + "\\" + sectionName + "_" + rasterRelationship.getFileName() + ".txt");

                int exitCode = start.waitFor();
                if(exitCode == 0) {
                    redisService.del(email + projectName + sectionName + rasterRelationship.getFileName());
                } else {
                    redisService.set(email + projectName + sectionName + rasterRelationship.getFileName(), -1, 60*24l);
                }
                System.out.println(exitCode);
            }
        }.start();

    }

    @Override
    public List<Double> getSectionValue(String email, String projectName, String sectionName, String DEMName, String DEMId) {
        String filePath = baseDir + email + "\\projects\\" + projectName + "\\断面形态\\" + sectionName + "_" + DEMName + ".txt";
        File file = new File(filePath);
        if(!file.exists()) {
            Integer state = (Integer) redisService.get(email + projectName + sectionName + DEMName);
            if(state == null) {
                String result = projectMapper.getResultByEmailAndProjectName(email, projectName);
                ProjectJsonBean projectJsonBean = JSONObject.parseObject(result, ProjectJsonBean.class);
                cn.hutool.json.JSONObject temp = null;
                for(Resource resource : projectJsonBean.getAnalyse().getSection().getAnalysisResultList()) {
                    if(resource.getName().equals(sectionName)) {
                        temp = resource.getGeoJson();
                        break;
                    }
                }
                if(temp == null) throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
                double lat1 = temp.getJSONArray("coordinates").get(0, JSONArray.class).get(0, Double.class);
                double lon1 = temp.getJSONArray("coordinates").get(0, JSONArray.class).get(1, Double.class);
                double lat2 = temp.getJSONArray("coordinates").get(1, JSONArray.class).get(0, Double.class);
                double lon2 = temp.getJSONArray("coordinates").get(1, JSONArray.class).get(1, Double.class);
                saveSectionValue(DEMId, lat1, lon1, lat2, lon2, sectionName, email, projectName);
                throw new MyException(100, "正在计算断面");
            } else if(state == 0) {
                throw new MyException(100, "正在计算断面");
            } else if(state == -1) {
                throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
            } else {
                throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
            }
        } else {
            BufferedReader bufferedReader = null;
            List<Double> result = new ArrayList<>();
            try {
                bufferedReader = new BufferedReader(new FileReader(file));
                String tempString = null;
                while((tempString = bufferedReader.readLine()) != null) {
                    result.add(Double.parseDouble(tempString));
                }
                bufferedReader.close();

            } catch (Exception e) {
                e.printStackTrace();
                throw new MyException(-1, "读取断面文件时出错");
            } finally {
                if(bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new MyException(-1, "读取断面文件时出错");
                    }
                }
            }
            return result;
        }

    }

    @Override
    public void delSection(String email, String projectName, String sectionName, String DEMName) {
        String path = baseDir + email + "\\projects\\" + projectName + "\\断面形态\\" + sectionName + "_" + DEMName + ".txt";
        File file = new File(path);
        file.delete();
    }

    @Override
    public void saveSectionContrastValue(Double lat1, Double lon1, Double lat2, Double lon2, String sectionName, String email, String projectName) {
        String resultPath = baseDir + email + "\\projects\\" + projectName + "\\断面比较";
        File file = new File(resultPath);
        if(!file.exists()) {
            file.mkdir();
        }
        redisService.set(email + projectName + sectionName + "断面比较", 0, 60*24l);

        new Thread() {
            @Override
            @SneakyThrows
            public void run() {
                Process start = AnalyseUtil.saveSectionContrast(lat1.toString(), lon1.toString(), lat2.toString(), lon2.toString(), resultPath + "\\" + sectionName + ".txt");
                int exitCode = start.waitFor();
                if(exitCode == 0) {
                    redisService.del(email + projectName + sectionName + "断面比较");
                } else {
                    redisService.set(email + projectName + sectionName + "断面比较", -1, 60*24l);
                }
                System.out.println(exitCode);
            }
        }.start();
    }

    @Override
    public Map<String, List<Double>> getSectionContrastValue(String email, String projectName, String sectionName) {
        String filePath = baseDir + email + "\\projects\\" + projectName + "\\断面比较\\" + sectionName + ".txt";
        File file = new File(filePath);
        if(!file.exists()) {
            Integer state = (Integer) redisService.get(email + projectName + sectionName + "断面比较");
            if(state == null) {
                String result = projectMapper.getResultByEmailAndProjectName(email, projectName);
                ProjectJsonBean projectJsonBean = JSONObject.parseObject(result, ProjectJsonBean.class);
                cn.hutool.json.JSONObject temp = null;
                for(Resource resource : projectJsonBean.getAnalyse().getSectionContrast().getAnalysisResultList()) {
                    if(resource.getName().equals(sectionName)) {
                        temp = resource.getGeoJson();
                        break;
                    }
                }
                if(temp == null) throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
                double lat1 = temp.getJSONArray("coordinates").get(0, JSONArray.class).get(0, Double.class);
                double lon1 = temp.getJSONArray("coordinates").get(0, JSONArray.class).get(1, Double.class);
                double lat2 = temp.getJSONArray("coordinates").get(1, JSONArray.class).get(0, Double.class);
                double lon2 = temp.getJSONArray("coordinates").get(1, JSONArray.class).get(1, Double.class);
                saveSectionContrastValue(lat1, lon1, lat2, lon2, sectionName, email, projectName);
                throw new MyException(100, "正在计算断面");
            } else if(state == 0) {
                throw new MyException(100, "正在计算断面");
            } else {
                throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
            }
        } else {
            BufferedReader bufferedReader = null;
            Map<String, List<Double>> result = new HashMap<>();
            try {
                bufferedReader = new BufferedReader(new FileReader(file));
                String countStr = bufferedReader.readLine();
                int count = Integer.parseInt(countStr);
                String keys = bufferedReader.readLine();
                String[] keyArray = keys.split(" ");
                for(int i = 0;i < count;i++) {
                    result.put(keyArray[i], new ArrayList<>());
                }
                String temp = null;
                int number = 0;
                while((temp = bufferedReader.readLine()) != null) {
                    if(temp.equals("")) {
                        System.out.println(temp);
                        number = number + 1;
                    } else {
                        result.get(keyArray[number]).add(Double.parseDouble(temp));
                    }
                }
                bufferedReader.close();
            } catch (Exception e) {
                e.printStackTrace();
                throw new MyException(-1, "读取断面文件时出错");
            } finally {
                if(bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new MyException(-1, "读取断面文件时出错");
                    }
                }
            }
            return result;
        }
    }

    @Override
    public JSONObject pageQuery(int size, int page) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", projectMapper.countAll());
        jsonObject.put("list", projectMapper.pageQuery(size, page * size));
        return jsonObject;
    }

    @Override
    public Map<String, Object> findProjectById(String projectId) {
        return projectMapper.findProjectById(projectId);
    }


}
