package demo.sicau.datamanagementplatform.controller;

import demo.sicau.datamanagementplatform.constants.*;
import demo.sicau.datamanagementplatform.entity.DTO.Article;
import demo.sicau.datamanagementplatform.entity.POJO.VO.ResultVO;
import demo.sicau.datamanagementplatform.service.ArticleService;
import demo.sicau.datamanagementplatform.util.TokenUtil;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 14:58 2018/11/20
 * @Description:
 */
@RestController
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 上传文章
     * @param article 文章对象
     * @return ResultVO
     */
    @RequiresPermissions(ResourceConstants.ARTICLE + PermissionActionConstant.ADD)
    @PostMapping(CommonConstants.NONPUBLIC_PREFIX + "/" + ApiConstants.ARTICLE + "/upload")
    public ResultVO upload(@RequestBody Article article, @RequestHeader(HttpParamKeyConstants.CLIENT_DIGEST) String token){
        article.setId(UUID.randomUUID().toString().replace("-",""));
        return articleService.upload(article, new TokenUtil().getUserKeyByToken(token));
    }

    /**
     * 查询某篇文章具体内容
     * @param id 文章id
     * @return ResultVO
     */
    @GetMapping(CommonConstants.PUB_PREFIX + "/" + ApiConstants.ARTICLE + "/get")
    public ResultVO getArticle(@RequestParam("id") String id){
        return articleService.getById(id);
    }

    /**
     * 分页查询文章简介信息列表
     * @param page 当前页
     * @param pageSize 一页显示数量
     * @return ResultVO
     */
    @GetMapping(CommonConstants.PUB_PREFIX + "/" +ApiConstants.ARTICLE + "/get_list")
    public ResultVO getArticleList(@RequestParam("page") int page,
                                   @RequestParam("page_size") int pageSize){
        return articleService.getList(page,pageSize);
    }

    /**
     * 查询个人的文章列表
     * @param page 当前页
     * @param pageSize 一页显示的数量
     * @return ResultVO
     */
    @GetMapping(CommonConstants.NONPUBLIC_PREFIX + "/" + ApiConstants.ARTICLE + "/list_personal")
    public ResultVO getPersonalArticleList(@RequestParam("page") int page,
                                           @RequestParam("page_size") int pageSize,
                                           @RequestHeader(HttpParamKeyConstants.CLIENT_DIGEST) String token){
        return articleService.getPersonalArticleList(page,pageSize,new TokenUtil().getUserKeyByToken(token));
    }

    /**
     * 删除个人文章
     * @param id 文章id
     * @return ResultVO
     */
    @DeleteMapping(CommonConstants.NONPUBLIC_PREFIX + "/" + ApiConstants.ARTICLE + "/del_personal")
    public ResultVO deletePersonalArticle(@RequestParam("id") String id,
                                          @RequestHeader(HttpParamKeyConstants.CLIENT_DIGEST) String token){
        return articleService.delPersonalArticleById(id,new TokenUtil().getUserKeyByToken(token));
    }

    /**
     * 删除文章
     * @param id 文章id
     * @return ResultVO
     */
    @RequiresPermissions(ResourceConstants.ARTICLE + PermissionActionConstant.DELETE)
    @DeleteMapping(CommonConstants.NONPUBLIC_PREFIX + "/" + ApiConstants.ARTICLE + "/delete")
    public ResultVO deleteArticle(@RequestParam("id") String id){
        return articleService.delArticleById(id);
    }

    /**
     * 更新通过状态
     * @param id 文章id
     * @param hasPass 是否通过
     * @return ResultVO
     */
    @RequiresPermissions(ResourceConstants.ARTICLE + PermissionActionConstant.UPDATE)
    @PutMapping(CommonConstants.NONPUBLIC_PREFIX + "/" + ApiConstants.ARTICLE + "/update_pass_status")
    public ResultVO updatePassStatus(@RequestParam("id") String id,
                                     @RequestParam("has_pass") int hasPass){
        return articleService.updatePassStatus(id,hasPass);
    }

    /**
     * 分页查询文章信息列表
     * @param page 当前页
     * @param pageSize 一页显示数量
     * @return ResultVO
     * article:select
     */
    @RequiresPermissions(ResourceConstants.ARTICLE + PermissionActionConstant.SELECT)
    @GetMapping(CommonConstants.NONPUBLIC_PREFIX + "/" +ApiConstants.ARTICLE + "/list")
    public ResultVO list(@RequestParam("page") int page,
                         @RequestParam("page_size") int pageSize){
        return articleService.list(page,pageSize);
    }

    /**
     * 更新文章
     * @param article 文章实体
     * @return ResultVO
     */
    @RequiresPermissions(ResourceConstants.ARTICLE + PermissionActionConstant.UPDATE)
    @PutMapping(CommonConstants.NONPUBLIC_PREFIX + "/" +ApiConstants.ARTICLE + "/update")
    public ResultVO update(@RequestBody Article article){
        return articleService.update(article);
    }
}
