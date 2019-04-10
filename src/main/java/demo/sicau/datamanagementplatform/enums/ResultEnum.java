package demo.sicau.datamanagementplatform.enums;

/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 18:21 2018/10/30
 * @Description: 响应结果枚举类
 */
public enum ResultEnum {

    UNKNOWN_ERROR(-1,"未知错误"),
    SUCCESS(0,"成功"),
    PARAM_ERROR(10001, "参数不正确"),
    LOGIN_FAILED(10002, "登录失败"),
    USER_NOT_FOUND(10003, "用户不存在"),
    PASSWORD_INCORRECT(10004, "密码不正确"),
    INVALID_TOKEN(10005, "无效的token"),
    ACTION_UNAUTHORIZED(10006, "操作无权限"),
    DELETE_FAILED(10008, "删除失败"),
    FILE_FORMAT_WRONG(10009,"文件格式错误"),
    RESOURCE_NOT_FOUND(100010, "资源不存在"),
    RESOURCE_SIZE_TOO_BIG(100011, "资源文件太大"),
    UPLOADS_TOO_MANY(100012, "上传数量过多"),
    VERIFY_CODE_ERROR(100013,"验证码错误"),
    VERIFY_CODE_INVALID(100014,"验证码已失效"),
    ACTION_TOO_FAST(100015,"操作太快"),
    ILLEGAL_DOS(100016,"非法请求拦截"),
    PROHIBITED_PERIOD(100017,"ip禁用期间无法请求"),
    RESOURCE_EXIST(100018,"资源已存在"),
    DOWNLOAD_DIGEST_NOT_FOUND(100019,"未传入下载文件消息摘要"),
    CANNOT_DEL_SELF(100020,"不能删除自己");

    private Integer status;

    private String msg;

    ResultEnum(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public Integer getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }
}
