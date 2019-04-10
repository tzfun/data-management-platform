package demo.sicau.datamanagementplatform.entity.DTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 15:02 2018/11/20
 * @Description:
 */
public class Article {

    private String id;

    private String title;

    private String summary;

    private String content;

    private List<String> tags;

    private String strTags;

    public Article() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getStrTags() {
        return strTags;
    }

    public void setStrTags(String strTags) {
        this.strTags = strTags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public List<String> getTags() {
        return tags;
    }
}
