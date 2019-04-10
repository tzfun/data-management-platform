package demo.sicau.datamanagementplatform.entity.DTO;

/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 23:45 2018/11/24
 * @Description:
 */
public class ResourceInfo extends Resource {

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件大小
     * 单位KB
     */
    private long fileSize;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 文件类型（全称）
     */
    private String fileFullType;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileFullType() {
        return fileFullType;
    }

    public void setFileFullType(String fileFullType) {
        this.fileFullType = fileFullType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
}
