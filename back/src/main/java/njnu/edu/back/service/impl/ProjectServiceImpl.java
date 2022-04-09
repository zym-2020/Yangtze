package njnu.edu.back.service.impl;

import njnu.edu.back.dao.ProjectMapper;
import njnu.edu.back.dao.UserMapper;
import njnu.edu.back.proj.dto.AddProject;
import njnu.edu.back.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
