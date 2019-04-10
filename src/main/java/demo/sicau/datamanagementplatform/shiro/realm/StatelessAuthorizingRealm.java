package demo.sicau.datamanagementplatform.shiro.realm;

import demo.sicau.datamanagementplatform.entity.DTO.Permission;
import demo.sicau.datamanagementplatform.service.UserService;
import demo.sicau.datamanagementplatform.shiro.StatelessAuthenticationToken;
import demo.sicau.datamanagementplatform.util.MD5Util;
import demo.sicau.datamanagementplatform.util.TokenUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 21:13 2018/11/2
 * @Description:
 */
public class StatelessAuthorizingRealm extends AuthorizingRealm {
    private static final Logger logger = LoggerFactory.getLogger(StatelessAuthorizingRealm.class);

    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private UserService userService;
    /**
     * 仅支持StatelessToken 类型的Token，
     * 那么如果在StatelessAuthcFilter类中返回的是UsernamePasswordToken，那么将会报如下错误信息：
     * Please ensure that the appropriate Realm implementation is configured correctly or
     * that the realm accepts AuthenticationTokens of this type.StatelessAuthcFilter.isAccessAllowed()
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof StatelessAuthenticationToken;
    }

    /**
     * 身份验证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        logger.info("StatelessRealm.doGetAuthenticationInfo()");
        StatelessAuthenticationToken statelessToken = (StatelessAuthenticationToken)token;
        String userId = (String)statelessToken.getPrincipal();//不能为null,否则会报错的.
        return tokenUtil.validTokenBySimpleAuthenticationInfo(userId,getName());//然后进行客户端消息摘要和服务器端消息摘要的匹配
    }

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
       logger.info("StatelessRealm.doGetAuthorizationInfo()");
        //根据用户名查找角色，请根据需求实现
        String userId = (String) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        //查询user的角色并添加权限
        List<Permission> permissionList = userService.listPermissionByUserId(userId);
//        System.out.println(permissionList);
        for (Permission permission : permissionList){
            authorizationInfo.addStringPermission(permission.getResource() + ":" + permission.getAction());
        }
        return authorizationInfo;
    }

    //得到密钥
    private String getKey(String username) {
        return MD5Util.GetMD5Code(username);
    }
}
