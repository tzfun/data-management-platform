package demo.sicau.datamanagementplatform.service.imp;

import demo.sicau.datamanagementplatform.entity.POJO.VO.ResultFileVO;
import demo.sicau.datamanagementplatform.service.FileService;
import demo.sicau.datamanagementplatform.util.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 21:01 2018/11/16
 * @Description:
 */
@Service
public class FileServiceImp implements FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileServiceImp.class);
    @Autowired
    private FileUtil fileUtil;

    @Value("${server.host}")
    private String host;

    @Override
    public ResultFileVO uploadArticleImg(String userId, MultipartFile file) {
        ResultFileVO resultFileVO = new ResultFileVO();
        try{
            if (file.isEmpty() || StringUtils.isBlank(file.getOriginalFilename())) {
                throw new Exception("Img not empty!");
            }
            String contentType = file.getContentType();
            if(!contentType.contains("")){
                throw new Exception("Img format error!");
            }
            String originalFilename = file.getOriginalFilename();
            logger.info("上传图片：name={},type={}",originalFilename,contentType);
            String filePath = fileUtil.getArticleImgLocalPath(userId);
            String fileName = fileUtil.saveFile(file,filePath);
            String url = host + "/" + fileUtil.getArticleImgUrl(userId) + "/" + fileName;
            logger.info("保存图片：url={}",url);
            resultFileVO.setUrl(url);
            resultFileVO.setUploaded(true);
            resultFileVO.setMsg("上传成功");
            return resultFileVO;
        }catch (Exception e){
            e.printStackTrace();
            resultFileVO.setUrl("/");
            resultFileVO.setMsg("上传出错，"+e.getMessage());
            resultFileVO.setUploaded(false);
            return resultFileVO;
        }
    }
}
