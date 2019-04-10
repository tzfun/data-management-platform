package demo.sicau.datamanagementplatform.entity.DTO;


/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 11:17 2018/11/24
 * @Description:
 */
public class Resource {
    private String id;
    /**
     * 文件资源路径
     */
    private String  fileUrl;
    /**
     * 资源图片路径
     */
    private String imgUrl;

    private String title;

    private String summary;

    private String introduction;

    private String type;

    private int seeNum;

    private int downloadNum;

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSeeNum() {
        return seeNum;
    }

    public void setSeeNum(int seeNum) {
        this.seeNum = seeNum;
    }

    public int getDownloadNum() {
        return downloadNum;
    }

    public void setDownloadNum(int downloadNum) {
        this.downloadNum = downloadNum;
    }
}
