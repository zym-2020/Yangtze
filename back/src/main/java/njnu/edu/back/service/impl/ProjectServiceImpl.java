package njnu.edu.back.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.result.ResultEnum;
import njnu.edu.back.common.utils.AnalyseUtil;
import njnu.edu.back.common.utils.LocalUploadUtil;
import njnu.edu.back.dao.main.AnalyticDataSetMapper;
import njnu.edu.back.dao.main.ProjectFileMapper;
import njnu.edu.back.dao.main.ProjectMapper;
import njnu.edu.back.pojo.Project;
import njnu.edu.back.pojo.support.Layer;
import njnu.edu.back.pojo.support.NameCount;
import njnu.edu.back.pojo.support.Section;
import njnu.edu.back.repository.ProjectRepository;
import njnu.edu.back.service.ProjectService;
import njnu.edu.back.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
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

    @Value("${contourdir}")
    String contourDir;

    @Value("${shp2pgsqldir}")
    String shp2pgsqlDir;

    @Value("${pictureAddress}")
    String pictureAddress;

    @Autowired
    ProjectMapper projectMapper;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    AnalyticDataSetMapper analyticDataSetMapper;

    @Autowired
    RedisService redisService;

    @Autowired
    ProjectFileMapper projectFileMapper;


    @Override
    public String addProject(String projectName, MultipartFile file, String email) {
        String avatar = "";
        if(!file.isEmpty()) {
            String uuid = UUID.randomUUID().toString();
            String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
            LocalUploadUtil.uploadAvatar(pictureAddress + uuid + "." + suffix, file);
            avatar = uuid + "." + suffix;
        }

        String projectId = projectMapper.addProject(projectName, email, avatar, new String[]{});
        File f = new File(basePath + email + "\\project\\" + projectId);
        f.mkdirs();
        return projectId;
    }

    @Override
    public Map<String, Object> getAll(String keyword, int page, int size) {
        if(!keyword.equals("")) {
            keyword = "%" + keyword + "%";
        }
        List<Map<String, Object>> list = projectMapper.fuzzyQuery(keyword, size, page * size);
        int total = projectMapper.fuzzyCount(keyword);
        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        return result;
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


    //    @Override
//    public void addLayer(Layer layer, String projectId) {
//        Optional<Project> optionalProject = projectRepository.findById(projectId);
//        if(!optionalProject.isPresent()) {
//            throw new MyException(ResultEnum.NO_OBJECT);
//        }
//        Project project = optionalProject.get();
//        project.getLayers().add(layer);
//        projectRepository.save(project);
//    }
//
//    @Override
//    public Project addLayers(List<Layer> layers, String projectId) {
//        Optional<Project> optionalProject = projectRepository.findById(projectId);
//        if(!optionalProject.isPresent()) {
//            throw new MyException(ResultEnum.NO_OBJECT);
//        }
//        Project project = optionalProject.get();
//        List<Layer> layerList = project.getLayers();
//        for(Layer layer : layers) {
//            layerList.add(layer);
//        }
//
//        return projectRepository.save(project);
//    }
//
//    @Override
//    public Layer addSection(Layer layer, String projectId, String email) {
//        Optional<Project> optionalProject = projectRepository.findById(projectId);
//        if(!optionalProject.isPresent()) {
//            throw new MyException(ResultEnum.NO_OBJECT);
//        }
//        Project project = optionalProject.get();
//        List<String> types = new ArrayList<>();
//        types.add("riverBed");
//        List<Map<String, Object>> dems = analyticDataSetMapper.findDataByType(types);
//        layer.setName("断面形态_" + project.getNameCount().getSection());
//        List<Section> sections = new ArrayList<>();
//        for(Map<String, Object> map : dems) {
//            sections.add(new Section(UUID.randomUUID().toString(), map.get("id").toString(), 0));
//        }
//        layer.setSections(sections);
//        project.getNameCount().setSection(project.getNameCount().getSection() + 1);
//        project.getLayers().add(layer);
//        projectRepository.save(project);
//
//        for (int i = 0; i < dems.size(); i++) {
//            Map<String, Object> map = dems.get(i);
//            int finalI = i;
//            new Thread() {
//                @Override
//                @SneakyThrows
//                public void run() {
//                    String address = (String) map.get("address");
//                    String dataName = (String) map.get("name");
//                    String id = sections.get(finalI).getId();
//                    String tempPath = baseDir + "other\\temp\\" + id + ".txt";
//                    String resultPath = baseDir + email + "\\projects\\" + projectId + "\\" + id + ".txt";
//                    Process process = AnalyseUtil.saveSectionValue(tempPath,address + "\\" + dataName, layer.getGeoJson().getJSONArray("coordinates"), resultPath);
//                    int code = process.waitFor();
//                    for (Layer l: project.getLayers()) {
//                        if(l.getId().equals(layer.getId())) {
//                            if(code == 0) {
//                                l.getSections().get(finalI).setState(1);
//                            } else {
//                                l.getSections().get(finalI).setState(-1);;
//                            }
//                        }
//                    }
//                    File file = new File(tempPath);
//                    if(file.exists()) {
//                        file.delete();
//                    }
//                    projectRepository.save(project);
//                }
//            }.start();
//        }
//
//        return layer;
//    }
//
//    @Override
//    public List<Map<String, Object>> getSectionValue(String sectionId, String projectId, String email, List<String> valueIds) {
//        List<Map<String, Object>> result = new ArrayList<>();
//        for(String str : valueIds) {
//            File file = new File(baseDir + email + "\\projects\\" + projectId + "\\" + str + ".txt");
//            if(!file.exists()) {
//                Optional<Project> optionalProject = projectRepository.findById(projectId);
//                if(!optionalProject.isPresent()) {
//                    throw new MyException(ResultEnum.NO_OBJECT);
//                }
//                Project project = optionalProject.get();
//                List<Layer> layers = project.getLayers();
//                for (Layer layer : layers) {
//                    if(layer.getId().equals(sectionId)) {
//                        List<Section> sections = layer.getSections();
//                        for(Section section : sections) {
//                            if(section.getSectionId().equals(sectionId)) {
//                                if(section.getState() == 0) {
//                                    throw new MyException(-99, "断面计算中");
//                                } else {
//                                    section.setState(-1);
//                                    projectRepository.save(project);
//                                    throw new MyException(ResultEnum.NO_OBJECT);
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//            BufferedReader bufferedReader = null;
//            Map map = new HashMap();
//            map.put("dem", str);
//            List<String> list = new ArrayList<>();
//            try {
//                bufferedReader = new BufferedReader(new FileReader(file));
//                String temp = bufferedReader.readLine();
//                while(temp != null) {
//                    if(temp.contains("+")) {
//                        list.add("0");
//                    } else {
//                        list.add(temp);
//                    }
//                    temp = bufferedReader.readLine();
//                }
//                map.put("list", list);
//                result.add(map);
//                bufferedReader.close();
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
//            } finally {
//                try {
//                    if(bufferedReader != null) {
//                        bufferedReader.close();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
//                }
//            }
//        }
//        return result;
//    }
//
//    @Override
//    public Project getProjectInfo(String projectId) {
//        Optional<Project> optionalProject = projectRepository.findById(projectId);
//        if(!optionalProject.isPresent()) {
//            throw new MyException(ResultEnum.NO_OBJECT);
//        }
//        Project project = optionalProject.get();
//        return project;
//    }
//
//    @Override
//    public Page<Project> getAll(int page, int size, String keyWord) {
//        Pageable pageable = PageRequest.of(page, size);
//        if (keyWord.equals("")) {
//            Page<Project> projects = projectRepository.findAll(pageable);
//            return projects;
//        } else {
//            ExampleMatcher exampleMatcher = ExampleMatcher.matching()
//                    .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
//                    .withIgnoreCase(true);
//            Project project = new Project();
//            project.setProjectName(keyWord);
//            Example<Project> projectExample = Example.of(project, exampleMatcher);
//            Page<Project> projects = projectRepository.findAll(projectExample, pageable);
//            return projects;
//        }
//
//    }
//
//    @Override
//    public List<Project> getProjectsByEmail(String email) {
//        return projectRepository.findAllByCreator(email);
//    }
//
//    @Override
//    public int checkState(String projectId, String layerId) {
//        Optional<Project> optionalProject = projectRepository.findById(projectId);
//        if(!optionalProject.isPresent()) {
//            throw new MyException(ResultEnum.USER_PASSWORD_NOT_MATCH);
//        }
//        Project project = optionalProject.get();
//        List<Layer> layers = project.getLayers();
//        for(Layer layer : layers) {
//            if(layer.getId().equals(layerId)) {
//                List<Section> sections = layer.getSections();
//                for (Section section : sections) {
//                    if (section.getState() == 0) {
//                        return 0;
//                    }
//                }
//                return 1;
//            }
//        }
//        throw new MyException(ResultEnum.NO_OBJECT);
//    }
//
//    @Override
//    public void delLayer(String projectId, String layerId, String email) {
//        Optional<Project> optionalProject = projectRepository.findById(projectId);
//        if(!optionalProject.isPresent()) {
//            throw new MyException(ResultEnum.NO_OBJECT);
//        }
//        Project project = optionalProject.get();
//        List<Layer> layers = project.getLayers();
//        int index = -1;
//        for (int i = 0; i < layers.size(); i++) {
//            if(layers.get(i).getId().equals(layerId)) {
//                index = i;
//                break;
//            }
//        }
//        List<Section> sections = layers.get(index).getSections();
//        String type = layers.get(index).getType();
//        layers.remove(index);
//        projectRepository.save(project);
//        if(type.equals("section")) {
//            for(Section section: sections) {
//                String path = baseDir + email + "\\projects\\" + projectId + "\\" + section.getId() + ".txt";
//                File file = new File(path);
//                if(file.exists()) {
//                    file.delete();
//                }
//            }
//        }
//    }
//
//    @Override
//    public String getFlushId(String projectId, String benchmark, String reference, String name) {
//        String id = analyticDataSetMapper.findIdByBenchmarkAndReference(benchmark, reference);
//        if(id == null) {
//            throw new MyException(ResultEnum.NO_OBJECT);
//        }
//        Layer layer = new Layer();
//        layer.setId(id);
//        layer.setType("flush");
//        layer.setName(name);
//        Optional<Project> optionalProject = projectRepository.findById(projectId);
//        if(!optionalProject.isPresent()) {
//            throw new MyException(ResultEnum.NO_OBJECT);
//        }
//        Project project = optionalProject.get();
//        project.getLayers().add(layer);
//        projectRepository.save(project);
//        return id;
//    }
//
//    @Override
//    public String computeContour(String projectId, String demId, String email, String interval, String shpName, String srid) {
//        Map<String, Object> demMap = analyticDataSetMapper.findById(demId);
//        if(demMap == null) {
//            throw new MyException(ResultEnum.NO_OBJECT);
//        }
//
//        String address = (String) demMap.get("address");
//        String fileName = (String) demMap.get("file_name");
//        String uuid = UUID.randomUUID().toString();
//        String resultPath = baseDir + email + "\\" + "projects\\" + projectId + "\\" + shpName + ".shp";
//        redisService.set(uuid, 0, 24*7*60l);
//
//        new Thread() {
//            @Override
//            @SneakyThrows
//            public void run() {
//                Process process = AnalyseUtil.createContour(contourDir, interval, address + "\\" + fileName, resultPath);
//                int code = process.waitFor();
//                if(code == 0) {
//                    while(!AnalyseUtil.shpFileIsExist(baseDir + email + "\\" + "projects\\" + projectId, shpName)) {
//                        Thread.currentThread().sleep(300);
//                    }
//                    Process process1 = AnalyseUtil.uploadShpToDataBase(shp2pgsqlDir, baseDir + email + "\\" + "projects\\" + projectId, shpName, srid);
//                    int code1 = process1.waitFor();
//                    if(code1 == 0) {
//                        Optional<Project> optionalProject = projectRepository.findById(projectId);
//                        if(!optionalProject.isPresent()) {
//                            throw new MyException(ResultEnum.NO_OBJECT);
//                        }
//                        Project project = optionalProject.get();
//                        Layer layer = new Layer();
//                        layer.setId(uuid);
//                        layer.setName(shpName);
//                        layer.setType("contour");
//                        project.getLayers().add(layer);
//                        projectRepository.save(project);
//                        redisService.set(uuid, 1, 3*60l);
//                    } else {
//                        redisService.set(uuid, -1, 3*60l);
//                    }
//                } else {
//                    redisService.set(uuid, -1, 3*60l);
//                }
//            }
//        }.start();
//        return uuid;
//    }
//
//    @Override
//    public int checkContourState(String uid) {
//        int state = (int) redisService.get(uid);
//        if(state == 1 || state == -1) {
//            redisService.del(uid);
//        }
//        return state;
//    }
//
//    @Override
//    public void sortLayer(String projectId, List<Layer> layers) {
//        Optional<Project> optionalProject = projectRepository.findById(projectId);
//        if(!optionalProject.isPresent()) {
//            throw new MyException(ResultEnum.NO_OBJECT);
//        }
//        Project project = optionalProject.get();
//        project.setLayers(layers);
//        projectRepository.save(project);
//    }
//
//    @Override
//    public String addRegion(String projectId, JSONArray jsonArray, String demId, String email) {
//        Optional<Project> optionalProject = projectRepository.findById(projectId);
//        if(!optionalProject.isPresent()) {
//            throw new MyException(ResultEnum.NO_OBJECT);
//        }
//        Project project = optionalProject.get();
//        Map<String, Object> map = analyticDataSetMapper.findById(demId);
//        String name = (String) map.get("name");
//        String rasterAddress = (String) map.get("address") +"\\" + map.get("file_name");
//        String uuid = UUID.randomUUID().toString();
//        String outputPng = baseDir + email + "\\projects\\" + projectId + "\\" + uuid + ".png";
//        String outputJson = baseDir + email + "\\projects\\" + projectId + "\\" + uuid + ".json";
//        new Thread() {
//            @Override
//            @SneakyThrows
//            public void run() {
//                redisService.set(uuid, 0,60l);
//                String tempPath = baseDir + email + "\\temp\\" + uuid + ".txt";
//                Process process = AnalyseUtil.rasterCrop(tempPath, rasterAddress, outputPng, outputJson, jsonArray);
//                int code = process.waitFor();
//                if(code == 0) {
//                    redisService.set(uuid, 1);
//                    Layer layer = new Layer();
//                    layer.setId(uuid);
//                    layer.setName(name + "_形态");
//                    layer.setType("region");
//                    layer.setPoints(AnalyseUtil.getPoints(outputJson));
//                    project.getLayers().add(layer);
//                    projectRepository.save(project);
//                } else {
//                    redisService.set(uuid, -1);
//                }
//                LocalUploadUtil.deleteFolder(tempPath);
//            }
//        }.start();
//        return uuid;
//    }
//
//    @Override
//    public Layer getRegionLayer(String projectId, String layerId) {
//        Optional<Project> optionalProject = projectRepository.findById(projectId);
//        if(!optionalProject.isPresent()) {
//            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
//        }
//        Project project = optionalProject.get();
//        List<Layer> layers = project.getLayers();
//        for(Layer layer : layers) {
//            if(layer.getId().equals(layerId)) {
//                return layer;
//            }
//        }
//        throw new MyException(ResultEnum.NO_OBJECT);
//    }
//
//    @Override
//    public int checkAddRegion(String key) {
//        int code = (int) redisService.get(key);
//        if(code == 1 || code == -1) {
//            redisService.del(key);
//        }
//        return code;
//    }
//
//    @Override
//    public void getRegion(String projectId, String layerId, String email, HttpServletResponse response) {
//        String address = baseDir + email + "\\projects\\" + projectId + "\\" + layerId + ".png";
//        File file = new File(address);
//        if(!file.exists()) {
//            throw new MyException(ResultEnum.NO_OBJECT);
//        }
//        InputStream inputStream = null;
//        try {
//            inputStream = new FileInputStream(file);
//            ServletOutputStream servletOutputStream = response.getOutputStream();
//            byte[] bytes = new byte[1024];
//            int len = inputStream.read(bytes);
//            while(len != -1) {
//                servletOutputStream.write(bytes);
//                len = inputStream.read(bytes);
//            }
//            inputStream.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
//        } finally {
//            try {
//                if (inputStream != null) {
//                    inputStream.close();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
//            }
//        }
//    }
//
//
//    @Override
//    public void getRegion(String pngName, String email, HttpServletResponse response) {
//        String address = baseDir + email + "\\Tif\\" + pngName + ".png";
//        File file = new File(address);
//        if (!file.exists()) {
//            throw new MyException(ResultEnum.NO_OBJECT);
//        }
//        InputStream inputStream = null;
//        try {
//            inputStream = new FileInputStream(file);
//            ServletOutputStream servletOutputStream = response.getOutputStream();
//            byte[] bytes = new byte[1024];
//            int len = inputStream.read(bytes);
//            while (len != -1) {
//                servletOutputStream.write(bytes);
//                len = inputStream.read(bytes);
//            }
//            inputStream.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
//        } finally {
//            try {
//                if (inputStream != null) {
//                    inputStream.close();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
//            }
//        }
//    }
}
