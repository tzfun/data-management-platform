package demo.sicau.datamanagementplatform.entity.POJO.PO;

import demo.sicau.datamanagementplatform.entity.DTO.Article;

import java.util.Date;

/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 15:18 2018/11/20
 * @Description:
 */
public class ArticlePO extends Article {

    private String createTime;

    private String updateTime;

    private String userId;

    private int seeNum;

    private boolean hasPass;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getSeeNum() {
        return seeNum;
    }

    public void setSeeNum(int seeNum) {
        this.seeNum = seeNum;
    }

    public boolean isHasPass() {
        return hasPass;
    }

    public void setHasPass(boolean hasPass) {
        this.hasPass = hasPass;
    }
}
