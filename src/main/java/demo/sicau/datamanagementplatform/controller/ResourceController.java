package demo.sicau.datamanagementplatform.controller;

import demo.sicau.datamanagementplatform.constants.*;
import demo.sicau.datamanagementplatform.entity.POJO.VO.ResourceVO;
import demo.sicau.datamanagementplatform.entity.POJO.VO.ResultVO;
import demo.sicau.datamanagementplatform.service.ResourceService;
import demo.sicau.datamanagementplatform.util.TokenUtil;
import jdk.nashorn.internal.parser.Token;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 11:05 2018/11/24
 * @Description:
 */
@RestController
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    /**
     * 上传资源接口
     * @param file 资源文件
     * @param type 资源类型
     * @param img 图片文件
     * @param title 标题
     * @param summary 摘要
     * @param introduction 资源介绍
     * @return ResultVO
     */
    @RequiresPermissions(ResourceConstants.RESOURCE + PermissionActionConstant.ADD)
    @PostMapping(CommonConstants.NONPUBLIC_PREFIX + "/" + ApiConstants.RESOURCE + "/upload")
    public ResultVO uploadResource(@RequestParam("file") MultipartFile file,
                                   @RequestParam("type") String type,
                                   @RequestParam("img") MultipartFile img,
                                   @RequestParam("title") String title,
                                   @RequestParam("summary") String summary,
                                   @RequestParam(value = "introduction",required = false,defaultValue = "") String introduction,
                                   @RequestHeader(HttpParamKeyConstants.CLIENT_DIGEST) String token){

        ResourceVO resourceVO = new ResourceVO(UUID.randomUUID().toString().replace("-",""),file,img,title,summary,introduction,type);
        return resourceService.uploadResource(resourceVO,new TokenUtil().getUserKeyByToken(token));
    }

    /**
     * 获取资源信息
     * @param id 文章id
     * @return ResultVO
     */
    @GetMapping(CommonConstants.PUB_PREFIX + "/" + ApiConstants.RESOURCE + "/get")
    public ResultVO getResource(@RequestParam("id") String id){
        return resourceService.getById(id);
    }

    /**
     * 分页获取资源列表
     * @param page 当前页
     * @param pageSize 页面显示数量
     * @param type 资源类型
     * @return ResultVO
     */
    @GetMapping(CommonConstants.PUB_PREFIX + "/" + ApiConstants.RESOURCE + "/type_list")
    public ResultVO getListByType(@RequestParam("page") int page,
                            @RequestParam("page_size") int pageSize,
                            @RequestParam("type") String type){
        return resourceService.getListByType(page,pageSize,type);
    }

    /**
     * 获取资源列表
     * @param page 当前页
     * @param pageSize 页面显示数量
     * @return ResultVO
     */
    @GetMapping(CommonConstants.PUB_PREFIX + "/" + ApiConstants.RESOURCE + "/list")
    public ResultVO getList(@RequestParam("page") int page,
                                  @RequestParam("page_size") int pageSize){
        return resourceService.getList(page,pageSize);
    }

    /**
     * 获取热门下载的资源
     * @param page 当前页面
     * @param pageSize 页面大小
     * @return ResultVO
     */
    @GetMapping(CommonConstants.PUB_PREFIX + "/" + ApiConstants.RESOURCE + "/list_download_hot")
    public ResultVO getDownloadHot(@RequestParam("page") int page,
                           @RequestParam("page_size") int pageSize){
        return resourceService.getDownloadHot(page,pageSize);
    }

    /**
     * 查询用户的资源列表
     * @param page  当前页
     * @param pageSize 页面数据量
     * @param token 消息摘要
     * @return ResultVO
     */
    @GetMapping(CommonConstants.NONPUBLIC_PREFIX + "/" + ApiConstants.RESOURCE + "/list_personal")
    public ResultVO listPersonal(@RequestParam("page") int page,
                                 @RequestParam("page_size") int pageSize,
                                 @RequestHeader(HttpParamKeyConstants.CLIENT_DIGEST) String token){
        return resourceService.listPersonal(page,pageSize,new TokenUtil().getUserKeyByToken(token));
    }

    /**
     * 删除个人上传的资源
     * @param id 资源id
     * @param token 用户消息摘要
     * @return ResultVO
     */
    @DeleteMapping(CommonConstants.NONPUBLIC_PREFIX + "/" + ApiConstants.RESOURCE + "/del_personal")
    public ResultVO delPersonal(@RequestParam("id") String id,
                                @RequestHeader(HttpParamKeyConstants.CLIENT_DIGEST) String token){
        return resourceService.delPersonal(id,new TokenUtil().getUserKeyByToken(token));
    }

    /**
     * 查询所有的资源
     * @param page 当前页
     * @param pageSize 页面大小
     * @return ResultVO
     */
    @RequiresPermissions(ResourceConstants.RESOURCE + PermissionActionConstant.SELECT)
    @GetMapping(CommonConstants.NONPUBLIC_PREFIX + "/" + ApiConstants.RESOURCE + "/list_all")
    public ResultVO listAll(@RequestParam("page") int page,
                            @RequestParam("page_size") int pageSize){
        return resourceService.listAll(page,pageSize);
    }

    /**
     * 删除文章
     * @param id 文章id
     * @return ResultVO
     */
    @RequiresPermissions(ResourceConstants.RESOURCE + PermissionActionConstant.DELETE)
    @DeleteMapping(CommonConstants.NONPUBLIC_PREFIX + "/" + ApiConstants.RESOURCE + "/delete")
    public ResultVO delete(@RequestParam("id") String id){
        return resourceService.delete(id);
    }

    /**
     * 修改资源通过状态
     * @param id 资源id
     * @param hasPass 是否通过
     * @return ResultVO
     */
    @RequiresPermissions(ResourceConstants.RESOURCE + PermissionActionConstant.UPDATE)
    @PutMapping(CommonConstants.NONPUBLIC_PREFIX + "/" + ApiConstants.RESOURCE + "/update_pass_status")
    public ResultVO updatePassStatus(@RequestParam("id") String id,
                                     @RequestParam("has_pass") int hasPass){
        return resourceService.updatePassStatus(id,hasPass);
    }
}
