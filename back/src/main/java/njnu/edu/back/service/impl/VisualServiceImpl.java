package njnu.edu.back.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.result.ResultEnum;
import njnu.edu.back.common.utils.TileUtil;
import njnu.edu.back.dao.main.FileMapper;
import njnu.edu.back.dao.main.VisualFileMapper;
import njnu.edu.back.dao.shp.VectorTileMapper;
import njnu.edu.back.pojo.VisualFile;
import njnu.edu.back.pojo.support.TileBox;
import njnu.edu.back.service.VisualService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/08/22/14:31
 * @Description:
 */
@Repository
public class VisualServiceImpl implements VisualService {

    @Value("${pictureAddress}")
    String pictureAddress;

    @Value("${visualAddress}")
    String visualAddress;

    @Value("${basePath}")
    String basePath;

    @Autowired
    VisualFileMapper visualFileMapper;

    @Autowired
    VectorTileMapper vectorTileMapper;

    @Autowired
    FileMapper fileMapper;

    @Override
    public void getAvatar(String fileName, HttpServletResponse response) {
        InputStream in = null;
        ServletOutputStream sos = null;
        try {
            in = new FileInputStream(pictureAddress + fileName);
            sos = response.getOutputStream();
            byte[] bytes = new byte[1024];
            while((in.read(bytes)) > -1) {
                sos.write(bytes);
            }
            sos.flush();
            sos.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        } finally {
            try {
                if(in != null) {
                    in.close();
                }
                if (sos != null) {
                    sos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
            }
        }
    }

    @Override
    public void getRaster(String visualId, int x, int y, int z, HttpServletResponse response) {
        Map<String, Object> raster = visualFileMapper.findById(visualId);
        y = (int) Math.pow(2, z) - y - 1;
        String path = visualAddress + raster.get("content") + "\\" + z + "\\" + x + "\\" + y + ".png";
        InputStream in = null;
        ServletOutputStream sos = null;
        try {
            response.setContentType("image/png");
            sos = response.getOutputStream();
            File file = new File(path);
            if(!file.exists()) {
                in = new FileInputStream(visualAddress + "blank.png");
            } else {
                in = new FileInputStream(path);
            }
            byte[] bytes = new byte[1024];
            while((in.read(bytes)) > -1) {
                sos.write(bytes);
            }
            sos.flush();
            sos.close();
            in.close();
        } catch (Exception e) {
//            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        } finally {
            try {
                if(in != null) {
                    in.close();
                }
                if (sos != null) {
                    sos.close();
                }
            } catch (Exception e) {
//                throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
            }
        }
    }

    @Override
    public void getVectorTiles(String visualId, int x, int y, int z, HttpServletResponse response) {
        Map<String, Object> map = visualFileMapper.findById(visualId);
        TileBox tileBox = TileUtil.tile2boundingBox(x, y, z, (String) map.get("content"));
        tileBox.setVisualId(visualId);
        byte[] bytes;
        if(map.get("type").equals("pointVectorTile") || map.get("type").equals("lineVectorTile") || map.get("type").equals("polygonVectorTile")) {
            bytes = (byte[]) vectorTileMapper.getVictorTile(tileBox);
        } else {
            bytes = (byte[]) vectorTileMapper.getVictorTile3D(tileBox);
        }
        ServletOutputStream sos = null;
        try {
            response.setContentType("application/octet-stream");
            sos = response.getOutputStream();
            sos.write(bytes);
        } catch (Exception e) {

        } finally {
            try {
                if(sos != null) {
                    sos.close();
                }
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void getPhoto(String fileId, HttpServletResponse response) {
        Map<String, Object> map = fileMapper.findById(fileId);
        InputStream in = null;
        ServletOutputStream sos = null;
        try {
            in = new FileInputStream(basePath + map.get("address"));
            sos = response.getOutputStream();
            byte[] bytes = new byte[1024];
            while((in.read(bytes)) > -1) {
                sos.write(bytes);
            }
            sos.flush();
            sos.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        } finally {
            try {
                if(in != null) {
                    in.close();
                }
                if (sos != null) {
                    sos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
            }
        }
    }

    @Override
    public JSONArray getCoordinates(String visualId) {
        Map<String, Object> map = visualFileMapper.findById(visualId);
        String content = (String) map.get("content");
        JSONObject jsonObject = JSON.parseObject(content);
        return jsonObject.getJSONArray("coordinates");
    }

    @Override
    public void getPngResource(String visualId, HttpServletResponse response) {
        Map<String, Object> map = visualFileMapper.findById(visualId);
        String content = (String) map.get("content");
        JSONObject jsonObject = JSON.parseObject(content);
        String address = visualAddress + jsonObject.getString("address");
        InputStream in = null;
        ServletOutputStream sos = null;
        try {
            in = new FileInputStream(address);
            sos = response.getOutputStream();
            response.setContentType("image/png");
            byte[] bytes = new byte[1024];
            while((in.read(bytes)) > -1) {
                sos.write(bytes);
            }
            sos.flush();
            sos.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (sos != null) {
                    sos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
            }
        }
    }

    @Override
    public void addVisualFile(VisualFile visualFile) {
        visualFileMapper.addVisualFile(visualFile);
    }
}
