package demo.sicau.datamanagementplatform.dao;

import demo.sicau.datamanagementplatform.entity.DTO.Notice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 21:24 2018/12/22
 * @Description:
 */
@Mapper
public interface NoticeDao {
    /**
     * 插入一条公告
     * @param notice 公告实体
     * @return boolean
     */
    boolean insert(@Param("notice") Notice notice);

    /**
     * 通过id查询公告
     * @param id 公告id
     * @return Notice
     */
    Notice selectById(@Param("id") String id);

    /**
     * 增加访问量
     */
    void addSeeNum(@Param("id") String id);

    /**
     * 查询最近几条长期公告
     * @param longTermNum 查询数目
     * @return  ArrayList<Notice>
     */
    ArrayList<Notice> selectLongTermNoticeByNum(int longTermNum);

    /**
     * 查询最近几条短期公告
     * @param shortTermNum 查询数目
     * @return ArrayList<Notice>
     */
    ArrayList<Notice> selectShortTermNoticeByNum(int shortTermNum);

    /**
     * 查询所有公告
     * @param page 当前页
     * @param pageSize 页面大小
     * @return ArrayList<Notice>
     */
    ArrayList<Notice> select(@Param("page") int page, @Param("pageSize") int pageSize);

    /**
     * 统计所有公告数目
     * @return int
     */
    int countAll();

    /**
     * 删除公告
     * @param id 公告id
     */
    boolean deleteById(@Param("id") String id);
}
