package njnu.edu.back.common.utils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Zip2 {
    public static List read(String zipFilePath) {
        List lis=new ArrayList<> ();
        Map<String,Object> map;
        try {
            ZipFile zipFile = new ZipFile(zipFilePath,Charset.forName("GBK"));
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                map = new HashMap<String,Object>();
                ZipEntry entry = entries.nextElement();
                String name = entry.getName();
                long compressedSize = entry.getCompressedSize();
                long normalSize = entry.getSize();
                String type = entry.isDirectory() ? "文件夹" : "文件";
                map.put("名称",name);
                map.put("类型",type);
                map.put("压缩后大小",compressedSize);
                map.put("原始大小",normalSize);

                //System.out.print(name);
                lis.add(map);
                //System.out.format("\t 类型：%s；压缩后大小：%d；原始大小：%d\n", type, compressedSize, normalSize);
            }

            zipFile.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return lis;
    }
}
