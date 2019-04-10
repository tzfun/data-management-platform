package demo.sicau.datamanagementplatform.service.imp;

import demo.sicau.datamanagementplatform.dao.NoticeDao;
import demo.sicau.datamanagementplatform.entity.DTO.Notice;
import demo.sicau.datamanagementplatform.entity.POJO.VO.ResultVO;
import demo.sicau.datamanagementplatform.enums.ResultEnum;
import demo.sicau.datamanagementplatform.service.NoticeService;
import demo.sicau.datamanagementplatform.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 21:25 2018/12/22
 * @Description:
 */
@Service
public class NoticeServiceImp implements NoticeService {

    @Autowired
    private NoticeDao noticeDao;
    @Autowired
    private ResultUtil resultUtil;

    @Override
    public ResultVO addNotice(Notice notice) {
        try{
            noticeDao.insert(notice);
            return resultUtil.success(notice.getId());
        }catch (Exception e){
            e.printStackTrace();
            return resultUtil.error(ResultEnum.UNKNOWN_ERROR.getStatus(),e.getMessage());
        }
    }

    @Override
    public ResultVO select(String id) {
        try{
            Notice notice = noticeDao.selectById(id);
            if (notice!=null){
                noticeDao.addSeeNum(id);
                return resultUtil.success(notice);
            }else{
                return resultUtil.error(ResultEnum.RESOURCE_NOT_FOUND);
            }
        }catch (Exception e){
            e.printStackTrace();
            return resultUtil.error(ResultEnum.UNKNOWN_ERROR.getStatus(),e.getMessage());
        }
    }

    @Override
    public ResultVO listNoticeByType(int longTermNum, int shortTermNum) {
        try{
            ArrayList<Notice> longTermNotice = noticeDao.selectLongTermNoticeByNum(longTermNum);
            ArrayList<Notice> shortTermNotice = noticeDao.selectShortTermNoticeByNum(shortTermNum);
            HashMap<String,Object> resMap = new HashMap<>();
            resMap.put("longTerm",longTermNotice);
            resMap.put("shortTerm",shortTermNotice);
            return resultUtil.success(resMap);
        }catch (Exception e){
            e.printStackTrace();
            return resultUtil.error(ResultEnum.UNKNOWN_ERROR.getStatus(),e.getMessage());
        }
    }

    @Override
    public ResultVO listAll(int page, int pageSize) {
        try{
            ArrayList<Notice> notices = noticeDao.select(page,pageSize);
            int total = noticeDao.countAll();
            HashMap<String,Object> resMap = new HashMap<>();
            resMap.put("rows",notices);
            resMap.put("total",total);
            return resultUtil.success(resMap);
        }catch (Exception e){
            e.printStackTrace();
            return resultUtil.error(ResultEnum.UNKNOWN_ERROR.getStatus(),e.getMessage());
        }
    }

    @Override
    public ResultVO deleteById(String id) {
        try{
            noticeDao.deleteById(id);
            return resultUtil.success();
        }catch (Exception e){
            e.printStackTrace();
            return resultUtil.error(ResultEnum.UNKNOWN_ERROR.getStatus(),e.getMessage());
        }
    }
}
