package demo.sicau.datamanagementplatform.entity.POJO.PO;

import demo.sicau.datamanagementplatform.entity.DTO.User;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 19:44 2018/11/7
 * @Description:
 */
public class UserPO extends User {

    private String password;

    private String createTime;

    private String updateTime;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
