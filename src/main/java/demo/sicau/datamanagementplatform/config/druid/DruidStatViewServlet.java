package demo.sicau.datamanagementplatform.config.druid;

import com.alibaba.druid.support.http.StatViewServlet;

import javax.servlet.Servlet;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 19:04 2018/11/7
 * @Description:
 */
@WebServlet(
        urlPatterns= {"/druid/*"},
        initParams= {
                @WebInitParam(name="allow",value="127.0.0.1"),
                @WebInitParam(name="loginUsername",value="root"),
                @WebInitParam(name="loginPassword",value="1246886075"),
                @WebInitParam(name="resetEnable",value="true")// 允许HTML页面上的“Reset All”功能
        }
)
public class DruidStatViewServlet extends StatViewServlet implements Servlet {
    private static final long serialVersionUID = 1L;
}