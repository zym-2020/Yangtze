package njnu.edu.back.service;

import com.alibaba.fastjson.JSONArray;
import njnu.edu.back.pojo.VisualFile;

import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/08/22/14:30
 * @Description:
 */
public interface VisualService {
    void getAvatar(String fileName, HttpServletResponse response);

    void getRaster(String visualId, int x, int y, int z, HttpServletResponse response);

    void getVectorTiles(String visualId, int x, int y, int z, HttpServletResponse response);

    void getPhoto(String visualId, HttpServletResponse response);

    JSONArray getCoordinates(String visualId);

    void getPngResource(String visualId, HttpServletResponse response);

    void addVisualFile(VisualFile visualFile);
}
