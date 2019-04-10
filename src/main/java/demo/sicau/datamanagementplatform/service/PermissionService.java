package demo.sicau.datamanagementplatform.service;

import demo.sicau.datamanagementplatform.entity.POJO.VO.ResultVO;

import java.util.ArrayList;

/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 16:05 2018/11/12
 * @Description:
 */
public interface PermissionService {

    /**
     * 获取资源接口
     * @return ResultVO
     */
    ResultVO getResourceList();

    /**
     * 获取操作接口
     * @return ResultVO
     */
    ResultVO getActionList();

    /**
     * 获取角色权限列表
     * @return ResultVO
     */
    ResultVO getRoleAndPermissionList();

    /**
     * 获取用户和角色列表
     * @return ResultVO
     */
    ResultVO getUserAndRoleList();

    /**
     * 通过关系id删除角色关系
     * @param id 被解除用户的id
     * @return ResultVO
     */
    ResultVO delUserAndRole(String uid, String token);

    /**
     * 查询所有权限
     * @return ResultVO
     */
    ResultVO getAllPermission();

    /**
     * 新增角色，并分配相应权限
     * @param roleName  角色名
     * @param permissions 权限
     * @return ResultVO
     */
    ResultVO addRole(String roleName, ArrayList<String> permissions);

    /**
     * 查询所有角色
     * @return  ResultVO
     */
    ResultVO getAllRole();

    /**
     * 添加用户角色
     * @param role  角色id
     * @param userIds   用户id
     * @param actionUserId 操作者用户id
     * @return ResultVO
     */
    ResultVO addUserRole(String role, ArrayList<String> userIds,String actionUserId);

    /**
     * 更新用户角色
     * @param roleId    角色id
     * @param userId    用户id
     * @param actionUserId 操作者用户id
     * @return  ResultVO
     */
    ResultVO updateUserRole(String roleId, String userId,String actionUserId);
}
