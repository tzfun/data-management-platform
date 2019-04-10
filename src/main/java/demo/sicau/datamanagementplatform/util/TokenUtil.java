package demo.sicau.datamanagementplatform.util;

import demo.sicau.datamanagementplatform.shiro.StatelessAuthenticationToken;
import demo.sicau.datamanagementplatform.util.redis.RedisKeyManagerUtil;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Author beifengtz
 * @Date Created in 10:17 2018/9/22
 * @Description:
 */
@Component
public class TokenUtil {

    /**
     * 验证成功后默认重置token保存时间(秒)
     */
    public static final long DEFAULT_EXPIRATION_TIME = 7 * 24 * 3600; // 7天

    private static final Logger logger = LoggerFactory.getLogger(TokenUtil.class);

    /**
     * token缓存的key
     */
    private static final String TOKEN_KEY = "token";


    @Autowired
    private RedisUtil redisUtil;
    /**
     * 创建消息摘要
     * 默认过期时间为7天
     *
     * @param token 用于生成digest的模型实体
     * @param password 用户密码
     * @return 生成的digest
     */
    public String createDigest(StatelessAuthenticationToken token, String password) {
        return createDigest(token,password,DEFAULT_EXPIRATION_TIME);
    }

    /**
     * 创建消息摘要
     *
     * @param token 用于生成digest的模型实体
     * @param password 用户密码
     * @param expirationTime 过期时间
     * @return 生成的digest
     */
    public String createDigest(StatelessAuthenticationToken token,String password, long expirationTime) {
        logger.info("--------创建digest消息摘要---------");
        String userId = token.getUserId();
        //进行消息摘要
        String digest = HmacSHA256Utils.digest(password, token.getParams());
        //将token存储到redis并设置过期时间
        String digestKey = RedisKeyManagerUtil.getDigestKey(userId);
        redisUtil.hset(digestKey,TOKEN_KEY,digest,expirationTime);
        logger.info("用户id:" + token.getUserId() + ",消息摘要已创建：" + digest);
        return digest;
    }

    public String getDigest(String userId){
        return (String) redisUtil.hget(RedisKeyManagerUtil.getDigestKey(userId),TOKEN_KEY);
    }

    /**
     * 验证token
     * 验证成功后默认重置token保存时间为7天
     *
     */
    public boolean validToken(StatelessAuthenticationToken clientToken) {
        return validToken(clientToken, DEFAULT_EXPIRATION_TIME);
    }

    /**
     * 验证token
     *
     * @param expirationTime 重置token的保存时间(天)
     * @return
     */
    public boolean validToken(StatelessAuthenticationToken clientToken, long expirationTime) {
        boolean flag = false;
        String userIdInfo;
        if (clientToken != null) {
            String userId = clientToken.getUserId();
            userIdInfo = "用户id:" + userId;
            try {
                //判断token是否存在
                Object serverToken = redisUtil.hget(
                        RedisKeyManagerUtil.getDigestKey(userId),
                                TOKEN_KEY
                        );
                if (serverToken == null || !serverToken.equals(clientToken.getClientDigest())) {
                    logger.info(userIdInfo + ",与服务端token匹配失败");
                } else {
                    flag = true;
                    redisUtil.getRedisTemplate().expire(RedisKeyManagerUtil.getDigestKey(userId),expirationTime, TimeUnit.SECONDS);
                    logger.info(userIdInfo + ",token验证通过，重置保存时间为" + expirationTime + "天");
                }

            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        } else {
            logger.info("请求参数token为空，验证失败");
        }
        return flag;
    }

    /**
     * 删除Token
     * @param userId
     */
    public void deleteToken(String userId) {
        redisUtil.hdel(RedisKeyManagerUtil.getDigestKey(userId));
    }


    public String getUserKeyByToken(String token){
        return token.split(":")[1];
    }

    /**
     * 通过Shiro的SimpleAuthenticationInfo类来进行身份验证,
     * 验证成功后默认重置token保存时间为7天
     * @param userId
     * @param realmName
     * @param expirationTime
     * @return
     */
    public SimpleAuthenticationInfo validTokenBySimpleAuthenticationInfo(String userId, String realmName, long expirationTime) {
        String digest = (String) redisUtil.hget(RedisKeyManagerUtil.getDigestKey(userId),TOKEN_KEY);
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(userId, digest, realmName);
        redisUtil.getRedisTemplate().expire(RedisKeyManagerUtil.getDigestKey(userId),expirationTime,TimeUnit.SECONDS);
        return authenticationInfo;
    }

    public SimpleAuthenticationInfo validTokenBySimpleAuthenticationInfo(String userId, String realmName) {
        return validTokenBySimpleAuthenticationInfo(userId, realmName, DEFAULT_EXPIRATION_TIME);
    }
}
