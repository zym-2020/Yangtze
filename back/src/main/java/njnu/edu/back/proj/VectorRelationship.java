package njnu.edu.back.proj;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/04/06/17:34
 * @Description:矢量文件关系
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VectorRelationship {
    Integer id;
    String tableName;       //矢量文件对应数据库表名
    String description;
    String category;
    String address;         //矢量文件存储地址
    String fileName;
    String meta;
    String uploader;
    String source;
}