package demo.sicau.datamanagementplatform.entity.POJO.VO;

/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 21:02 2018/11/16
 * @Description: 文章图片上传返回实体类
 */
public class ResultFileVO {
    private boolean uploaded;

    private String url;

    private String msg;

    public boolean isUploaded() {
        return uploaded;
    }

    public void setUploaded(boolean uploaded) {
        this.uploaded = uploaded;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
