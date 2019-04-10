package demo.sicau.datamanagementplatform.entity.POJO.VO;

import demo.sicau.datamanagementplatform.entity.DTO.ResourceInfo;
import demo.sicau.datamanagementplatform.entity.DTO.User;

/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 11:56 2018/11/25
 * @Description:
 */
public class ResourceListVO {

    private User author;

    private ResourceInfo resource;

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public ResourceInfo getResource() {
        return resource;
    }

    public void setResource(ResourceInfo resource) {
        this.resource = resource;
    }
}
