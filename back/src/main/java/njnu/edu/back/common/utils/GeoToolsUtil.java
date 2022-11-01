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
