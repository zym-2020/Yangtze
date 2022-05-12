package njnu.edu.back.dao;

import njnu.edu.back.pojo.File;
import njnu.edu.back.pojo.dto.AddFileDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/05/02/15:34
 * @Description:
 */
@Repository
public interface FileMapper {
    int addFile(AddFileDTO addFileDTO);

    List<Map<String, Object>> findByLevel(@Param("level") int level, @Param("email") String email);

    List<Map<String, Object>> findByParentId(@Param("parentId")String parentId);

    void rename(@Param("id") String id, @Param("name") String name);

    void delete(@Param("id") String id);

    File findById(@Param("id") String id);
}
