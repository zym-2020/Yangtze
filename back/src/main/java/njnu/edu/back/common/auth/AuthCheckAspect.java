package njnu.edu.back.common.auth;

import njnu.edu.back.common.exception.MyException;
import njnu.edu.back.common.result.JsonResult;
import njnu.edu.back.common.result.ResultEnum;
import njnu.edu.back.common.utils.JwtTokenUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/03/28/20:56
 * @Description:
 */
@Aspect
@Component
public class AuthCheckAspect {
    @Autowired
    HttpServletRequest request;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Pointcut("@annotation(njnu.edu.back.common.auth.AuthCheck)")
    public void check() {};

    @Around("check()")
    public Object aroundCheck(ProceedingJoinPoint joinPoint) throws Throwable {
        String authorization = request.getHeader("authorization");
        if(authorization != null) {
            String token = authorization.substring(tokenHead.length());
            if(JwtTokenUtil.validateToken(token)) {
                if(JwtTokenUtil.tokenStatus(token) == 1) {
                    JsonResult result = (JsonResult) joinPoint.proceed();
                    result.setRefreshToken(JwtTokenUtil.refreshToken(token));
                    return result;
                } else {
                    throw new MyException(ResultEnum.TOKEN_WRONG);
                }
            } else {
                return joinPoint.proceed();
            }
        } else {
            throw new MyException(ResultEnum.TOKEN_WRONG);
        }

    }


}
