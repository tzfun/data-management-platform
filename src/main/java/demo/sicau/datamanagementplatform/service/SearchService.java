package demo.sicau.datamanagementplatform.service;

import demo.sicau.datamanagementplatform.entity.POJO.VO.ResultVO;

/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 0:36 2018/12/25
 * @Description:
 */
public interface SearchService {
    /**
     * 获取公共搜索结果
     * 为了节省数据库查询
     * 每一个keyword搜索出的结果加入2分钟缓存
     * @return ResultVO
     */
    ResultVO publicSearch(String keyword);
}
