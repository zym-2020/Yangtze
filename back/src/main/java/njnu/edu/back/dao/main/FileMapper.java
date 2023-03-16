package njnu.edu.back.dao.main;

import njnu.edu.back.pojo.File;
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

    /**
    * @Description:方便录入数据写的方法
    * @Author: Yiming
    * @Date: 2022/8/23
    */
    Map<String, Object> findByFileName(@Param("fileName") String fileName);

    int addFile(File file);

    List<Map<String, Object>> findByFolderId(@Param("folderId")String folderId, @Param("email") String email);

    void batchDelete(@Param("list") List<String> list);

    void batchDeleteFolder(@Param("list") List<String> list);

    List<Map<String, Object>> findListById(@Param("list") List<String> list);


    void updateVisualIdAndType(@Param("id") String id, @Param("visualId") String visualId, @Param("type") String type);

    Map<String, Object> findInfoById(@Param("id") String id);

    List<Map<String, Object>> findInfoListById(@Param("list") List<String> list);

    int getCountByEmail(@Param("email") String email);

    void updateVisualTypeAndVisualId(@Param("id") String id, @Param("visualType") String visualType, @Param("visualId") String visualId);

    List<Map<String, Object>> getVisualAuditFiles(@Param("keyword") String keyword, @Param("size") Integer size, @Param("start") Integer start);

    int getVisualAuditFilesCount(@Param("keyword") String keyword);

}
