package demo.sicau.datamanagementplatform.entity.POJO.VO;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 11:19 2018/11/24
 * @Description:
 */
public class ResourceVO {
    private String id;
    /**
     * 文件资源
     */
    private MultipartFile file;
    /**
     * 资源图片
     */
    private MultipartFile img;

    private String title;

    private String summary;

    private String introduction;

    private String type;

    public ResourceVO(){}

    public ResourceVO(String id,MultipartFile file, MultipartFile img, String title, String summary, String introduction, String type) {
        this.id = id;
        this.file = file;
        this.img = img;
        this.title = title;
        this.summary = summary;
        this.introduction = introduction;
        this.type = type;
    }


    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
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

    public MultipartFile getImg() {
        return img;
    }

    public void setImg(MultipartFile img) {
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
