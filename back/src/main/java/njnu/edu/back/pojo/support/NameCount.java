package njnu.edu.back.pojo.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/07/01/22:09
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NameCount {
    Integer section;
    Integer sectionContrast;
    Integer area;
    Integer boundary;
    Integer branch;
    Integer deep;
    Integer deepContrast;
    Integer elev;
    Integer line;
    Integer slope;
    Integer volume;
    Integer anyArea;

    public NameCount(int count) {
        this.section = count;
        this.sectionContrast = count;
        this.area = count;
        this.boundary = count;
        this.branch = count;
        this.deep = count;
        this.deepContrast = count;
        this.elev = count;
        this.line = count;
        this.slope = count;
        this.volume = count;
        this.anyArea = count;
    }
}
