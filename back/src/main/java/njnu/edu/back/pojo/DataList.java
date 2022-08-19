package njnu.edu.back.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/08/19/17:01
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataList {
    String id;
    String name;
    String[] location;
    String description;
    String[] tags;
    String creator;
    Date createTime;
    Date updateTime;
    Integer download;
    Integer watch;
    String avatar;
    Integer status;
    String thumbnail;

    String provider;
    String time;
    String range;
    String type;
    String providerPhone;
    String providerEmail;
    String providerAddress;
    Boolean getOnline;
    String detail;
}
