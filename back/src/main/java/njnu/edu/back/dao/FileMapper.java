package njnu.edu.back.dao;

import njnu.edu.back.proj.dto.AddFileDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

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

    List<Map<String, Object>> findByLevel(@Param("level") int level);

    List<Map<String, Object>> findByParentId(@Param("parentId")UUID parentId);
}
