package njnu.edu.back.common.exception;

import lombok.Data;
import njnu.edu.back.common.result.ResultEnum;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/03/28/21:03
 * @Description:
 */
@Data
public class MyException extends RuntimeException {
    Integer code;
    String msg;

    public MyException() {
        super();
    }

    public MyException(ResultEnum resultEnum) {
        super();
        this.code = resultEnum.getCode();
        this.msg = resultEnum.getMsg();
    }

    public MyException(Integer code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
    }
}
