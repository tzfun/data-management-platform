package demo.sicau.datamanagementplatform.service;

import demo.sicau.datamanagementplatform.entity.DTO.Article;
import demo.sicau.datamanagementplatform.entity.POJO.VO.ResultVO;

/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 14:59 2018/11/20
 * @Description:
 */
public interface ArticleService {

    /**
     * 上传文章
     * @param article 文章对象
     * @return ResultVO
     */
    ResultVO upload(Article article,String userId);

    /**
     * 查询某篇文章具体内容
     * @param id 文章id
     * @return ResultVO
     */
    ResultVO getById(String id);

    /**
     * 分页查询文章简介信息列表
     * @param page 当前页
     * @param pageSize 一页显示数量
     * @return ResultVO
     */
    ResultVO getList(int page, int pageSize);

    /**
     * 查询个人的文章列表
     * @param page 当前页
     * @param pageSize 一页显示的数量
     * @return ResultVO
     */
    ResultVO getPersonalArticleList(int page, int pageSize,String userId);

    /**
     * 删除个人文章
     * @param id 文章id
     * @param userId 用户id
     * @return  ResultVO
     */
    ResultVO delPersonalArticleById(String id, String userId);

    /**
     * 删除文章
     * @param id 文章id
     * @return ResultVO
     */
    ResultVO delArticleById(String id);

    /**
     * 更新通过状态
     * @param id 文章id
     * @param hasPass 是否通过
     * @return ResultVO
     */
    ResultVO updatePassStatus(String id, int hasPass);

    /**
     * 分页查询文章简介信息列表
     * @param page 当前页
     * @param pageSize 一页显示数量
     * @return ResultVO
     */
    ResultVO list(int page, int pageSize);

    /**
     * 更新文章
     * @param article 文章实体
     * @return ResultVO
     */
    ResultVO update(Article article);
}
