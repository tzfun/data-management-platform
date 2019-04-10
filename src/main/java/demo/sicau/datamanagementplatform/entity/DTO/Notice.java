package demo.sicau.datamanagementplatform.entity.DTO;

/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 21:22 2018/12/22
 * @Description:
 */
public class Notice {
    private String id;

    private String userId;

    private String title;

    private String content;

    private boolean longTerm;

    private String createTime;

    private String seeNum;

    private String summary;

    public Notice() {
    }

    public Notice(String id, String userId, String title, String content,String summary, boolean longTerm) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.summary = summary;
        this.longTerm = longTerm;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isLongTerm() {
        return longTerm;
    }

    public void setLongTerm(boolean longTerm) {
        this.longTerm = longTerm;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getSeeNum() {
        return seeNum;
    }

    public void setSeeNum(String seeNum) {
        this.seeNum = seeNum;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
