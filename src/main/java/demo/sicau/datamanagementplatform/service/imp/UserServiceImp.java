package demo.sicau.datamanagementplatform.service.imp;

import demo.sicau.datamanagementplatform.dao.PermissionDao;
import demo.sicau.datamanagementplatform.dao.UserDao;
import demo.sicau.datamanagementplatform.entity.DTO.Permission;
import demo.sicau.datamanagementplatform.entity.DTO.User;
import demo.sicau.datamanagementplatform.entity.POJO.PO.UserPO;
import demo.sicau.datamanagementplatform.entity.POJO.VO.ResultVO;
import demo.sicau.datamanagementplatform.enums.ResultEnum;
import demo.sicau.datamanagementplatform.service.CredentialService;
import demo.sicau.datamanagementplatform.service.UserService;
import demo.sicau.datamanagementplatform.shiro.StatelessAuthenticationToken;
import demo.sicau.datamanagementplatform.util.*;
import demo.sicau.datamanagementplatform.util.redis.RedisKeyManagerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 19:22 2018/11/7
 * @Description:
 */
@Service
public class UserServiceImp implements UserService {

    private static final long USER_CACHE_TIME = 2 * 3600;   //用户信息缓存2小时
    private static final long PERMISSION_CACHE_TIME = 2 * 3600;   //权限信息缓存2小时

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImp.class);

    @Autowired
    private CredentialService credentialService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private ResultUtil resultUtil;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private PermissionDao permissionDao;

    @Override
    public ResultVO login(String account, String password, String credentialKey, String credentialValue) {
        ResultVO resultVO = credentialService.checkVerifyCode(credentialKey,credentialValue);
        if(resultVO.getStatus() == ResultEnum.SUCCESS.getStatus()){
            try{
                UserPO userPO = userDao.selectUserByAccountAndPassword(account,MD5Util.GetMD5Code(password));
                if(userPO!=null){
                    //生成token，客户端作为令牌使用
                    StatelessAuthenticationToken statelessAuthenticationToken = new StatelessAuthenticationToken();
                    String userId = userPO.getId();
                    statelessAuthenticationToken.setUserId(userId);
                    String token = tokenUtil.createDigest(statelessAuthenticationToken,password);
                    logger.info("用户登录成功，生成令牌。userId={};token={}",userId,token);
                    // 缓存用户信息
                    redisUtil.hmset(RedisKeyManagerUtil.getUserInfoKey(userId), (Map<String, Object>) ObjectTransformUtil.objectToMap(userPO),USER_CACHE_TIME);
                    logger.info("缓存用户信息成功。userId={};",userId);
                    //设置返回的数据
                    return resultUtil.success(token+":"+userId);
                }else{
                    return resultUtil.error(ResultEnum.PASSWORD_INCORRECT);
                }
            }catch (Exception e){
                e.printStackTrace();
                return resultUtil.error(ResultEnum.UNKNOWN_ERROR.getStatus(),e.getMessage());
            }
        }else{
            return resultVO;
        }
    }

    @Override
    public List<Permission> listPermissionByUserId(String userId) {
        return permissionDao.listByUserId(userId);
    }

    @Override
    public ResultVO register(String account, String password, String credentialKey, String credentialValue) {
        ResultVO resultVO = credentialService.checkVerifyCode(credentialKey,credentialValue);
        if(resultVO.getStatus() == ResultEnum.SUCCESS.getStatus()){
            try{
                UserPO userPO = userDao.selectUserByAccount(account);
                if (userPO == null){
                    String id = UUID.randomUUID().toString().replace("-","");
                    if (userDao.insertUserByAccountAndPassword(id,account, MD5Util.GetMD5Code(password))){

                        //生成token，客户端作为令牌使用
                        StatelessAuthenticationToken statelessAuthenticationToken = new StatelessAuthenticationToken();
                        String userId = id;
                        statelessAuthenticationToken.setUserId(userId);
                        String token = tokenUtil.createDigest(statelessAuthenticationToken,password);
                        UserPO user = new UserPO();
                        user.setPassword(password);
                        user.setAccount(account);
                        user.setId(id);
                        logger.info("用户注册，验证验证码通过，注册成功。userId={},token={}",userId,token);
                        // 缓存用户信息
                        redisUtil.hmset(RedisKeyManagerUtil.getUserInfoKey(userId), (Map<String, Object>) ObjectTransformUtil.objectToMap(user), USER_CACHE_TIME);
                        logger.info("缓存用户信息成功。userId={},token={}",userId,token);
                        //设置返回的数据
                        return resultUtil.success(token+":"+userId);
                    }
                }else {
                    return resultUtil.error(ResultEnum.RESOURCE_EXIST.getStatus(),"用户已存在");
                }
            }catch (Exception e){
                e.printStackTrace();
                return resultUtil.error(ResultEnum.UNKNOWN_ERROR.getStatus(),e.getMessage());
            }
        }else{
            logger.info("用户注册，验证验证码不通过");
            return resultVO;
        }
        return resultUtil.error(ResultEnum.UNKNOWN_ERROR);
    }

    @Override
    public ResultVO logout(String token) {
        String userId  = tokenUtil.getUserKeyByToken(token);
        redisUtil.del(RedisKeyManagerUtil.getUserInfoKey(userId)); // 删除用户缓存数据
        redisUtil.del(RedisKeyManagerUtil.getDigestKey(userId)); // 删除用户消息摘要
        redisUtil.del(RedisKeyManagerUtil.getPermissionKey(userId));//  删除权限信息缓存
        logger.info("用户注销，删除用户信息缓存、消息摘要缓存、权限信息缓存。userId={}",userId);
        return resultUtil.success();
    }

    @Override
    public ResultVO getInfo(String token) {
        String userId = tokenUtil.getUserKeyByToken(token);
        Map<Object, Object> userMap = redisUtil.hmget(RedisKeyManagerUtil.getUserInfoKey(userId));
        if (userMap.get("id") == null){
            User user = userDao.selectUserById(userId);
            logger.info("获取用户信息，从数据库读取。userId={}",userId);
            return resultUtil.success(user);
        }else{
            userMap.remove("class");
            userMap.remove("password");
            logger.info("获取用户信息，从缓存读取。userId={}",userId);
            return resultUtil.success(userMap);
        }
    }

    @Override
    public ResultVO addUser(UserPO userPO) {
        try{
            UserPO userDB = userDao.selectUserByAccount(userPO.getAccount());
            if(userDB == null){
                userPO.setPassword(MD5Util.GetMD5Code(userPO.getPassword()));
                if(userDao.insertUser(userPO)){
                    logger.info("管理员增加一名用户，该用户信息：userId={},account={}",userPO.getId(),userPO.getAccount());
                    return resultUtil.success();
                }
            }else{
                return resultUtil.error(ResultEnum.RESOURCE_EXIST.getStatus(),"用户已存在");
            }
        }catch (Exception e){
            e.printStackTrace();
            return resultUtil.error(ResultEnum.UNKNOWN_ERROR.getStatus(),e.getMessage());
        }
        return resultUtil.error(ResultEnum.UNKNOWN_ERROR);
    }

    @Override
    public ResultVO deleteUserById(String id,String token) {
        String tempUserId = new TokenUtil().getUserKeyByToken(token);
        try{
            UserPO user = userDao.selectUserById(id);
            if(user == null){
                return resultUtil.error(ResultEnum.USER_NOT_FOUND);
            }else {
                UserPO actionUser = userDao.selectUserById(tempUserId);
                if (tempUserId.equals(user.getId())){
                    return resultUtil.error(ResultEnum.CANNOT_DEL_SELF);    //  无法删除自己
                }else if("sAdmin".equals(user.getRole())){  // 如果被删除的用户为sAdmin
                    if ("sAdmin".equals(actionUser.getRole())){     // 如果操作用户的角色也为sAdmin则允许操作
                        if (userDao.deleteUserById(id)){
                            logger.info("超级管理员（userId={}）成功删除一名超级管理员（userId={}）",tempUserId,id);
                            return resultUtil.success();
                        }
                    }else{  // 非sAdmin用户无法删除sAdmin用户
                        return resultUtil.error(ResultEnum.ACTION_UNAUTHORIZED);
                    }
                }else{  // 被删除用户不为sAdmin即可操作
                    if (userDao.deleteUserById(id)){
                        logger.info("管理员（userId={}）成功删除一名用户（userId={}）",tempUserId,id);
                        return resultUtil.success();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return resultUtil.error(ResultEnum.UNKNOWN_ERROR.getStatus(),e.getMessage());
        }
        return resultUtil.error(ResultEnum.UNKNOWN_ERROR);
    }

    @Override
    public ResultVO updateUser(User user) {
        try{
            UserPO userDB = userDao.selectUserById(user.getId());
            if(userDB == null){
                return resultUtil.error(ResultEnum.USER_NOT_FOUND);
            }else{
                if(userDao.updateUser(user)){
                    UserPO newUserInfo = userDao.selectUserById(user.getId());
                    redisUtil.hmset(RedisKeyManagerUtil.getUserInfoKey(user.getId()), (Map<String, Object>) ObjectTransformUtil.objectToMap(newUserInfo), USER_CACHE_TIME);
                    logger.info("个人修改用户信息，更新缓存。userId={}",user.getId());
                    return resultUtil.success();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            return resultUtil.error(ResultEnum.UNKNOWN_ERROR.getStatus(),e.getMessage());
        }
        return resultUtil.error(ResultEnum.UNKNOWN_ERROR);
    }

    @Override
    public ResultVO updateUserByPermission(User user,String userId) {
        if ("".equals(user.getId().trim()) || user.getId() == null){
            return resultUtil.error(ResultEnum.PARAM_ERROR.getStatus(),"请传入正确的用户id");
        }else{
            try{
                UserPO userPO = userDao.selectUserById(user.getId());
                if(userPO == null){
                    return resultUtil.error(ResultEnum.USER_NOT_FOUND);
                }else{
                    UserPO actionUser = userDao.selectUserById(userId);
                    if ("sAdmin".equals(userPO.getRole())){ // 被修改者是sAdmin
                        if ("sAdmin".equals(actionUser.getRole())){ // 操作者也是sAdmin
                            userDao.updateUser(user);
                            UserPO newUserInfo = userDao.selectUserById(user.getId());
                            redisUtil.hmset(RedisKeyManagerUtil.getUserInfoKey(userId), (Map<String, Object>) ObjectTransformUtil.objectToMap(newUserInfo), USER_CACHE_TIME);
                            logger.info("管理员修改用户信息，更新缓存。actionUserId={},userId={}",userId,user.getId());
                            return resultUtil.success();
                        }else{
                            return resultUtil.error(ResultEnum.ACTION_UNAUTHORIZED);
                        }
                    }else{
                        userDao.updateUser(user);
                        return resultUtil.success();
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
                return resultUtil.error(ResultEnum.UNKNOWN_ERROR.getStatus(),e.getMessage());
            }
        }
    }

    @Override
    public ResultVO updatePasswordByPermission(String userId, String password,String actionUserId) {
        try{
            UserPO user = userDao.selectUserById(userId);
            UserPO actionUser = userDao.selectUserById(actionUserId);
            if ("sAdmin".equals(user.getRole())){ // 被操作者是sAdmin
                if ("sAdmin".equals(actionUser.getRole())){ // 操作者也是sAdmin则放行
                    if(userDao.updatePassword(userId,password)){
                        // 删除用户摘要缓存
                        redisUtil.del(RedisKeyManagerUtil.getDigestKey(userId));
                        return resultUtil.success();
                    }
                }else{
                    return resultUtil.error(ResultEnum.ACTION_UNAUTHORIZED);
                }
            }else{
                if(userDao.updatePassword(userId,password)){
                    // 删除用户摘要缓存
                    redisUtil.del(RedisKeyManagerUtil.getDigestKey(userId));
                    return resultUtil.success();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return resultUtil.error(ResultEnum.UNKNOWN_ERROR.getStatus(),e.getMessage());
        }
        return resultUtil.error(ResultEnum.UNKNOWN_ERROR);
    }

    @Override
    public ResultVO updatePassword(String userId, String oldPassword, String newPassword) {
        try{
            UserPO userPO = userDao.selectUserById(userId);
            if (userPO == null){
                return resultUtil.error(ResultEnum.USER_NOT_FOUND);
            }else if(!userPO.getPassword().equals(oldPassword)){
                return resultUtil.error(ResultEnum.PASSWORD_INCORRECT);
            }else{
                if (userDao.updatePassword(userId,newPassword)){
                    // 删除用户摘要缓存
                    redisUtil.del(RedisKeyManagerUtil.getDigestKey(userId));
                    return resultUtil.success();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return resultUtil.error(ResultEnum.UNKNOWN_ERROR.getStatus(),e.getMessage());
        }
        return resultUtil.error(ResultEnum.UNKNOWN_ERROR);
    }

    @Override
    public ResultVO getPermissionList(String userId) {
        try{
            List<Permission> redisList = (List<Permission>) ObjectTransformUtil.jsonToList(String.valueOf(redisUtil.get(RedisKeyManagerUtil.getPermissionKey(userId))));
            if (redisList == null || redisList.size() == 0){
                List<Permission> permissionList = permissionDao.listByUserId(userId);
                if (permissionList !=null){
                    if(redisUtil.set(RedisKeyManagerUtil.getPermissionKey(userId),ObjectTransformUtil.listToJson(permissionList),PERMISSION_CACHE_TIME)){
                        logger.info("从数据库读取权限列表，并存入缓存,userId={}",userId);
                        return resultUtil.success(permissionList);
                    }
                }
            }else{
                logger.info("从缓存读取权限列表，userId={}",userId);
                return resultUtil.success(redisList);
            }
        }catch (Exception e){
            e.printStackTrace();
            return resultUtil.error(ResultEnum.UNKNOWN_ERROR.getStatus(),e.getMessage());
        }
        return resultUtil.error(ResultEnum.UNKNOWN_ERROR);
    }

    @Override
    public ResultVO getUserList(int page, int pageSize) {
        try{
            int total = userDao.countUser();
            List<UserPO> rows = null;
            if(total != 0){
                rows  = userDao.selectUserByPagination(page * pageSize,pageSize);
            }
            HashMap<String,Object> resMap = new HashMap<>();
            resMap.put("total",total);
            resMap.put("rows",rows);
            return resultUtil.success(resMap);
        }catch (Exception e){
            e.printStackTrace();
            return resultUtil.error(ResultEnum.UNKNOWN_ERROR.getStatus(),e.getMessage());
        }
    }

    @Override
    public ResultVO batchDeleteUserById(ArrayList<String> idList) {
        try{
            if (userDao.batchDeleteUserById(idList)){
                return resultUtil.success();
            }
        }catch (Exception e){
            e.printStackTrace();
            return resultUtil.error(ResultEnum.UNKNOWN_ERROR.getStatus(),e.getMessage());
        }
        return resultUtil.error(ResultEnum.UNKNOWN_ERROR);
    }
}
