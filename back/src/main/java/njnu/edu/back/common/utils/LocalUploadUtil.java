package njnu.edu.back.common.utils;

import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.result.ResultEnum;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.channels.FileChannel;
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

    public static int merge(String from, String to, int total) {
        File t = new File(to);
        FileInputStream in = null;
        FileChannel inChannel = null;
        FileOutputStream out = null;
        FileChannel outChannel = null;
        try {
            out = new FileOutputStream(t, true);
            outChannel = out.getChannel();
            long start = 0;
            for(int i = 0; i < total; i++) {
                File file = new File(from + "\\" + Integer.toString(i));
                in = new FileInputStream(file);
                inChannel = in.getChannel();

                // 从inChannel中读取file.length()长度的数据，写入outChannel的start处
                outChannel.transferFrom(inChannel, start, file.length());
                start += file.length();
                in.close();
                inChannel.close();
            }
            out.close();
            outChannel.close();
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            try {
                if(in != null) {
                    in.close();
                }
                if(out != null) {
                    out.close();
                }
                if(inChannel != null) {
                    inChannel.close();
                }
                if(outChannel != null) {
                    outChannel.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        }
    }

    public static boolean DeleteFolder(String path) {
        File file = new File(path);
        if(!file.exists()) {
            return false;
        } else {
            if(file.isFile()) {
                return deleteFile(path);
            } else {
                return deleteDirectory(path);
            }
        }
    }

    private static boolean deleteFile(String path) {
        File file = new File(path);
        if(file.exists() && file.isFile()) {
            file.delete();
            return true;
        }
        return false;
    }

    private static boolean deleteDirectory(String path) {
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!path.endsWith(File.separator)) {
            path = path + File.separator;
        }
        File dirFile = new File(path);
        //如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        File[] files = dirFile.listFiles();
        for(File f : files) {
            if(f.isFile()) {
                flag = deleteFile(f.getAbsolutePath());
                if(!flag) break;
            } else {
                flag = deleteDirectory(f.getAbsolutePath());
                if(!flag) break;
            }
        }
        if(!flag) return false;

        if(dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }


}
