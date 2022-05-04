package njnu.edu.back.common.utils;

import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.result.ResultEnum;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/05/03/22:31
 * @Description:
 */
public class LocalUploadUtil {

    public static boolean checkMD5(String MD5, String dir) {
        String path = dir + "\\temp\\" + MD5;
        File file = new File(path);
        return file.exists();
    }

    public static List<String> getNoUploadChunk(String MD5, String dir, int total) {
        String path = dir + "\\temp\\" + MD5;
        File file = new File(path);
        List<String> result = new ArrayList<>();
        for(int i = 0; i < total; i++) {
            result.add(Integer.toString(i));
        }
        if(!file.exists()) {
            return result;
        }
        File[] fs = file.listFiles();
        for(File f : fs) {
            result.remove(f.getName());
        }
        return result;
    }

    public static void UploadFile(MultipartFile multipartFile, String name, String dir, String MD5) {
        String path = dir + "\\temp\\" + MD5;
        File file = new File(path);
        if(!file.exists()) {
            file.mkdir();
        }
        InputStream ins = null;
        FileOutputStream outs = null;
        try {
            ins = multipartFile.getInputStream();
            outs = new FileOutputStream(path + "\\" + name);
            int len;
            byte[] bytes = new byte[1024];
            while ((len = ins.read(bytes)) != -1) {
                outs.write(bytes, 0, len);
            }
            outs.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        } finally {
            try {
                if (outs != null) {
                    outs.close();
                }
                if (ins != null) {
                    ins.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
            }
        }

    }
}
