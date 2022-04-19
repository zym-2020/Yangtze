package njnu.edu.back.common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/03/28/21:03
 * @Description:
 */
@AllArgsConstructor
@Getter
public enum ResultEnum {
    SUCCESS(0, "成功"),
    DEFAULT_EXCEPTION(-1, "默认的服务器内部异常，我并不想进行处理！！"),
    NO_OBJECT(-2, "没有对应的对象"),//没有对应的对象
    EXIST_OBJECT(-3, "对象已存在"),//对象已存在
    NO_TOKEN(-4, "Missing Token"),
    TOKEN_WRONG(-5, "Token Wrong"),
    USER_PASSWORD_NOT_MATCH(-6, "账户名和密码不匹配"),//账户名和密码不匹配
    QUERY_TYPE_ERROR(-7, "查询类型不支持"),//查询类型不支持
    REMOTE_SERVICE_ERROR(-8, "远程服务调用出错"),//远程服务调用出错
    DUPLICATE_NAME_ERROR(-9, "文件重名！"),


    ;
    private Integer code;

    private String msg;
}
