package demo.sicau.datamanagementplatform.service;

import demo.sicau.datamanagementplatform.entity.POJO.VO.ResourceVO;
import demo.sicau.datamanagementplatform.entity.POJO.VO.ResultVO;

/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 11:05 2018/11/24
 * @Description:
 */
public interface ResourceService {

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
    ResultVO uploadResource(ResourceVO resourceVO,String userId);

    /**
     * 获取资源信息
     * @param id 文章id
     * @return ResultVO
     */
    ResultVO getById(String id);

    /**
     * 分页获取资源列表
     * @param page 当前页
     * @param page_size 页面显示数量
     * @param type 资源类型
     * @return ResultVO
     */
    ResultVO getListByType(int page, int pageSize, String type);

    /**
     * 获取资源列表
     * @param page 当前页
     * @param pageSize 页面显示数量
     * @return ResultVO
     */
    ResultVO getList(int page, int pageSize);

    /**
     * 获取热门下载的资源
     * @param page 当前页面
     * @param pageSize 页面大小
     * @return ResultVO
     */
    ResultVO getDownloadHot(int page, int pageSize);

    /**
     * 查询用户的资源列表
     * @param page  当前页
     * @param pageSize 页面数据量
     * @param token 消息摘要
     * @return ResultVO
     */
    ResultVO listPersonal(int page, int pageSize, String userId);

    /**
     * 删除个人上传的资源
     * @param id 资源id
     * @param token 用户消息摘要
     * @return ResultVO
     */
    ResultVO delPersonal(String id, String userId);

    /**
     * 查询所有的资源
     * @param page 当前页
     * @param pageSize 页面大小
     * @return ResultVO
     */
    ResultVO listAll(int page, int pageSize);

    /**
     * 删除文章
     * @param id 文章id
     * @return ResultVO
     */
    ResultVO delete(String id);

    /**
     * 修改资源通过状态
     * @param id 资源id
     * @param hasPass 是否通过
     * @return ResultVO
     */
    ResultVO updatePassStatus(String id, int hasPass);
}
