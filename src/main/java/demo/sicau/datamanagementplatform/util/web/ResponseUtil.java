package demo.sicau.datamanagementplatform.util.web;

import com.google.gson.Gson;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 15:21 2018/11/4
 * @Description:
 */
public class ResponseUtil {
    /**
     * 返回json格式数据
     * @param httpServletResponse
     * @param data 数据对象
     * @throws IOException
     */
    public static void toJson(HttpServletResponse httpServletResponse, Object data) throws IOException{
        Gson gson = new Gson();
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        httpServletResponse.getWriter().write(
                gson.toJson(data)
        );
    }
}
