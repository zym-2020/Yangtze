package njnu.edu.back.dao;

import njnu.edu.back.proj.dto.AddProject;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/04/07/17:31
 * @Description:
 */
@Repository
public interface ProjectMapper {
    void addProject(AddProject addProject);
}
