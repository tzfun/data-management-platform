package demo.sicau.datamanagementplatform.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * @Author beifengtz
 * @Date Created in 22:16 2018/9/21
 * @Description:
 */
public class CustomRealm extends AuthorizingRealm {

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
//        1.从主体传过来的认证信息中，获得用户名
        String userName = (String) authenticationToken.getPrincipal();

//        通过用户名到数据库中获取凭证
        String password = getPasswordByUserName(userName);
        if(password == null){
            return null;
        }
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo("beifengtz","123456","customRealm");
        return simpleAuthenticationInfo;
    }

    /**
     * 从数据库中查询凭证
     * */
    private String getPasswordByUserName(String userName) {

        return null;
    }
}
