package demo.sicau.datamanagementplatform.entity.POJO.VO;

import demo.sicau.datamanagementplatform.entity.DTO.ResourceInfo;
import demo.sicau.datamanagementplatform.entity.DTO.User;
import demo.sicau.datamanagementplatform.entity.POJO.PO.ResourcePO;

/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 21:13 2018/11/24
 * @Description:
 */
public class ResourceArticleVO {
    private ResourcePO resource;

    private User author;

    public ResourcePO getResource() {
        return resource;
    }

    public void setResource(ResourcePO resource) {
        this.resource = resource;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}
