package njnu.edu.back.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import lombok.SneakyThrows;
import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.utils.GeoToolsUtil;
import njnu.edu.back.dao.VectorRelationshipMapper;
import njnu.edu.back.pojo.dto.AddVector;
import njnu.edu.back.service.RedisService;
import njnu.edu.back.service.VectorRelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/04/11/17:33
 * @Description:
 */
@Service
public class VectorRelationshipServiceImpl implements VectorRelationshipService {

    @Autowired
    VectorRelationshipMapper vectorRelationshipMapper;

    @Autowired
    RedisService redisService;

    @Value("${basedir}")
    String baseDir;

    @Override
    public JSONObject pageQuery(int size, int page) {
        int start = page * size;
        JSONObject jsonObject = new JSONObject();
        jsonObject.set("total", vectorRelationshipMapper.countAll());
        jsonObject.set("list", vectorRelationshipMapper.pageQuery(size, start));
        return jsonObject;
    }

    @Override
    public String newShape(JSONArray jsonArray, String fileName, String type, String email, String source, String projectName, String category, String meta) {
        String path = "";
        if(source.equals("analyse")) {
            path = baseDir + email + "\\" + "projects\\" + projectName + "\\" + fileName;
        } else if(source.equals("upload")) {
            path = baseDir + email + "\\upload\\shape\\" + fileName;
        }
        GeoToolsUtil.json2shape(jsonArray, fileName, path, type);
        UUID uuid = UUID.randomUUID();
        redisService.set(uuid.toString(), -1, 24*60l);          //计算中
        String finalPath = path;
        String finalName = fileName;
        new Thread() {
            @Override
            @SneakyThrows
            public void run() {
                ProcessBuilder processBuilder = new ProcessBuilder();
                List<String> commands = new ArrayList<>();
                commands.add("cmd.exe");
                commands.add("/c");
                commands.add("D:\\App\\postgresql\\bin\\shp2pgsql");
                commands.add("-c");
                commands.add(finalPath + "\\" + fileName + ".shp");
                commands.add(finalName);
                commands.add("|");
                commands.add("D:\\App\\postgresql\\bin\\psql");
                commands.add("-U");
                commands.add("postgres");
                commands.add("-d");
                commands.add("testdb");
                processBuilder.environment().put("PGPASSWORD", "123");
                processBuilder.command(commands);
                Process process = processBuilder.start();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    System.out.println(line);
                }
                int exitCode = process.waitFor();
                if(exitCode == 0) {
                    AddVector addVector = new AddVector(UUID.randomUUID().toString(), finalName, category, finalPath, meta, fileName + ".shp", email, source, GeoToolsUtil.conversion(type));
                    vectorRelationshipMapper.addVector(addVector);
                    redisService.set(uuid.toString(), addVector.getId().toString(), 24*60l);          //成功
                } else
                    redisService.set(uuid.toString(), -2, 24*60l);      //失败
            }

        }.start();
        return uuid.toString();
    }

    @Override
    public Map<String, Object> checkState(String uuid) {
        String state = (String) redisService.get(uuid);
        if(state == null) {
            throw new MyException(-1, "该数据可能已经上传完毕了");
        }
        if(Integer.parseInt(state) == -1) {
            return null;
        } else if(Integer.parseInt(state) == -2) {
            throw new MyException(-1, "矢量插入错误");
        } else {
            redisService.del(uuid);
            return vectorRelationshipMapper.queryAnalyseVector(state);
        }

    }
}
