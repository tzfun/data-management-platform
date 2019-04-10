package demo.sicau.datamanagementplatform.util;

import demo.sicau.datamanagementplatform.constants.CommonConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 13:17 2018/11/17
 * @Description:
 */
@Component
public class FileUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 存放图片的路径
     */
    @Value("${file.img.location}")
    private String imgLocation;
    /**
     * 允许的图片类型
     */
    @Value("${file.img.allow-type}")
    private String imgAllowType;
    /**
     * 允许的图片大小
     */
    @Value("${file.img.allow-size}")
    private int imgAllowSize;


    /**
     * 存放资源文件的路径
     */
    @Value("${file.resource.location}")
    private String resourceLocation;
    /**
     * 允许的资源文件类型
     */
    @Value("${file.resource.allow-type}")
    private String resourceAllowType;
    /**
     * 允许的资源文件大小
     */
    @Value("${file.resource.allow-size}")
    private int resourceAllowSize;

    /**
     * 获取文章图片存储路径
     * @param userId 用户id
     * @return String
     */
    public String getArticleImgLocalPath(String userId){
        return imgLocation + "/" + getArticleImgUrl(userId);
    }

    /**
     * 获取资源文件存储路径(用户登录才可访问)
     * @param userId 用户id
     * @return String
     */
    public String getResourceLocalPath(String userId){
        return resourceLocation + "/" + getResourceUrl(userId);
    }

    /**
     * 获取文章图片存储相对路径的文件夹
     * @param userId 用户id
     * @return String
     */
    public String getArticleImgUrl(String userId){
        Calendar cal = Calendar.getInstance();
        return userId + "/" + getCurrentDatePath(cal);
    }

    /**
     * 获取资源文件存储相对路径的文件夹
     * @param userId 用户id
     * @return String
     */
    public String getResourceUrl(String userId){
        Calendar cal = Calendar.getInstance();
        return CommonConstants.DOWNLOAD_PREFIX + "/" + userId + "/" + getCurrentDatePath(cal);
    }

    /**
     * 获取当前时间结构路径
     * @param cal Calendar
     * @return String
     */
    private String getCurrentDatePath(Calendar cal){
        return cal.get(Calendar.YEAR)+ "/" + (cal.get(Calendar.MONTH)+1) + "/" + cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 检查图片类型
     * @param type 类型
     * @return 通过返回true，不通过返回false
     */
    public boolean checkImgType(String type){
        String typeList[] = imgAllowType.split("\\|");
        for (int i=0 ; i< typeList.length;i++){
            if (typeList[i].toLowerCase().equals(type.toLowerCase())){
                return true;
            }
        }
        return false;
    }

    /**
     * 检查资源文件类型
     * @param type 类型
     * @return 通过返回true，不通过返回false
     */
    public boolean checkResourceType(String type){
        String typeList[] = resourceAllowType.split("\\|");
        for (int i=0 ; i< typeList.length;i++){
            if (typeList[i].toLowerCase().equals(type.toLowerCase())){
                return true;
            }
        }
        return false;
    }

    /**
     * 检查图片大小
     * @param size 大小（单位KB）
     * @return 通过返回true，不通过返回false
     */
    public boolean checkImgSize(long size){
        if (size/(1024*1024) <= imgAllowSize) return true;
            return false;
    }

    /**
     * 检查资源文件大小
     * @param size 大小（单位KB）
     * @return 通过返回true，不通过返回false
     */
    public boolean checkResourceSize(long size){
        if (size/(1024*1024) <= resourceAllowSize) return true;
            return false;
    }

    /**
     * 文件存储
     * @param file MultipartFile类型
     * @param filePath 文件保存的本地路径
     * @return boolean
     */
    public String saveFile(MultipartFile file, String filePath) throws IOException {
        File localFile = new File(filePath);
        if(!localFile.exists()){
            localFile.mkdirs();
        }
        FileInputStream fileInputStream = (FileInputStream) file.getInputStream();
        String fileName = new Date().getTime() + "." + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath + File.separator + fileName));
        byte[] bs = new byte[1024];
        int len;
        while((len = fileInputStream.read(bs)) !=-1 ){
            bos.write(bs, 0, len);
        }
        bos.flush();
        bos.close();
        return fileName;
    }

    /**
     * 通过文件的路径删除本地文件
     * @param localPath 本地路径
     * @return boolean
     */
    public boolean delFile(String localPath) throws IOException {
        String realPth = resourceLocation + localPath;
        File file = new File(realPth);
        if (!file.exists()){    // 如果文件不存在直接返回false
            logger.warn("文件不存在，real_path={}",realPth);
            return true;
        }else{
            if (file.delete()){ // 如果删除文件成功返回true
                logger.info("删除文件成功，real_path={}",realPth);
                return true;
            }else{
                logger.error("删除文件失败，real_path={}",realPth);
                return false;
            }
        }
    }
}
