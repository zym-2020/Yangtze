package njnu.edu.back.pojo.support.projectJson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/04/16/21:56
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Analyse {
    AnalyseResource section;       //断面形态0
    AnalyseResource sectionContrast;   //断面比较1
    AnalyseResource area;              //断面面积冲淤2
    AnalyseResource boundary;          //边界分析3
    AnalyseResource branch;        //汊道断面比较4
    AnalyseResource deep;          //冲淤等深线5
    AnalyseResource deepContrast;      //等深线比较6
    AnalyseResource elev;          //特定高程冲淤7
    AnalyseResource line;          //深泓线比较8
    AnalyseResource slope;         //河床坡度提取9
    AnalyseResource volume;        //河道容积计算10
    AnalyseResource anyArea;       //任意区域冲淤11
}
