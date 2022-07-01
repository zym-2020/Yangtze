package njnu.edu.back.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.result.ResultEnum;
import njnu.edu.back.pojo.Project;
import njnu.edu.back.pojo.support.Layer;
import njnu.edu.back.repository.ProjectRepository;
import njnu.edu.back.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
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

    @Value("${basedir}")
    String baseDir;

    @Autowired
    ProjectRepository projectRepository;


    @Override
    public Project addProject(Project project) {
        project.setCreateTime(new Date());
        project.setLayers(new ArrayList<>());
        project.setSortLayers(new ArrayList<>());
        project.setAvatar("");
        Project result = projectRepository.save(project);
        return result;
    }

    @Override
    public Project addProject(String jsonString, MultipartFile file) {
        String uuid = UUID.randomUUID().toString();
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        InputStream in = null;
        FileOutputStream out = null;
        try {
            in = file.getInputStream();
            File outFile = new File(baseDir + "other\\avatar");
            if(!outFile.exists()) {
                outFile.mkdirs();
            }
            out = new FileOutputStream(baseDir + "other\\avatar\\" + uuid + "." + suffix);
            int len = -1;
            byte[] bytes = new byte[1024];
            while((len = in.read(bytes)) != -1) {
                out.write(bytes, 0, len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        }finally {
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
        JSONObject jsonObject = JSON.parseObject(jsonString);
        Project project = new Project(null, jsonObject.getString("projectName"), jsonObject.getString("creator"), jsonObject.getString("description"), new Date(), new ArrayList<>(), new ArrayList<>(), jsonObject.getString("creatorName"), uuid + "." + suffix);
        return projectRepository.save(project);
    }

    @Override
    public void addLayer(Layer layer, String projectId) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        if(!optionalProject.isPresent()) {
            throw new MyException(ResultEnum.NO_OBJECT);
        }
        Project project = optionalProject.get();
        project.getLayers().add(layer);
        project.getSortLayers().add(layer.getId());
        projectRepository.save(project);
    }

    @Override
    public Project addLayers(List<Layer> layers, String projectId) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        if(!optionalProject.isPresent()) {
            throw new MyException(ResultEnum.NO_OBJECT);
        }
        Project project = optionalProject.get();
        List<String> sortLayers = project.getSortLayers();
        List<Layer> layerList = project.getLayers();
        for(Layer layer : layers) {
            sortLayers.add(layer.getId());
            layerList.add(layer);
        }

        return projectRepository.save(project);
    }

    @Override
    public Project getProjectInfo(String projectId) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        if(!optionalProject.isPresent()) {
            throw new MyException(ResultEnum.NO_OBJECT);
        }
        Project project = optionalProject.get();
        return project;
    }

    @Override
    public Page<Project> getAll(int page, int size, String keyWord) {
        Pageable pageable = PageRequest.of(page, size);
        if (keyWord.equals("")) {
            Page<Project> projects = projectRepository.findAll(pageable);
            return projects;
        } else {
            ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                    .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                    .withIgnoreCase(true);
            Project project = new Project();
            project.setProjectName(keyWord);
            Example<Project> projectExample = Example.of(project, exampleMatcher);
            Page<Project> projects = projectRepository.findAll(projectExample, pageable);
            return projects;
        }

    }

    @Override
    public List<Project> getProjectsByEmail(String email) {
        return projectRepository.findAllByCreator(email);
    }

    //    @Autowired
