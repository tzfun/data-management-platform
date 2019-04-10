package demo.sicau.datamanagementplatform.shiro.filter;

import demo.sicau.datamanagementplatform.constants.HttpParamKeyConstants;
import demo.sicau.datamanagementplatform.enums.ResultEnum;
import demo.sicau.datamanagementplatform.shiro.StatelessAuthenticationToken;
import demo.sicau.datamanagementplatform.util.ResultUtil;
import demo.sicau.datamanagementplatform.util.web.ResponseUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 20:33 2018/11/2
 * @Description: 访问控制过滤器
 */
public class StatelessAccessControlFilter extends AccessControlFilter {
    private static final Logger logger = LoggerFactory.getLogger(StatelessAccessControlFilter.class);

    @Autowired
    private ResultUtil resultUtil;

    /**
     * 组成token的参数个数，用:分割
     */
    private static final Integer TOKEN_ARGS_NUM = 2;
    /**
     * 先执行：isAccessAllowed 再执行onAccessDenied
     *
     * isAccessAllowed：表示是否允许访问；mappedValue就是[urls]配置中拦截器参数部分，
     * 如果允许访问返回true，否则false；
     *
     * 如果返回true的话，就直接返回交给下一个filter进行处理。
     * 如果返回false的话，回往下执行onAccessDenied
     */

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
            throws Exception {
        logger.info("StatelessAuthcFilter.isAccessAllowed()");
        return false;
    }

    /**
     * onAccessDenied：表示当访问拒绝时是否已经处理了；如果返回true表示需要继续处理；
     * 如果返回false表示该拦截器实例已经处理了，将直接返回即可。
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        logger.info("StatelessAuthcFilter.onAccessDenied()");
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        try {
        //1、客户端生成的消息摘要
        String digest = httpRequest.getHeader(HttpParamKeyConstants.CLIENT_DIGEST);
        String digestArray[] = digest.split("\\:");
        if (digestArray.length != TOKEN_ARGS_NUM){
            logger.error(getName()+"："+ ResultEnum.PARAM_ERROR.getMsg());
            onParamError(httpResponse, ResultEnum.PARAM_ERROR.getMsg());
            return false;
        }
            //1、客户端生成的消息摘要
        String clientDigest = digestArray[0];
        // 2、客户端传入的用户身份
        String clientId = digestArray[1];
        //如果参数为空则返回错误信息
        if (clientDigest == null || clientId == null){
            String nullParamMsg = "已拦截请求！"+ HttpParamKeyConstants.CLIENT_ID + "或" + HttpParamKeyConstants.CLIENT_DIGEST+ "不能为空！";
            logger.error(nullParamMsg);
            onParamError(httpResponse, nullParamMsg);
            return false;
        }
        //参数不为空打印信息
        logger.info(HttpParamKeyConstants.CLIENT_ID + ":" + clientId + "," + HttpParamKeyConstants.CLIENT_DIGEST + ":" + clientDigest);
        //3、客户端请求的参数列表
//        Map<String, String[]> params = new HashMap<String, String[]>(request.getParameterMap());
//        params.remove("digest");//签名或者消息摘要算法的时候不能包含digest.
        //4、生成无状态Token
        StatelessAuthenticationToken statelessAuthenticationToken = new StatelessAuthenticationToken(clientId,clientDigest);
//     UsernamePasswordToken token = new UsernamePasswordToken(username,clientDigest);
            //5、委托给Realm进行登录
            getSubject(request, response).login(statelessAuthenticationToken);
        } catch (NullPointerException e){
            logger.error(getName()+"：未传入用户消息摘要");
            onParamError(httpResponse,"未传入用户消息摘要");
            return false;
        }catch(AuthenticationException e){
            logger.error(getName()+"：用户登录失败");
            onLoginFail(httpResponse,"登录失败");
        }catch (Exception e) {
            e.printStackTrace();
            //6、登录失败
            onLoginFail(httpResponse,"系统错误："+e.getMessage());
            return false;//就直接返回给请求者.
        }
        return true;
    }

    /**
     * 登录失败时默认返回401 状态码
     * @param response
     * @throws IOException
     */
    private void onLoginFail(HttpServletResponse response, String msg) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ResponseUtil.toJson(
                response,
                resultUtil.error(ResultEnum.LOGIN_FAILED.getStatus(),msg)
        );
    }

    /**
     * 参数不正确返回错误信息
     * @param response
     * @param msg 错误消息
     * @throws IOException
     */
    private void onParamError(HttpServletResponse response, String msg) throws IOException{
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        ResponseUtil.toJson(
                response,
                resultUtil.error(ResultEnum.PARAM_ERROR.getStatus(), msg)
        );
    }
}
