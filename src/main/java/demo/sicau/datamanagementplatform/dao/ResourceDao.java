package demo.sicau.datamanagementplatform.dao;

import demo.sicau.datamanagementplatform.entity.DTO.Resource;
import demo.sicau.datamanagementplatform.entity.DTO.ResourceInfo;
import demo.sicau.datamanagementplatform.entity.POJO.PO.ResourcePO;
import demo.sicau.datamanagementplatform.entity.POJO.VO.ResourceArticleVO;
import demo.sicau.datamanagementplatform.entity.POJO.VO.ResourceListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 16:42 2018/11/24
 * @Description:
 */
@Mapper
public interface ResourceDao {
    /**
     * 插入资源
     * @param resource 资源对象
     * @param userId 作者id
     * @return boolean
     */
    boolean insertResource(@Param("resource") ResourceInfo resource, @Param("userId") String userId);

    /**
     * 通过id查询某一资源文章
     * @param id 资源id
     * @return ResourceArticleVO
     */
    ResourceArticleVO selectResourceArticleById(@Param("id") String id);

    /**
     * 更新浏览量
     * @param id 资源id
     * @return boolean
     */
    boolean updateSeeNum(@Param("id") String id);

    /**
     * 通过资源类型分页查询资源列表(不包含未通过的)
     * @param page 当前索引
     * @param pageSize 查询大小
     * @param type 资源类型
     * @return ResourceListVO
     */
    ArrayList<ResourceListVO> selectResourceListByType(@Param("page") int page,@Param("pageSize") int pageSize, @Param("type") String type);

    /**
     * 分页查询文章列表(不包含未通过的)
     * @param page 当前索引
     * @param pageSize 查询数据量
     * @return ResourceListVO
     */
    ArrayList<ResourceListVO> selectResourceList(@Param("page") int page,@Param("pageSize") int pageSize);

    /**
     * 统计某一类型的资源数目（只包含通过部分）
     * @param type 资源类型
     * @return int
     */
    int countResource(@Param("type") String type);

    /**
     * 更新下载数目（+1）
     * @param resourceId 资源id
     * @return boolean
     */
    boolean updateDownloadNum(@Param("id") String resourceId);

    /**
     * 分页查询资源列表（按照下载量由高到低排序）(不包含未通过的)
     * @param page 当前索引
     * @param pageSize 查询数量
     * @return ResourceListVO
     */
    ArrayList<ResourceListVO> selectResourceListOrderByDownload(@Param("page") int page,@Param("pageSize") int pageSize);

    /**
     * 通过用户id查询资源
     * @param page 当前页
     * @param pageSize 页面大小
     * @param userId 用户id
     * @return ArrayList<ResourcePO>
     */
    ArrayList<ResourcePO> selectResourceByUserId(@Param("page") int page,
                                                 @Param("pageSize") int pageSize,
                                                 @Param("userId") String userId);

    /**
     * 根据用户id查询总数
     * @param userId 用户id
     * @return int
     */
    int selectTotalByUserId(@Param("userId") String userId);

    /**
     * 通过资源id和用户id删除资源记录
     * @param id 资源id
     * @param userId 用户id
     * @return boolean
     */
    boolean deleteByUserIdAndId(@Param("id") String id,
                                @Param("userId") String userId);

    /**
     * 通过资源id查询资源信息
     * @param id 资源id
     * @return Resource
     */
    ResourcePO selectResourceById(@Param("id") String id);

    /**
     * 查询所有的资源
     * @param page  当前页
     * @param pageSize 页面大小
     * @return ArrayList<ResourcePO>
     */
    ArrayList<ResourcePO> selectAll(@Param("page") int page,@Param("pageSize") int pageSize);

    /**
     * 统计所有资源的数目
     * @return int
     */
    int countAll();

    /**
     * 通过文章id删除文章
     * @param id 文章id
     * @return boolean
     */
    boolean deleteById(@Param("id") String id);

    /**
     * 修改通过状态
     * @param id 资源id
     * @param hasPass 修改后的状态
     * @return boolean
     */
    boolean updatePassStatus(@Param("id") String id, @Param("hasPass") int hasPass);

}
