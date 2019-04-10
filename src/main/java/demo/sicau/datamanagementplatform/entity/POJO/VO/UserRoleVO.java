package demo.sicau.datamanagementplatform.entity.POJO.VO;

import demo.sicau.datamanagementplatform.entity.DTO.Role;
import demo.sicau.datamanagementplatform.entity.DTO.User;

/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 9:22 2018/11/29
 * @Description:
 */
public class UserRoleVO {
    private String id;

    private User user;

    private Role role;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
