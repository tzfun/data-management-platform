package demo.sicau.datamanagementplatform.service.imp;

import demo.sicau.datamanagementplatform.constants.PermissionActionConstant;
import demo.sicau.datamanagementplatform.constants.ResourceConstants;
import demo.sicau.datamanagementplatform.dao.PermissionDao;
import demo.sicau.datamanagementplatform.dao.UserDao;
import demo.sicau.datamanagementplatform.entity.DTO.Permission;
import demo.sicau.datamanagementplatform.entity.DTO.Role;
import demo.sicau.datamanagementplatform.entity.POJO.PO.RolePermissionPO;
import demo.sicau.datamanagementplatform.entity.POJO.PO.UserPO;
import demo.sicau.datamanagementplatform.entity.POJO.PO.UserRolePO;
import demo.sicau.datamanagementplatform.entity.POJO.VO.ResultVO;
import demo.sicau.datamanagementplatform.entity.POJO.VO.RolePermissionVO;
import demo.sicau.datamanagementplatform.entity.POJO.VO.UserRoleVO;
import demo.sicau.datamanagementplatform.enums.ResultEnum;
import demo.sicau.datamanagementplatform.service.PermissionService;
import demo.sicau.datamanagementplatform.util.ObjectTransformUtil;
import demo.sicau.datamanagementplatform.util.RedisUtil;
import demo.sicau.datamanagementplatform.util.ResultUtil;
import demo.sicau.datamanagementplatform.util.TokenUtil;
import demo.sicau.datamanagementplatform.util.redis.RedisKeyManagerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 16:05 2018/11/12
 * @Description:
 */
@Service
public class PermissionServiceImp implements PermissionService {

    private static final long SYSTEM_PERMISSION_CACHE_TIME = 24 * 3600; //系统所有权限缓存时间一天

    private static final Logger logger = LoggerFactory.getLogger(PermissionServiceImp.class);

    @Autowired
    private ResultUtil resultUtil;
    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public ResultVO getResourceList() {
        return resultUtil.success(ResourceConstants.RESOURCES);
    }

    @Override
    public ResultVO getActionList() {
        return resultUtil.success(PermissionActionConstant.ACTIONS);
    }

    @Override
    public ResultVO getRoleAndPermissionList() {
        try{
            List<RolePermissionVO> rolePermissionVOS = permissionDao.selectRolePermission();
            return resultUtil.success(rolePermissionVOS);
        }catch (Exception e){
            e.printStackTrace();
            return resultUtil.error(ResultEnum.UNKNOWN_ERROR.getStatus(),e.getMessage());
        }
    }

    @Override
    public ResultVO getUserAndRoleList() {
        try{
            List<UserRoleVO> userRoleVOS = permissionDao.selectUserRole();
            return resultUtil.success(userRoleVOS);
        }catch (Exception e){
            e.printStackTrace();
            return resultUtil.error(ResultEnum.UNKNOWN_ERROR.getStatus(),e.getMessage());
        }
    }

