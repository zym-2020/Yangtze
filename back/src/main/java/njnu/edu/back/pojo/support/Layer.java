package njnu.edu.back.pojo.support;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/06/28/19:47
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Layer {
    String id;
    String type;            //BaseLayer || ResultLayer
    //    String dataType;        //raster || vector || geoJson
    String name;
    String tableName;   //针对shape
    String vectorType;   //针对shape
    JSONObject geoJson;
    String selectDemId;          //dem id针对断面形态
    String selectDemName;       //dem name针对断面形态
    List<String> selectDemIds;      //针对断面比较
    List<String> selectDemNames;    //针对断面比较
    Integer state;
}
