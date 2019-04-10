package demo.sicau.datamanagementplatform.service;

import demo.sicau.datamanagementplatform.entity.DTO.Notice;
import demo.sicau.datamanagementplatform.entity.POJO.VO.ResultVO;

/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 21:25 2018/12/22
 * @Description:
 */
public interface NoticeService {
    /**
     * 新增公告
     * @param notice @Link{Notice}
     * @return ResultVO
     */
    ResultVO addNotice(Notice notice);

    /**
     * 通过公告id查询信息
     * @param id    公告id
     * @return  ResultVO
     */
    ResultVO select(String id);

    /**
     * 通过类型列出公告
     * @param longTermNum 长期公告数目
     * @param shortTermNum 短期公告数目
     * @return  ResultVO
     */
    ResultVO listNoticeByType(int longTermNum, int shortTermNum);

    /**
     * 查询所有公告
     * @param page 当前页面
     * @param pageSize 页面大小
     * @return ResultVO
     */
    ResultVO listAll(int page, int pageSize);

    /**
     * 通过公告id删除
     * @param id 公告id
     * @return ResultVO
     */
    ResultVO deleteById(String id);
}