    @Override
    public ResultVO delUserAndRole(String uid,String token) {
        String tempUserId = new TokenUtil().getUserKeyByToken(token);
        try{
            Role role = permissionDao.selectRoleByUserRoleId(uid);
            if (uid.equals(tempUserId)){    // 无法解除自己的关系
                return resultUtil.error(ResultEnum.CANNOT_DEL_SELF);
            }
            UserPO actionUser = userDao.selectUserById(tempUserId);
            if ("sAdmin".equals(role.getRoleName())){   // 如果被删除的用户是sAdmin
                if ( "sAdmin".equals(actionUser.getRole())){    // 操作者的角色也是sAdmin才可删除，否则认为权限不足
                    if(permissionDao.delUserAndRole(uid)){
                        return resultUtil.success();
                    }
                }else{
                    return resultUtil.error(ResultEnum.ACTION_UNAUTHORIZED);
                }
            }else{  // 被删除的用户不是sAdmin
                if(permissionDao.delUserAndRole(uid)){
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
    public ResultVO getAllPermission() {
        try{
            List<Permission> redisList = (List<Permission>) ObjectTransformUtil.jsonToList(String.valueOf(redisUtil.get(RedisKeyManagerUtil.getSystemPermissionKey())));
            if (redisList == null || redisList.size() == 0){
                ArrayList<Permission> permissions = permissionDao.selectPermissions();
                if (permissions != null){
                    logger.info("从数据库中读取系统所有权限。");
                    redisUtil.set(RedisKeyManagerUtil.getSystemPermissionKey(), ObjectTransformUtil.listToJson(permissions),SYSTEM_PERMISSION_CACHE_TIME);
                    logger.info("系统所有权限加入缓存成功。");
                    return resultUtil.success(permissions);
                }
            }else{
                logger.info("从缓存读取系统所有权限。");
                return resultUtil.success(redisList);
            }

        }catch (Exception e){
            e.printStackTrace();
            return resultUtil.error(ResultEnum.UNKNOWN_ERROR.getStatus(),e.getMessage());
        }
        return resultUtil.error(ResultEnum.UNKNOWN_ERROR);
    }

    @Transactional
    @Override
    public ResultVO addRole(String roleName, ArrayList<String> permissions) {
        if("sAdmin".equals(roleName)){
            return resultUtil.error(ResultEnum.RESOURCE_EXIST.getStatus(),"角色名不能为sAdmin");
        }
        ArrayList<Role> roles = permissionDao.selectAllRole();
        for (Role tempRole : roles){
            if (tempRole.getRoleName().equals(roleName)){
                return resultUtil.error(ResultEnum.RESOURCE_EXIST.getStatus(),"该角色名已存在");
            }
        }
        Role role = new Role(UUID.randomUUID().toString().replace("-",""),roleName);
        ArrayList<RolePermissionPO> rolePermissions = new ArrayList<>();
        permissionDao.insertRole(role);
        for (int i = 0;i<permissions.size();i++){
            RolePermissionPO tempRolePermission = new RolePermissionPO(
                    UUID.randomUUID().toString().replace("-",""),
                    role.getId(),
                    permissions.get(i)
            );
            rolePermissions.add(tempRolePermission);
        }
        permissionDao.insertRolePermission(rolePermissions);
        return resultUtil.success();
    }

    @Override
    public ResultVO getAllRole() {
        try{
            ArrayList<Role> roles = permissionDao.selectAllRole();
            return resultUtil.success(roles);
        }catch (Exception e){
            e.printStackTrace();
            return resultUtil.error(ResultEnum.UNKNOWN_ERROR.getStatus(),e.getMessage());
        }
    }

    @Transactional
    @Override
    public ResultVO addUserRole(String role, ArrayList<String> userIds,String actionUserId) {
        boolean flag = false;
        // 遍历检查这些用户是否已有角色
        for (int i = 0;i < userIds.size(); i ++){
            UserRolePO userRolePO = permissionDao.selectUserRoleByUserId(userIds.get(i));
            if (userRolePO != null){
                flag = true;
//                permissionDao.updateUserRole(role,userIds.get(i));
            }
        }
        if (flag){
            return resultUtil.error(ResultEnum.RESOURCE_EXIST.getStatus(),"操作用户中包含已有角色的用户，无法操作");
        }else{
            Role userRole = permissionDao.selectRoleByRoleId(role); // 查询将要分配的角色
            if ("sAdmin".equals(userRole.getRoleName())){   // 如果将要分配的角色是sAdmin，则判断操作者是否是sAdmin，是则允许，否则拦截
                UserPO actionUser = userDao.selectUserById(actionUserId);
                if ("sAdmin".equals(actionUser.getRole())){ //操作者也是sAdmin，放行
                    for (int i = 0;i < userIds.size(); i ++){
                        permissionDao.insertUserRole(UUID.randomUUID().toString().replace("-",""),role,userIds.get(i));
                    }
                }else{
                    return resultUtil.error(ResultEnum.ACTION_UNAUTHORIZED);
                }
            }else{
                for (int i = 0;i < userIds.size(); i ++){
                    permissionDao.insertUserRole(UUID.randomUUID().toString().replace("-",""),role,userIds.get(i));
                }
            }
        }
        return resultUtil.success();
    }

    @Override
    public ResultVO updateUserRole(String roleId, String userId,String actionUserId) {
        UserPO user = userDao.selectUserById(userId);
        if ("sAdmin".equals(user.getRole())){   // 如果被操作者是sAdmin
            UserPO actionUser = userDao.selectUserById(actionUserId);
            if ("sAdmin".equals(actionUser.getRole())){ // 操作者也是sAdmin则允许
                permissionDao.updateUserRole(roleId,userId);
                return resultUtil.success();
            }else{  // 操作者不是sAdmin没有权限操作sAdmin用户
                return resultUtil.error(ResultEnum.ACTION_UNAUTHORIZED);
            }
        }else{  // 被操作者不是sAdmin则允许操作
            permissionDao.updateUserRole(roleId,userId);
            return resultUtil.success();
        }
    }
}
