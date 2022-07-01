package njnu.edu.back.repository;

import njnu.edu.back.pojo.Project;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/06/28/20:38
 * @Description:
 */
public interface ProjectRepository extends MongoRepository<Project, String> {
    List<Project> findAllByCreator(String email);
}
