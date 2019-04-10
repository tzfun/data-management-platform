package demo.sicau.datamanagementplatform.entity.POJO.VO;

import demo.sicau.datamanagementplatform.entity.DTO.User;
import demo.sicau.datamanagementplatform.entity.POJO.PO.ArticlePO;

/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 21:21 2018/11/20
 * @Description:
 */
public class ArticleListVO {

    private User author;

    private ArticlePO article;

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public ArticlePO getArticle() {
        return article;
    }

    public void setArticle(ArticlePO article) {
        this.article = article;
    }
}
