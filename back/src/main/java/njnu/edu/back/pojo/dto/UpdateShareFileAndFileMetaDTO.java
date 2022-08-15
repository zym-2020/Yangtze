package njnu.edu.back.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/05/25/16:01
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateShareFileAndFileMetaDTO {
    String id;
    String provider;
    String time;
    String range;
    String detail;
    String type;
    String providerPhone;
    String providerEmail;
    String providerAddress;
    Boolean getOnline;
    String name;
    String description;
    String originName;
    String structuredName;
    String visualName;
    String originAddress;
    String visualSource;
    String visualType;
    String structuredSource;
    String[] tags;
    String avatar;
    String thumbnail;
}