//    ProjectMapper projectMapper;
//
//    @Autowired
//    RasterRelationshipMapper rasterRelationshipMapper;
//
//    @Autowired
//    AnalyticDataSetMapper analyticDataSetMapper;
//
//    @Autowired
//    RedisService redisService;
//
//    @Value("${basedir}")
//    String baseDir;
//
//    @Value("${pythondir}")
//    String pythonDir;
//
//    @Override
//    public Map<String, Object> addProject(AddProject addProject, String email, MultipartFile multipartFile) {
//        addProject.setCreator(email);
//        InputStream in = null;
//        FileOutputStream out = null;
//        String ip = "";
//        String uuid = UUID.randomUUID().toString();
//        String suffix = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1);
//        try {
//            in = multipartFile.getInputStream();
//
//            File outFile = new File(baseDir + "other\\avatar");
//            if(!outFile.exists()) {
//                outFile.mkdir();
//            }
//            out = new FileOutputStream(outFile + "\\" + uuid + "." + suffix);
//            int len = -1;
//            byte[] bytes = new byte[1024];
//            while((len = in.read(bytes)) != -1) {
//                out.write(bytes, 0 ,len);
//            }
//            ip = CommonUtils.getIp();
//            out.close();
//            in.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
//        } finally {
//            try {
//                if(in != null) {
//                    in.close();
//                }
//                if(out != null) {
//                    out.close();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
//            }
//        }
//        addProject.setAvatar("/file/avatar/" + uuid + "." + suffix);
//        Map<String, Object> map = projectMapper.addProject(addProject);
//        String path = baseDir + email + "\\projects\\" + map.get("id");
//        File file = new File(path);
//        file.mkdir();
//        return map;
//    }
//
//    @Override
//    public Map<String, Object> addProjectWithoutAvatar(AddProject addProject, String email) {
//        addProject.setCreator(email);
//        addProject.setAvatar("");
//        Map<String, Object> map = projectMapper.addProject(addProject);
//        String path = baseDir + email + "\\projects\\" + map.get("id");
//        File file = new File(path);
//        file.mkdirs();
//        return map;
//    }
//
//    @Override
//    public String getResultById(String id) {
//        return projectMapper.getResult(id);
//    }
//
//    @Override
//    public List<Map<String, Object>> getProjectsByEmail(String email) {
//        return projectMapper.getProjectsByEmail(email);
//    }
//
//    @Override
//    public int setResult(ProjectJsonBean result, String id) {
//        return projectMapper.setResult(JSONObject.toJSONString(result), id);
//    }
//
//    @Override
//    public void saveSectionValue(String id, String DEMId, Double lat1, Double lon1, Double lat2, Double lon2, String email, String projectId) {
//        Map<String, Object> map = analyticDataSetMapper.findById(DEMId);
//        String fileName = (String) map.get("file_name");
//        String address = (String) map.get("address");
//        String path = address + "\\" + fileName;
//        String resultPath = baseDir + email + "\\projects\\" + projectId + "\\断面形态";
//        File file = new File(resultPath);
//        if(!file.exists()) {
//            file.mkdirs();
//        }
//        redisService.set(id, 0, 60*24l);
//        new Thread() {
//            @Override
//            @SneakyThrows
//            public void run() {
//                Process start = AnalyseUtil.saveSectionValue(path, lat1.toString(), lon1.toString(), lat2.toString(), lon2.toString(), resultPath + "\\" + id + ".txt");
//                int exitCode = start.waitFor();
//                if(exitCode == 0) {
//                    redisService.del(id);
//                } else {
//                    redisService.set(id, -1, 60*24l);
//                }
//                System.out.println(exitCode);
//            }
//        }.start();
//
//    }
//
//    @Override
//    public List<Double> getSectionValue(String id, String email, String projectId) {
//        String filePath = baseDir + email + "\\projects\\" + projectId + "\\断面形态\\" + id + ".txt";
//        File file = new File(filePath);
//        if(!file.exists()) {
//            Integer state = (Integer) redisService.get(id);
//
//            if(state == null) {
//                String result = projectMapper.getResult(projectId);
//                ProjectJsonBean projectJsonBean = JSONObject.parseObject(result, ProjectJsonBean.class);
//                cn.hutool.json.JSONObject temp = null;
//                Resource resource = null;
//                for(Resource tempResource : projectJsonBean.getAnalyse().getSection().getAnalysisResultList()) {
//                    if(tempResource.getId().equals(id)) {
//                        temp = tempResource.getGeoJson();
//                        resource = tempResource;
//                        break;
//                    }
//                }
//                if(temp == null) throw new MyException(-2, "断面不存在");
//                double lat1 = temp.getJSONArray("coordinates").get(0, JSONArray.class).get(0, Double.class);
//                double lon1 = temp.getJSONArray("coordinates").get(0, JSONArray.class).get(1, Double.class);
//                double lat2 = temp.getJSONArray("coordinates").get(1, JSONArray.class).get(0, Double.class);
//                double lon2 = temp.getJSONArray("coordinates").get(1, JSONArray.class).get(1, Double.class);
//                saveSectionValue(id, resource.getSelectDemId(), lat1, lon1, lat2, lon2, email, projectId);
//                throw new MyException(100, "正在计算断面");
//            } else if(state == 0) {
//                throw new MyException(100, "正在计算断面");
//            } else if(state == -1) {
//                throw new MyException(-1, "断面计算出错");
//            } else {
//                throw new MyException(-1, "断面计算出错");
//            }
//        } else {
//            BufferedReader bufferedReader = null;
//            List<Double> result = new ArrayList<>();
//            try {
//                bufferedReader = new BufferedReader(new FileReader(file));
//                String tempString = null;
//                while((tempString = bufferedReader.readLine()) != null) {
//                    result.add(Double.parseDouble(tempString));
//                }
//                bufferedReader.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//                throw new MyException(-1, "读取断面文件时出错");
//            } finally {
//                if(bufferedReader != null) {
//                    try {
//                        bufferedReader.close();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        throw new MyException(-1, "读取断面文件时出错");
//                    }
//                }
//            }
//            return result;
//        }
//
//    }
//
//    @Override
//    public void delSection(String email, String projectId, String sectionId) {
//        String path = baseDir + email + "\\projects\\" + projectId + "\\断面形态\\" + sectionId + ".txt";
//        File file = new File(path);
//        file.delete();
//    }
//
//    @Override
//    public void saveSectionContrastValue(String id, Double lat1, Double lon1, Double lat2, Double lon2, List<String> demIds, String email, String projectId) {
//        String resultPath = baseDir + email + "\\projects\\" + projectId + "\\断面比较";
//        File file = new File(resultPath);
//        if(!file.exists()) {
//            file.mkdir();
//        }
//        redisService.set(id, 0, 60*24l);
//        List<String> address = new ArrayList<>();
//        List<String> name = new ArrayList<>();
//        for(String demId : demIds) {
//            Map<String, Object> map = analyticDataSetMapper.findById(demId);
//            address.add(map.get("address") + "\\" + map.get("file_name"));
//            name.add((String) map.get("name"));
//        }
//        new Thread() {
//            @Override
//            @SneakyThrows
//            public void run() {
//                Process start = AnalyseUtil.saveSectionContrast(lat1.toString(), lon1.toString(), lat2.toString(), lon2.toString(), resultPath + "\\" + id + ".txt", address, name);
//                int exitCode = start.waitFor();
//                if(exitCode == 0) {
//                    redisService.del(id);
//                } else {
//                    redisService.set(id, -1, 60*24l);
//                }
//                System.out.println(exitCode);
//            }
//        }.start();
//    }
//
//    @Override
//    public Map<String, List<Double>> getSectionContrastValue(String email, String projectId, String sectionId) {
//        String filePath = baseDir + email + "\\projects\\" + projectId + "\\断面比较\\" + sectionId + ".txt";
//        File file = new File(filePath);
//        if(!file.exists()) {
//            Integer state = (Integer) redisService.get(sectionId);
//            if(state == null) {
//                String result = projectMapper.getResult(sectionId);
//                ProjectJsonBean projectJsonBean = JSONObject.parseObject(result, ProjectJsonBean.class);
//                cn.hutool.json.JSONObject temp = null;
//                Resource resource = null;
//                for(Resource tempResource : projectJsonBean.getAnalyse().getSectionContrast().getAnalysisResultList()) {
//                    if(tempResource.getId().equals(sectionId)) {
//                        temp = tempResource.getGeoJson();
//                        resource = tempResource;
//                        break;
//                    }
//                }
//                if(temp == null) throw new MyException(-2, "断面不存在");
//                double lat1 = temp.getJSONArray("coordinates").get(0, JSONArray.class).get(0, Double.class);
//                double lon1 = temp.getJSONArray("coordinates").get(0, JSONArray.class).get(1, Double.class);
//                double lat2 = temp.getJSONArray("coordinates").get(1, JSONArray.class).get(0, Double.class);
//                double lon2 = temp.getJSONArray("coordinates").get(1, JSONArray.class).get(1, Double.class);
//                saveSectionContrastValue(sectionId, lat1, lon1, lat2, lon2, resource.getSelectDemIds(), email, projectId);
//                throw new MyException(100, "正在计算断面");
//            } else if(state == 0) {
//                throw new MyException(100, "正在计算断面");
//            } else {
//                throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
//            }
//        } else {
//            BufferedReader bufferedReader = null;
//            Map<String, List<Double>> result = new HashMap<>();
//            try {
//                bufferedReader = new BufferedReader(new FileReader(file));
//                String countStr = bufferedReader.readLine();
//                int count = Integer.parseInt(countStr);
//                String keys = bufferedReader.readLine();
//                String[] keyArray = keys.split(" ");
//                for(int i = 0;i < count;i++) {
//                    result.put(keyArray[i], new ArrayList<>());
//                }
//                String temp = null;
//                int number = 0;
//                while((temp = bufferedReader.readLine()) != null) {
//                    if(temp.equals("")) {
//                        System.out.println(temp);
//                        number = number + 1;
//                    } else {
//                        result.get(keyArray[number]).add(Double.parseDouble(temp));
//                    }
//                }
//                bufferedReader.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//                throw new MyException(-1, "读取断面文件时出错");
//            } finally {
//                if(bufferedReader != null) {
//                    try {
//                        bufferedReader.close();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        throw new MyException(-1, "读取断面文件时出错");
//                    }
//                }
//            }
//            return result;
//        }
//    }
//
//    @Override
//    public JSONObject pageQuery(int size, int page, String keyWord) {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("total", projectMapper.countAll("%" + keyWord + "%"));
//        jsonObject.put("list", projectMapper.pageQuery(size, page * size, "%" + keyWord + "%"));
//        return jsonObject;
//    }
//
//    @Override
//    public Map<String, Object> findProjectById(String projectId) {
//        return projectMapper.findProjectById(projectId);
//    }


}
