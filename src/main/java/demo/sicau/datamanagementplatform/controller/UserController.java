package demo.sicau.datamanagementplatform.controller;

import demo.sicau.datamanagementplatform.constants.*;
import demo.sicau.datamanagementplatform.entity.DTO.Permission;
import demo.sicau.datamanagementplatform.entity.DTO.User;
import demo.sicau.datamanagementplatform.entity.POJO.PO.UserPO;
import demo.sicau.datamanagementplatform.entity.POJO.VO.ResultVO;
import demo.sicau.datamanagementplatform.service.UserService;
import demo.sicau.datamanagementplatform.util.MD5Util;
import demo.sicau.datamanagementplatform.util.ResultUtil;
import demo.sicau.datamanagementplatform.util.TokenUtil;
import jdk.nashorn.internal.parser.Token;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import javax.xml.transform.Result;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 19:22 2018/11/7
 * @Description:
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private ResultUtil resultUtil;

    /**
     * 用户登录接口
     * @param account 账号
     * @param password 密码
     * @param credentialValue 验证码（用户输入）
     * @param credentialKey 验证码key（header中传入）
     * @return ResultVO
     */
    @PostMapping(CommonConstants.PUB_PREFIX+"/"+ ApiConstants.USER+"/login")
    public ResultVO login(@RequestParam(name = "account") String account,
                          @RequestParam("password") String password,
                          @RequestParam("credential_value") String credentialValue,
                          @RequestParam("credential_key") String credentialKey){
        return userService.login(account,password,credentialKey,credentialValue);
    }

    /**
     * 公告用户注册接口
     * @param account 账号
     * @param password 密码
     * @param credentialValue 验证码（用户输入）
     * @param credentialKey 验证码key（header中传入）
     * @return ResultVO
     */
    @PostMapping(CommonConstants.PUB_PREFIX+"/"+ApiConstants.USER+"/register")
    public ResultVO register(@RequestParam(name = "account",required = true) String account,
                             @RequestParam(name = "password",required = true)String password,
                             @RequestParam("credential_value") String credentialValue,
                             @RequestParam("credential_key") String credentialKey){
        return userService.register(account,password,credentialKey,credentialValue);
    }

    /**
     * 获取用户信息接口
     * @param token 用户消息摘要
     * @return ResultVO
     */
    @GetMapping(CommonConstants.NONPUBLIC_PREFIX+"/"+ApiConstants.USER+"/info")
    public ResultVO getInfo(@RequestHeader(HttpParamKeyConstants.CLIENT_DIGEST) String token){
        return userService.getInfo(token);
    }

    /**
     * 用户注销接口
     * @param token 用户消息摘要
     * @return ResultVO
     */
    @GetMapping(CommonConstants.NONPUBLIC_PREFIX+"/"+ApiConstants.USER+"/logout")
    public ResultVO logout(@RequestHeader(HttpParamKeyConstants.CLIENT_DIGEST) String token){
        return userService.logout(token);
    }

    /**
     * 增加新用户（权限）
     * @param userPO 实体
     * @return ResultVO
     */
    @RequiresPermissions(ResourceConstants.USER + PermissionActionConstant.ADD)
    @PostMapping(CommonConstants.NONPUBLIC_PREFIX + "/" +ApiConstants.USER + "/add")
    public ResultVO addUser(@RequestBody UserPO userPO){
        userPO.setId(UUID.randomUUID().toString().replace("-",""));
        return userService.addUser(userPO);
    }

    /**
     * 删除用户（权限）
     * @param id 用户id
     * @return ResultVO
     */
    @RequiresPermissions(ResourceConstants.USER + PermissionActionConstant.DELETE)
    @DeleteMapping(CommonConstants.NONPUBLIC_PREFIX + "/" +ApiConstants.USER +"/delete")
    public ResultVO deleteUser(@RequestParam("id") String id,
                               @RequestHeader(HttpParamKeyConstants.CLIENT_DIGEST) String token){
        return userService.deleteUserById(id,token);
    }

    /**
     * 批量删除用户（权限）
     * @param idList 用户id列表
     * @return ResultVO
     */
    @RequiresPermissions(ResourceConstants.USER + PermissionActionConstant.DELETE)
    @DeleteMapping(CommonConstants.NONPUBLIC_PREFIX+"/" +ApiConstants.USER + "/batch_delete")
    public ResultVO batchDeleteUser(@RequestParam("id_list[]") ArrayList<String> idList){
        return userService.batchDeleteUserById(idList);
    }

    /**
     * 更新用户信息
     * @param user 用户实体
     * @return ResultVO
     */
    @PutMapping(CommonConstants.NONPUBLIC_PREFIX + "/" +ApiConstants.USER + "/update/{user_id}")
    public ResultVO updateUser(@RequestBody User user,
                               @PathVariable("user_id") String userId){
        user.setId(userId);
        return userService.updateUser(user);
    }

    /**
     * 更新用户信息（权限）
     * @param user 用户实体（不包含密码）
     * @return ResultVO
     */
    @RequiresPermissions(ResourceConstants.USER + PermissionActionConstant.UPDATE)
    @PutMapping(CommonConstants.NONPUBLIC_PREFIX + "/" +ApiConstants.USER + "/update")
    public ResultVO updateUserByPermission(@RequestBody User user,
                                           @RequestHeader(HttpParamKeyConstants.CLIENT_DIGEST) String token){
        return userService.updateUserByPermission(user,new TokenUtil().getUserKeyByToken(token));
    }

    /**
     * 更改密码（权限）
     * @param id 用户id
     * @param password 新密码
     * @return ResultVO
     */
    @RequiresPermissions(ResourceConstants.USER + PermissionActionConstant.UPDATE)
    @PutMapping(CommonConstants.NONPUBLIC_PREFIX + "/" +ApiConstants.USER +"/permission_update_password")
    public ResultVO updatePasswordByPermission(@RequestParam("id") String id,
                                               @RequestParam("password") String password,
                                               @RequestHeader(HttpParamKeyConstants.CLIENT_DIGEST) String token){
        return userService.updatePasswordByPermission(id, MD5Util.GetMD5Code(password),new TokenUtil().getUserKeyByToken(token));
    }

    /**
     * 更改密码
     * @param token 用户消息摘要
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return ResultVO
     */
    @PutMapping(CommonConstants.NONPUBLIC_PREFIX + "/" +ApiConstants.USER +"/update_password")
    public ResultVO updatePassword(@RequestHeader(HttpParamKeyConstants.CLIENT_DIGEST) String token,
                                   @RequestParam("old_password") String oldPassword,
                                   @RequestParam("new_password") String newPassword){
        return userService.updatePassword(new TokenUtil().getUserKeyByToken(token),MD5Util.GetMD5Code(oldPassword),MD5Util.GetMD5Code(newPassword));
    }

    /**
     * 获取用户权限列表（如果缓存中有则从缓存取，如果没有从数据库中取）
     * @param token 消息摘要
     * @return ResultVO
     */
    @GetMapping(CommonConstants.NONPUBLIC_PREFIX + "/" + ApiConstants.USER + "/permission")
    public ResultVO getPermissionList(@RequestHeader(HttpParamKeyConstants.CLIENT_DIGEST)String token){
        return userService.getPermissionList(new TokenUtil().getUserKeyByToken(token));
    }

    /**
     * 分页获取用户列表（权限）
     * @param page 当前页码
     * @param pageSize 页面大小
     * @return ResultVO
     */
    @RequiresPermissions(ResourceConstants.USER + PermissionActionConstant.SELECT)
    @GetMapping(CommonConstants.NONPUBLIC_PREFIX + "/" + ApiConstants.USER + "/get_list")
    public ResultVO getUserList(@RequestParam("page") int page,
                                @RequestParam("page_size") int pageSize){
        return userService.getUserList(page,pageSize);
    }
}
