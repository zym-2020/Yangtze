package njnu.edu.back.dao.main;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/04/07/17:31
 * @Description:
 */
@Repository
public interface ProjectMapper {
    String addProject(@Param("projectName") String projectName, @Param("email") String email, @Param("avatar") String avatar, @Param("layerManage") String[] layerManage, @Param("basemap") String basemap, @Param("isPublic") Boolean isPublic);

    List<Map<String, Object>> fuzzyQuery(@Param("keyword") String keyword, @Param("size") int size, @Param("start") int start, @Param("flag") int flag);

    List<Map<String, Object>> fuzzyQueryByEmail(@Param("email") String email, @Param("size") int size, @Param("start") int start, @Param("status") int status);

    int fuzzyCountByEmail(@Param("email") String email, @Param("status") int status);

    int fuzzyCount(@Param("keyword") String keyword, @Param("flag") int flag);

    Map<String, Object> getProjectInfo(@Param("projectId") String projectId);

    void updateLayer(@Param("projectId") String projectId, @Param("layers") String[] layers);

    List<Map<String, Object>> getLayersInfo(@Param("list") List<String> list);

    void updateBasemap(@Param("projectId") String projectId, @Param("basemap") String basemap);

    void updatePublicState(@Param("projectId") String projectId, @Param("flag") boolean flag);

    String updateProjectInfo(@Param("projectId") String projectId, @Param("projectName") String projectName, @Param("isPublic") boolean isPublic, @Param("avatar") String avatar);

    void deleteProject(@Param("projectId") String projectId);


}
