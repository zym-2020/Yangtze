package njnu.edu.back.common.utils;

import com.thoughtworks.xstream.XStream;
import njnu.edu.back.pojo.ModelConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/05/20/22:24
 * @Description:
 */
public class XmlUtil {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public static String toXml(Object obj) {
        XStream xStream = new XStream();
        xStream.autodetectAnnotations(true);    //自动发现注解
        return xStream.toXML(obj);
    }

    public static <T> T fromXml(InputStream is, Class<T> target) {
        XStream xStream = new XStream();
        XStream.setupDefaultSecurity(xStream);
        xStream.allowTypes(new Class[]{ModelConfig.class});
        xStream.processAnnotations(target);
        return (T) xStream.fromXML(is);
    }

    public static <T> T fromXml(File file, Class<T> target) {
        XStream xStream = new XStream();
        XStream.setupDefaultSecurity(xStream);
        xStream.allowTypes(new Class[]{ModelConfig.class});
        xStream.processAnnotations(target);
        return (T) xStream.fromXML(file);
    }

    public static <T> T fromXml(String xml, Class<T> target) {
        XStream xStream = new XStream();
        XStream.setupDefaultSecurity(xStream);
        xStream.allowTypes(new Class[]{ModelConfig.class});
        xStream.processAnnotations(target);
        return (T) xStream.fromXML(xml);
    }
}
