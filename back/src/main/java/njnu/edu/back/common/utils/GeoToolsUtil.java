package njnu.edu.back.common.utils;

import cn.hutool.json.JSONArray;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.io.WKTReader;
import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.result.ResultEnum;

import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;

import org.geotools.geometry.jts.JTSFactoryFinder;

import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/04/15/11:01
 * @Description:
 */

public class GeoToolsUtil {

    public static void json2shape(JSONArray jsonArray, String fileName, String path, String type) {
        File dirFile = new File(path);
        if(!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File shape = new File(path + "\\" + fileName + ".shp");
        generateCpgFile(path, fileName);
        try {
            Map<String, Serializable> param = new HashMap<>();
            param.put(ShapefileDataStoreFactory.URLP.key, shape.toURI().toURL());
            ShapefileDataStore shapefileDataStore = (ShapefileDataStore) new ShapefileDataStoreFactory().createNewDataStore(param);
            shapefileDataStore.setCharset(StandardCharsets.UTF_8);
            SimpleFeatureTypeBuilder simpleFeatureTypeBuilder = new SimpleFeatureTypeBuilder();
            simpleFeatureTypeBuilder.setCRS(DefaultGeographicCRS.WGS84);
            simpleFeatureTypeBuilder.setName("shapefile");
            switch (type) {
                case "points":
                    simpleFeatureTypeBuilder.add("the_geom", MultiPoint.class);
                    break;
                case "lines":
                    simpleFeatureTypeBuilder.add("the_geom", MultiLineString.class);
                    break;
                case "polygons":
                    simpleFeatureTypeBuilder.add("the_geom", MultiPolygon.class);
                    break;
                default:
                    throw new MyException(-1, "空间字段类型错误");
            }
            shapefileDataStore.createSchema(simpleFeatureTypeBuilder.buildFeatureType());
            FeatureWriter<SimpleFeatureType, SimpleFeature> writer = shapefileDataStore.getFeatureWriter(shapefileDataStore.getTypeNames()[0], Transaction.AUTO_COMMIT);
            generateCoordinateData(jsonArray, writer, type);
            writer.write();
            writer.close();
            shapefileDataStore.dispose();
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(-1, "生成shape文件出错");
        }
    }

    /**
    * @Description:创建cpg文件对象,设置字符编码
    * @Author: Yiming
    * @Date: 2022/4/15
    */
    
    private static void generateCpgFile(String path, String fileName) {
        File file = new File(path + "\\" + fileName + ".cpg");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write("GBK,GB2312,UTF-8");
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(-1, "创建cpg文件时出错");
        }
    }

    private static void generateCoordinateData(JSONArray jsonArray, FeatureWriter<SimpleFeatureType, SimpleFeature> writer, String type) {
        try {
            SimpleFeature feature;
            WKTReader wktReader = new WKTReader(JTSFactoryFinder.getGeometryFactory());
            for(int i = 0;i < jsonArray.size();i++) {
                feature = writer.next();
                String wktText = jsonArray2wktString(jsonArray.get(i, JSONArray.class), type);
                Geometry geometry = wktReader.read(wktText);
                feature.setAttribute("the_geom", geometry);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new MyException(-1, "读取wktText时出错");
        }

    }

    private static String jsonArray2wktString(JSONArray jsonArray, String type) {
        String result = "";
        switch (type) {
            case "points":
                result = "MULTIPOINT(";
                for(int i = 0;i < jsonArray.size();i++) {
                    if(i < jsonArray.size() - 1)
                        result += jsonArray.get(i, JSONArray.class).get(0, String.class) + " " + jsonArray.get(i, JSONArray.class).get(1, String.class) + ",";
                    else
                        result += jsonArray.get(i, JSONArray.class).get(0, String.class) + " " + jsonArray.get(i, JSONArray.class).get(1, String.class);
                }
                result += ")";
                break;
            case "lines":
                result = "MULTILINESTRING(";
                for(int i = 0;i < jsonArray.size();i++) {
                    JSONArray temp = jsonArray.get(i, JSONArray.class);
                    result += "(";
                    for(int j = 0;j < temp.size();j++) {
                        if(j < temp.size() - 1)
                            result += temp.get(j, JSONArray.class).get(0, String.class) + " " + temp.get(j, JSONArray.class).get(1, String.class) + ",";
                        else
                            result += temp.get(j, JSONArray.class).get(0, String.class) + " " + temp.get(j, JSONArray.class).get(1, String.class);
                    }
                    result += ")";
                    if(i < jsonArray.size() - 1)
                        result += ",";
                }
                result += ")";
                break;
            case "polygons":
                result = "MULTIPOLYGON(";
                for(int i = 0;i < jsonArray.size();i++) {
                    JSONArray temp1 = jsonArray.get(i, JSONArray.class);
                    result += "(";
                    for(int j = 0;j < temp1.size();j++) {
                        JSONArray temp2 = temp1.get(i, JSONArray.class);
                        result += "(";
                        for(int k = 0;k < temp2.size();k++) {
                            if(k < temp2.size() -1)
                                result += temp2.get(k, JSONArray.class).get(0, String.class) + " " + temp2.get(k, JSONArray.class).get(1, String.class) + ",";
                            else
                                result += temp2.get(k, JSONArray.class).get(0, String.class) + " " + temp2.get(k, JSONArray.class).get(1, String.class);
                        }
                        result += ")";
                        if(j < temp1.size() - 1)
                            result += ",";
                    }
                    result += ")";
                    if(i < jsonArray.size() - 1)
                        result += ",";
                }
                result += ")";
                break;
        }
        return result;
    }

    /**
    * @Description:type转换
    * @Author: Yiming
    * @Date: 2022/4/16
    */

    public static String conversion(String type) {
        switch (type) {
            case "points":
                return "circle";
            case "lines":
                return "line";
            case "polygons":
                return "fill";
            default:
                throw new MyException(-1, "类型转换错误！");
        }
    }


}
