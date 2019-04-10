package demo.sicau.datamanagementplatform.util.redis;

/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 8:32 2018/11/14
 * @Description: redis缓存数据库key管理工具
 */
public class RedisKeyManagerUtil {
    /**
     * 获取摘要存储的key
     * @param userId 用户id
     * @return String
     */
    public static String getDigestKey(String userId){
        return "digest:"+userId;
    }

    /**
     * 获取用户信息存储的key
     * @param userId 用户id
     * @return String
     */
    public static String getUserInfoKey(String userId){
        return "user:"+userId;
    }

    /**
     * 获取verifyCodeKey存储的key
     * @param codeKey 验证码Key
     * @return String
     */
    public static String getVerifyCodeKey(String codeKey){
        return "verify:"+codeKey;
    }

    /**
     * 获取权限信息储存的key
     * @param userId 用户id
     * @return String
     */
    public static String getPermissionKey(String userId) {
        return "permission:"+userId;
    }

    /**
     * 获取文章信息存储的Key
     * @param articleId 文章id
     * @return String
     */
    public static String getArticleKey(String articleId){
        return "article:"+articleId;
    }

    /**
     * 获取资源信息存储的Key
     * @param resourceId 资源id
     * @return String
     */
    public static String getResourceKey(String resourceId) {
        return "resource:"+resourceId;
    }

    /**
     * 获取系统权限缓存的key
     * @return String
     */
    public static String getSystemPermissionKey() {
        return "system:permission";
    }

    /**
     * 获取全局搜索结果缓存的key
     * @param keyword 关键字
     * @return String
     */
    public static String getSearchKey(String keyword) {
        return "search:"+keyword;
    }
}
