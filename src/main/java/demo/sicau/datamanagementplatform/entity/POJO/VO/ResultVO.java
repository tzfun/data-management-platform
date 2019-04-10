package demo.sicau.datamanagementplatform.entity.POJO.VO;

/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 18:10 2018/10/30
 * @Description:
 */
public class ResultVO {
    private int status;

    private String msg;

    private Object data;

    public ResultVO() {

    }

    public ResultVO(Integer status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public ResultVO(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
