package njnu.edu.back.service.impl;

import cn.hutool.json.JSONArray;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.result.ResultEnum;
import njnu.edu.back.dao.ProjectMapper;
import njnu.edu.back.dao.RasterRelationshipMapper;
import njnu.edu.back.proj.RasterRelationship;
import njnu.edu.back.proj.dto.AddProject;
import njnu.edu.back.proj.support.projectJson.ProjectJsonBean;
import njnu.edu.back.proj.support.projectJson.Resource;
import njnu.edu.back.service.ProjectService;
import njnu.edu.back.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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
    public void addProject(AddProject addProject, String email) {
        addProject.setCreator(email);
        projectMapper.addProject(addProject);
    }

    @Override
    public String getResultById(Integer id) {
        return projectMapper.getResult(id);
    }

    @Override
    public List<Map<String, Object>> getProjectId(String email) {
        return projectMapper.getProjectId(email);
    }

    @Override
    public int setResult(ProjectJsonBean result, int id) {
        return projectMapper.setResult(JSONObject.toJSONString(result), id);
    }

    @Override
    public void saveSectionValue(Integer DEMId, Double lat1, Double lon1, Double lat2, Double lon2, String sectionName, String email, String projectName) {
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
                ProcessBuilder processBuilder = new ProcessBuilder();
                List<String> commands = new ArrayList<>();
                commands.add("python");
                commands.add(pythonDir + "section.py");
                commands.add(path);
                commands.add(lat1.toString());
                commands.add(lon1.toString());
                commands.add(lat2.toString());
                commands.add(lon2.toString());
                commands.add(resultPath + "\\" + sectionName + "_" + rasterRelationship.getFileName() + ".txt");

                processBuilder.command(commands);
                Process start = processBuilder.start();

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
    public List<Double> getSectionValue(String email, String projectName, String sectionName, String DEMName, Integer DEMId) {
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
}
