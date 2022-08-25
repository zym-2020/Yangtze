package njnu.edu.back.dao.main;

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

    /**
    * @Description:方便录入数据写的方法
    * @Author: Yiming
    * @Date: 2022/8/23
    */
    Map<String, Object> findByFileName(@Param("fileName") String fileName);

    int addFile(File file);

    List<Map<String, Object>> findByFolderId(@Param("folderId")String folderId, @Param("email") String email);

    void rename(@Param("id") String id, @Param("fileName") String fileName);

    void batchDelete(@Param("list") List<String> list);

    void batchDeleteFolder(@Param("list") List<String> list);

    Map<String, Object> findById(@Param("id") String id);

    List<Map<String, Object>> findListById(@Param("list") List<String> list);

//    int batchInsert(List<File> fileList);
//
//    List<Map<String, Object>> selectFolder(@Param("email") String email);
//
//    int updateFolderParentIdAndLevel(@Param("list") List<String> list, @Param("parentId") String parentId, @Param("levelDifference") int levelDifference);
//
//    int updateFileParentIdAndLevel(@Param("list") List<String> list, @Param("parentId") String parentId, @Param("levelDifference") int levelDifference);
//
//    List<Map<String, Object>> selectFolderPath(@Param("list") List<String> id);
//
//    List<Map<String, Object>> selectFilePath(@Param("list") List<String> id);
}
