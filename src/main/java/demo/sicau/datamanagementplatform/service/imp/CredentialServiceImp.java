package demo.sicau.datamanagementplatform.service.imp;

import demo.sicau.datamanagementplatform.entity.POJO.VO.ResultVO;
import demo.sicau.datamanagementplatform.enums.ResultEnum;
import demo.sicau.datamanagementplatform.service.CredentialService;
import demo.sicau.datamanagementplatform.util.RedisUtil;
import demo.sicau.datamanagementplatform.util.ResultUtil;
import demo.sicau.datamanagementplatform.util.VerifyCodeUtil;
import demo.sicau.datamanagementplatform.util.redis.RedisKeyManagerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 12:28 2018/11/8
 * @Description:
 */
@Service
public class CredentialServiceImp implements CredentialService {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ResultUtil resultUtil;

    @Override
    public void createCredentialImg(HttpServletRequest request, HttpServletResponse response) {
        VerifyCodeUtil verifyCodeUtil = new VerifyCodeUtil();
        try {
            String verifyCodeKey = UUID.randomUUID().toString().replace("-","");
            // 将四位数字的验证码保存到Redis中。
            redisUtil.set(RedisKeyManagerUtil.getVerifyCodeKey(verifyCodeKey),verifyCodeUtil.createCredentialImg(response,verifyCodeKey),60);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ResultVO checkVerifyCode(String key, String value) {
        String redisValue = (String) redisUtil.get(RedisKeyManagerUtil.getVerifyCodeKey(key));
        if (redisValue == null){
            return resultUtil.error(ResultEnum.VERIFY_CODE_INVALID);
        }else if(value.toLowerCase().equals(redisValue.toLowerCase())){
            redisUtil.del(RedisKeyManagerUtil.getVerifyCodeKey(key));
            return resultUtil.success();
        }
        return resultUtil.error(ResultEnum.VERIFY_CODE_ERROR);
    }
}
