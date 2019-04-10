package demo.sicau.datamanagementplatform.service;

import demo.sicau.datamanagementplatform.entity.POJO.VO.ResultVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 12:28 2018/11/8
 * @Description:
 */
public interface CredentialService {
    /**
     * 获取验证码图片
     * @param request 请求
     * @param response 响应
     */
    void createCredentialImg(HttpServletRequest request, HttpServletResponse response);

    /**
     * 验证验证码
     * @param key 键
     * @param value 值
     * @return ResultVO
     */
    ResultVO checkVerifyCode(String key, String value);
}
