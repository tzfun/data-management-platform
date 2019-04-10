package demo.sicau.datamanagementplatform.entity.POJO.PO;

/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 11:39 2018/12/22
 * @Description:
 */
public class RolePermissionPO {
    private String id;

    private String roleId;

    private String permissionId;

    public RolePermissionPO(){}

    public RolePermissionPO(String id, String roleId, String permissionId) {
        this.id = id;
        this.roleId = roleId;
        this.permissionId = permissionId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }
}
