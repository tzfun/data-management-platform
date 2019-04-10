package demo.sicau.datamanagementplatform.filter;

import demo.sicau.datamanagementplatform.enums.ResultEnum;
import demo.sicau.datamanagementplatform.util.RedisUtil;
import demo.sicau.datamanagementplatform.util.ResultUtil;
import demo.sicau.datamanagementplatform.util.web.RequestUtil;
import demo.sicau.datamanagementplatform.util.web.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 16:23 2018/11/9
 * @Description: Dos防护以及爬虫拦截
 */
//@Order(0)
//@WebFilter(urlPatterns = "/*",description = "DOS防护及反爬虫机制",filterName = "dosFilter")
public class DosFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(DosFilter.class);

    private static final int FREE_TIME = 1; // 允许间隔1秒内进行请求
    private static final int BAN_USE_TIME = 2 * 3600; // 捕获到非法操作后禁用ip 2 个小时
    private static final String ILLEGAL_LIST_NAME = "illegal_ip_list";

    @Autowired
    @Qualifier(value="redisUtil")
    private RedisUtil redisUtil;
    @Autowired
    @Qualifier(value="resultUtil")
    private ResultUtil resultUtil;

    public DosFilter() {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String ip = RequestUtil.getIpAddress((HttpServletRequest) servletRequest);
//        System.out.println("ip----------->"+ip);
        Object ipLog = redisUtil.get(ip);
        if(redisUtil.hget(ILLEGAL_LIST_NAME,ip) !=null){
            System.out.println(redisUtil.hget(ILLEGAL_LIST_NAME, ip));
            logger.info("禁用ip重复请求已拦截，请求ip："+ip+",记录次数："+ipLog);
            ResponseUtil.toJson((HttpServletResponse) servletResponse,resultUtil.error(ResultEnum.PROHIBITED_PERIOD.getStatus(),"ip禁用期间无法请求！"));
        }else{
            if(ipLog == null){
                redisUtil.set(ip,1,FREE_TIME);
                filterChain.doFilter(servletRequest,servletResponse);
                return;
            }else if((int) ipLog >= 20 ){
                // 如果一秒内超过十次请求就视为恶意请求
                if(redisUtil.hget(ILLEGAL_LIST_NAME,ip) == null){
                    redisUtil.hset(ILLEGAL_LIST_NAME,ip,ipLog,BAN_USE_TIME);
                }
                logger.warn("成功拦截恶意请求！请求ip："+ip+",每秒请求次数："+ipLog+",已成功禁用该ip使用，2小时后失效。");
                ResponseUtil.toJson((HttpServletResponse) servletResponse,resultUtil.error(ResultEnum.ILLEGAL_DOS.getStatus(),"恶意请求已拦截："+ip+"，短时间内无法请求！"));
            }else if((int) ipLog >= 6 && (int)ipLog < 20){
                logger.warn("请求速度过快，已成功拦截！请求ip："+ip+",每1秒请求次数："+ipLog);
                redisUtil.set(ip,(int) ipLog + 1,FREE_TIME);
                // 如果一秒内超过2次请求就返回请求太快
                ResponseUtil.toJson((HttpServletResponse) servletResponse,resultUtil.error(ResultEnum.ACTION_TOO_FAST));
            }else{
                redisUtil.set(ip,(int) ipLog + 1,FREE_TIME);
                filterChain.doFilter(servletRequest,servletResponse);
                return;
            }
        }
    }
}
