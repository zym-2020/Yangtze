package njnu.edu.back.common.resolver;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/03/28/21:11
 * @Description:
 */
@Documented
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface JwtTokenParser {
    String value();
}
