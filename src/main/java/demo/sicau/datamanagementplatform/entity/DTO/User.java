package demo.sicau.datamanagementplatform.entity.DTO;


/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 19:20 2018/11/7
 * @Description:
 */
public class User {
    private String id;

    private String account;

    private String realName;

    private String sicauId;

    private String email;

    private long telephone;

    private String website;

    private int sex;

    private String role;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getSicauId() {
        return sicauId;
    }

    public void setSicauId(String sicauId) {
        this.sicauId = sicauId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public long getTelephone() {
        return telephone;
    }

    public void setTelephone(long telephone) {
        this.telephone = telephone;
    }
}
