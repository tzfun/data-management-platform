package demo.sicau.datamanagementplatform.entity.POJO.VO;

import demo.sicau.datamanagementplatform.entity.DTO.Permission;
import demo.sicau.datamanagementplatform.entity.DTO.Role;

/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 22:09 2018/11/28
 * @Description:
 */
public class RolePermissionVO {

    private String id;

    private Role role;

    private Permission permission;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }
}
