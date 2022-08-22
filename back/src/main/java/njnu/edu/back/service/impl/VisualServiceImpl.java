package njnu.edu.back.service.impl;

import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.result.ResultEnum;
import njnu.edu.back.common.utils.TileUtil;
import njnu.edu.back.dao.main.VisualFileMapper;
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

    @Autowired
    VisualFileMapper visualFileMapper;

    @Override
    public void getAvatar(String fileName, HttpServletResponse response) {
        InputStream in = null;
        ServletOutputStream sos = null;
        try {
            response.setContentType("image/png");
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
        String path = raster.get("content") + "\\" + z + "\\" + x + "\\" + y + ".png";
        InputStream in = null;
        ServletOutputStream sos = null;
        try {
            response.setContentType("image/png");
            sos = response.getOutputStream();
            File file = new File(path);
            if(!file.exists()) {
                sos.write(new byte[] {});
                sos.close();
                return;
            }
            in = new FileInputStream(path);
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
    public void getVectorTiles(String visualId, int x, int y, int z, HttpServletResponse response) {
        Map<String, Object> map = visualFileMapper.findById(visualId);
        TileBox tileBox = TileUtil.tile2boundingBox(x, y, z, (String) map.get("fileName"));
        byte[] bytes = (byte[]) visualFileMapper.getVectorTiles(tileBox);
        ServletOutputStream sos = null;
        try {
            response.setContentType("application/octet-stream");
            sos = response.getOutputStream();
            sos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        } finally {
            try {
                if(sos != null) {
                    sos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
            }
        }
    }

    @Override
    public void getPhoto(String visualId, HttpServletResponse response) {
        Map<String, Object> map = visualFileMapper.findById(visualId);
        InputStream in = null;
        ServletOutputStream sos = null;
        try {
            in = new FileInputStream((String) map.get("content"));
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
}
