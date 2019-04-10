package demo.sicau.datamanagementplatform.service;

import demo.sicau.datamanagementplatform.entity.POJO.VO.ResultFileVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 21:01 2018/11/16
 * @Description:
 */
public interface FileService {

    /**
     * 上传文章图片
     * @param token 消息摘要
     * @param file 文件
     * @return ResultVO
     */
    ResultFileVO uploadArticleImg(String userId, MultipartFile file);

}
