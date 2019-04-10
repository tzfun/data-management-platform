package demo.sicau.datamanagementplatform.controller;

import demo.sicau.datamanagementplatform.constants.*;
import demo.sicau.datamanagementplatform.entity.DTO.Notice;
import demo.sicau.datamanagementplatform.entity.POJO.VO.ResultVO;
import demo.sicau.datamanagementplatform.enums.ResultEnum;
import demo.sicau.datamanagementplatform.service.NoticeService;
import demo.sicau.datamanagementplatform.util.TokenUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 21:24 2018/12/22
 * @Description:
 */
@RestController
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    /**
     * 新增公告
     * @param title 标题
     * @param content   内容
     * @param longTerm  是否是长期
     * @param token 用户消息摘要
     * @return  ResultVO
     */
    @RequiresPermissions(ResourceConstants.NOTICE + PermissionActionConstant.ADD)
    @PostMapping(CommonConstants.NONPUBLIC_PREFIX + "/" + ApiConstants.NOTICE + "/add")
    public ResultVO addNotice(@RequestParam("title") String title,
                              @RequestParam("content") String content,
                              @RequestParam("long_term") boolean longTerm,
                              @RequestParam("summary") String summary,
                              @RequestHeader(HttpParamKeyConstants.CLIENT_DIGEST) String token){
        Notice notice = new Notice(
                UUID.randomUUID().toString().replace("-",""),
                new TokenUtil().getUserKeyByToken(token),
                title,
                content,
                summary,
                longTerm);
        return noticeService.addNotice(notice);
    }

    /**
     * 通过公告id查询信息
     * @param id    公告id
     * @return  ResultVO
     */
    @GetMapping(CommonConstants.PUB_PREFIX + "/" + ApiConstants.NOTICE + "/select")
    public ResultVO select(@RequestParam("id") String id){
        return noticeService.select(id);
    }

    /**
     * 通过类型列出公告
     * @param longTermNum 长期公告数目
     * @param shortTermNum 短期公告数目
     * @return  ResultVO
     */
    @GetMapping(CommonConstants.PUB_PREFIX + "/" + ApiConstants.NOTICE + "/list_by_type")
    public ResultVO listNoticeByType(@RequestParam("long_term_num") int longTermNum,
                                     @RequestParam("short_term_num") int shortTermNum){
        return noticeService.listNoticeByType(longTermNum,shortTermNum);
    }

    /**
     * 查询所有公告
     * @param page 当前页面
     * @param pageSize 页面大小
     * @return ResultVO
     */
    @RequiresPermissions(ResourceConstants.NOTICE + PermissionActionConstant.SELECT)
    @GetMapping(CommonConstants.NONPUBLIC_PREFIX + "/" + ApiConstants.NOTICE + "/list_all")
    public ResultVO listAll(@RequestParam("page") int page,
                           @RequestParam("page_size") int pageSize){
        return noticeService.listAll(page,pageSize);
    }

    /**
     * 通过公告id删除
     * @param id 公告id
     * @return ResultVO
     */
    @RequiresPermissions(ResourceConstants.NOTICE + PermissionActionConstant.DELETE)
    @DeleteMapping(CommonConstants.NONPUBLIC_PREFIX + "/" + ApiConstants.NOTICE + "/delete")
    public ResultVO listAll(@RequestParam("id") String id){
        return noticeService.deleteById(id);
    }
}
