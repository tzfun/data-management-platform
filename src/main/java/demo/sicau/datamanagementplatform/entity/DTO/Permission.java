package demo.sicau.datamanagementplatform.entity.DTO;


/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 17:28 2018/11/4
 * @Description:
 */
public class Permission {
    private String id;

    /**
     * 资源
     */
    private String resource;

    /**
     * 操作
     */
    private String action;

    @Override
    public String toString(){
        return "id="+this.id+",resource="+this.resource+",action="+this.action;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
