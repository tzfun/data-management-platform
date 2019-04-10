package demo.sicau.datamanagementplatform.controller;

import demo.sicau.datamanagementplatform.constants.*;
import demo.sicau.datamanagementplatform.entity.POJO.VO.ResultFileVO;
import demo.sicau.datamanagementplatform.service.FileService;
import demo.sicau.datamanagementplatform.util.TokenUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 20:55 2018/11/16
 * @Description:
 */
@RestController
public class FileController {

    @Autowired
    private FileService fileService;

    /**
     * 上传文章图片
     * @param token 消息摘要
     * @param file 文件
     * @return ResultVO
     */
    @RequiresPermissions(ResourceConstants.IMAGE + PermissionActionConstant.ADD)
    @PostMapping(CommonConstants.NONPUBLIC_PREFIX+ "/" + ApiConstants.FILE + "/article_img")
    public ResultFileVO uploadArticleImg(@RequestHeader(HttpParamKeyConstants.CLIENT_DIGEST) String token,
                                         @RequestParam("upload") MultipartFile file){
        return fileService.uploadArticleImg(new TokenUtil().getUserKeyByToken(token),file);
    }


}
