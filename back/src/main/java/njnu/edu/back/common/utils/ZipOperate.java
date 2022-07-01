package njnu.edu.back.common.utils;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.io.inputstream.ZipInputStream;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.progress.ProgressMonitor;
import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.result.ResultEnum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.util.*;


/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/06/08/14:52
 * @Description:
 */
public class ZipOperate {

    private static Logger logger = LoggerFactory.getLogger(CodeDetector.class);

    public static void getPath(String filePath, String destination, List<Map<String, String>> folderList, List<Map<String, String>> fileList) {
        FileInputStream input = null;
        BufferedInputStream bis = null;
        try {
            input = new FileInputStream(filePath);
            bis = new BufferedInputStream(input);
            String code = CodeDetector.getEncode(bis, false);
            ZipFile zipFile = new ZipFile(filePath);
            zipFile.setCharset(Charset.forName(code));

            List<FileHeader> list = zipFile.getFileHeaders();
            for(FileHeader fileHeader : list) {
                String fileName = fileHeader.getFileName();
                // fileName会将目录单独读出来，而且带有路径分割符
                // 此处还存在有些中文乱码的问题，暂没找到好的解决方法
                if (fileName.endsWith("/") || fileName.endsWith("\\\\") || fileName.endsWith("\\")) {
                    Map<String, String> map = new HashMap<>();
                    map.put("name", fileName);
                    map.put("id", UUID.randomUUID().toString());
                    folderList.add(map);
                    continue;
                }else {
                    String[] strs = fileName.split("/");
                    String uuid = UUID.randomUUID().toString();
                    String suffix = strs[strs.length - 1].substring(strs[strs.length - 1].lastIndexOf(".") + 1);
                    zipFile.extractFile(fileHeader, destination, uuid + "." + suffix);
                    Map<String, String> map = new HashMap<>();
                    map.put("name", fileName);
                    map.put("id", uuid);
                    fileList.add(map);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            delUnPackFile(fileList, destination);
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        } finally {
            try {
                if(input != null) {
                    input.close();
                }
                if(bis != null) {
                    bis.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
            }
        }
    }

    public static void delUnPackFile(List<Map<String, String>> fileList, String destination) {
        for(Map<String, String> map : fileList) {
            String fileName = map.get("name");
            String[] strs = fileName.split("/");
            String suffix = strs[strs.length - 1].substring(strs[strs.length - 1].lastIndexOf(".") + 1);
            LocalUploadUtil.deleteFolder(destination + "\\" + map.get("id") + "." +suffix);
        }
    }

    public static void compressFile(String destination, List<Map<String, Object>> fileList) {
        ZipFile zipFile = new ZipFile(destination);
        ProgressMonitor progressMonitor = zipFile.getProgressMonitor();
//        zipFile.setRunInThread(true);
        try {
            List<File> files = new ArrayList<>();
            Map<String, String> maps = new HashMap<>();
            for(Map<String, Object> map : fileList) {
                String address = (String) map.get("address");
                String name = address.split("\\\\")[address.split("\\\\").length - 1];
                files.add(new File(address));
                maps.put(name, (String) map.get("path"));
            }
            zipFile.addFiles(files);
            zipFile.renameFiles(maps);
//            while (!progressMonitor.getState().equals(ProgressMonitor.State.READY)) {
//                System.out.println("Percentage done: " + progressMonitor.getPercentDone());
//                System.out.println("Current file: " + progressMonitor.getFileName());
//                System.out.println("Current task: " + progressMonitor.getCurrentTask());
//
//                Thread.sleep(100);
//            }
//            if (progressMonitor.getResult().equals(ProgressMonitor.Result.SUCCESS)) {
//                System.out.println("Successfully added folder to zip");
//            } else if (progressMonitor.getResult().equals(ProgressMonitor.Result.ERROR)) {
//                System.out.println("Error occurred. Error message: " + progressMonitor.getException().getMessage());
//            } else if (progressMonitor.getResult().equals(ProgressMonitor.Result.CANCELLED)) {
//                System.out.println("Task cancelled");
//            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        }

    }


}
