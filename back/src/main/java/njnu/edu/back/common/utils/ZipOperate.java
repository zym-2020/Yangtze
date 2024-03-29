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
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.zip.ZipEntry;


/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/06/08/14:52
 * @Description:
 */
public class ZipOperate {

    public static void compressFile(String destination, List<Map<String, Object>> fileList) {
        ZipFile zipFile = new ZipFile(destination);
        try {
            List<File> files = new ArrayList<>();
            Map<String, String> maps = new HashMap<>();
            for(Map<String, Object> map : fileList) {
                String address = (String) map.get("address");
//                String name = address.split("\\\\")[address.split("\\\\").length - 1];
                String name = address.split("/")[address.split("/").length - 1];
                files.add(new File(address));
                maps.put(name, (String) map.get("fileName"));
            }
            zipFile.addFiles(files);
            zipFile.renameFiles(maps);

        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        }

    }

    public static void unpack(String destination, String to) {
        File file = new File(destination);
        if (!file.exists()) {
            throw new MyException(ResultEnum.DEFAULT_EXCEPTION);
        }
        ZipFile zipFile = new ZipFile(destination);
        try {
            zipFile.extractAll(to);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
