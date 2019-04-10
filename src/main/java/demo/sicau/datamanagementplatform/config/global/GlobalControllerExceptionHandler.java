package demo.sicau.datamanagementplatform.config.global;

import demo.sicau.datamanagementplatform.enums.ResultEnum;
import demo.sicau.datamanagementplatform.util.ResultUtil;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 16:37 2018/11/12
 * @Description: 全局异常处理
 */
@ControllerAdvice
public class GlobalControllerExceptionHandler {
    @Autowired
    private ResultUtil resultUtil;
    /*
     * shiro 抛出的UnauthorizedException 统一返回 操作无权限返回类
     */
    @ExceptionHandler(value = UnauthorizedException.class)
    public ResponseEntity  defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        return new ResponseEntity(resultUtil.error(ResultEnum.ACTION_UNAUTHORIZED),HttpStatus.OK);
    }
}
