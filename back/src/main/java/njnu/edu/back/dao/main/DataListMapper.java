package njnu.edu.back.dao.main;

import njnu.edu.back.pojo.DataList;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/08/19/17:02
 * @Description:
 */
@Repository
public interface DataListMapper {
    void addDataList(@Param("dataList") DataList dataList);

    void updateDataList(@Param("id") String id, @Param("dataList") DataList dataList);

    Map<String, Object> getFileInfo(@Param("id") String id);

    Map<String, Object> getFileInfoAndUserInfo(@Param("id") String id);

    void addWatchCount(@Param("id") String id);

    void addDownloadCount(@Param("id") String id);

    List<Map<String, Object>> fuzzyQuery(@Param("start") int start, @Param("size") int size, @Param("keyword") String keyword, @Param("tags") String[] tags, @Param("property") String property, @Param("flag") Boolean flag, @Param("status") int status);

    int countFuzzyQuery(@Param("keyword") String keyword, @Param("tags") String[] tags, @Param("status") int status);

    void deleteById(@Param("id") String id);

    List<Map<String, Object>> pageQueryByEmail(@Param("email") String email, @Param("size") int size, @Param("start") int start);

    int countPageQueryByEmail(@Param("email") String email);

    void updateStatusById(@Param("id") String id, @Param("status") int status);
}
