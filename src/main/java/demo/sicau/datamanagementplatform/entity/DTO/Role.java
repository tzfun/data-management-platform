package demo.sicau.datamanagementplatform.entity.DTO;

/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 22:17 2018/11/28
 * @Description:
 */
public class Role {

    private String id;

    private String roleName;

    private String createTime;

    public Role() {
    }

    public Role(String id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
