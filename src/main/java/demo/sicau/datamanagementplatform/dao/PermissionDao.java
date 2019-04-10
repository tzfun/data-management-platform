package demo.sicau.datamanagementplatform.dao;

import demo.sicau.datamanagementplatform.entity.DTO.Permission;
import demo.sicau.datamanagementplatform.entity.DTO.Role;
import demo.sicau.datamanagementplatform.entity.POJO.PO.RolePermissionPO;
import demo.sicau.datamanagementplatform.entity.POJO.PO.UserRolePO;
import demo.sicau.datamanagementplatform.entity.POJO.VO.RolePermissionVO;
import demo.sicau.datamanagementplatform.entity.POJO.VO.UserRoleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 16:09 2018/11/12
 * @Description:
 */
@Mapper
public interface PermissionDao {
    /**
     * 通过用户id查询他的权限
     * @param userId 用户id
     * @return Permission
     */
    List<Permission> listByUserId(@Param("userId") String userId);

    /**
     * 查询所有的角色并附带的权限
     * @return RolePermissionPO
     */
    List<RolePermissionVO> selectRolePermission();

    /**
     * 查询用户和角色列表
     * @return UserRoleVO
     */
    List<UserRoleVO> selectUserRole();

    /**
     * 通过关系id删除用户和角色的关系(无法删除sAdmin)
     * @param id 关系id
     * @return boolean
     */
    boolean delUserAndRole(@Param("id") String id);

    /**
     * 通过用户角色关系查询角色
     * @param id 关系id
     * @return Role
     */
    Role selectRoleByUserRoleId(@Param("id") String id);

    /**
     * 列出所有的权限
     * @return list集合
     */
    ArrayList<Permission> selectPermissions();

    /**
     * 插入角色
     * @param role @Link{Role} 角色对象
     * @return boolean
     */
    boolean insertRole(@Param("role") Role role);

    /**
     * 新增角色和权限之间的关系(批量插入)
     * @param idList id集合
     * @param id 角色id
     * @param permissions 权限id集合
     * @rerturn boolean
     */
    boolean insertRolePermission(@Param("rolePermissions") ArrayList<RolePermissionPO> rolePermissions);

    /**
     * 查询出所有的角色
     * @return ArrayList<Role>
     */
    ArrayList<Role> selectAllRole();

    /**
     * 插入用户角色关系
     * @param role 角色id
     * @param userIds 用户id
     */
    void insertUserRole(@Param("id") String id,
                        @Param("role") String role,
                        @Param("uid") String uid);

    /**
     * 根据用户id查询用户角色关系
     * @param uid 用户id
     * @return UserRolePO
     */
    UserRolePO selectUserRoleByUserId(@Param("uid") String uid);

    /**
     * 更新用户角色关系
     * @param role 角色id
     * @param uid 用户id
     */
    void updateUserRole(@Param("role") String role, @Param("uid") String uid);

    /**
     * 通过roleId查询用户角色
     * @param roleId 角色Id
     * @return Role
     */
    Role selectRoleByRoleId(@Param("roleId") String roleId);
}
