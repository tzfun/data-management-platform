package demo.sicau.datamanagementplatform.entity.POJO.PO;

import demo.sicau.datamanagementplatform.entity.DTO.Resource;
import demo.sicau.datamanagementplatform.entity.DTO.ResourceInfo;

import java.util.Date;

/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 16:27 2018/11/24
 * @Description:
 */
public class ResourcePO extends ResourceInfo {
    private String createTime;

    private String updateTime;

    private String userId;

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

    public boolean isHasPass() {
        return hasPass;
    }

    public void setHasPass(boolean hasPass) {
        this.hasPass = hasPass;
    }
}
