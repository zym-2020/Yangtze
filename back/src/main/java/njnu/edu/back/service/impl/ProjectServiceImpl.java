package njnu.edu.back.service.impl;

import com.alibaba.fastjson.JSONObject;
import njnu.edu.back.dao.ProjectMapper;
import njnu.edu.back.dao.UserMapper;
import njnu.edu.back.proj.dto.AddProject;
import njnu.edu.back.proj.support.projectJson.ProjectJsonBean;
import njnu.edu.back.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
