package demo.sicau.datamanagementplatform.constants;

/**
 * @Author beifengtz
 * @Date Created in 10:08 2018/9/22
 * @Description:
 */
public interface CommonConstants {
    String API_VERSION = "v1";//api版本号

    String NONPUBLIC_PREFIX = API_VERSION + "/nonpub";//非公共api的前缀

    String PUB_PREFIX = API_VERSION + "/pub";//公共api前缀

    String DOWNLOAD_PREFIX = NONPUBLIC_PREFIX + "/download";//文件下载api前缀
}
