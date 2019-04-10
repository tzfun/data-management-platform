package demo.sicau.datamanagementplatform.service;

import demo.sicau.datamanagementplatform.entity.DTO.Permission;
import demo.sicau.datamanagementplatform.entity.DTO.User;
import demo.sicau.datamanagementplatform.entity.POJO.PO.UserPO;
import demo.sicau.datamanagementplatform.entity.POJO.VO.ResultVO;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 19:22 2018/11/7
 * @Description: 用户业务逻辑层接口类
 */
public interface UserService {

    /**
     * 用户登录
     * @param account 账号
     * @param password 密码
     * @param credentialKey 验证码key
     * @param credentialValue 验证码值
     * @return ResultVO
     */
    ResultVO login(String account, String password, String credentialKey, String credentialValue);

    /**
     * 用过用户id获取其权限列表
     * @param userId 用户id
     * @return List<Permission>
     */
    List<Permission> listPermissionByUserId(String userId);

    /**
     * 用户注册
     * @param account 账号
     * @param password 密码
     * @param credentialKey 验证码key
     * @param credentialValue 验证码值
     * @return ResultVO
     */
    ResultVO register(String account, String password, String credentialKey, String credentialValue);

    /**
     * 用户注销
     * @param token 用户消息摘要
     * @return ResultVO
     */
    ResultVO logout(String token);

    /**
     * 获取用户信息（缓存）
     * @param token 用户消息摘要
     * @return ResultVO
     */
    ResultVO getInfo(String token);

    /**
     * 增加新用户
     * @param userPO 实体
     * @return ResultVO
     */
    ResultVO addUser(UserPO userPO);

    /**
     * 删除用户（权限）
     * @param id 用户id
     * @return ResultVO
     */
    ResultVO deleteUserById(String id,String token);

    /**
     * 更新用户信息
     * @param userPO 用户实体（不包含密码）
     * @return ResultVO
     */
    ResultVO updateUser(User user);

    /**
     * 更新用户信息
     * @param user 用户实体（不包含密码）
     * @return ResultVO
     */
    ResultVO updateUserByPermission(User user,String userId);

    /**
     * 更改密码
     * @param id 用户id
     * @param password 新密码
     * @return ResultVO
     */
    ResultVO updatePasswordByPermission(String userId, String password,String actionUserId);

    /**
     * 更改密码
     * @param token 用户消息摘要
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return ResultVO
     */
    ResultVO updatePassword(String userId, String oldPassword, String newPassword);

    /**
     * 获取用户所有权限（如果缓存中有则从缓存取，如果没有从数据库中取）
     * @param userId 用户id
     * @return ResultVO
     */
    ResultVO getPermissionList(String userId);

    /**
     * 分页获取用户列表（权限）
     * @param page 当前页码
     * @param pageSize 页面大小
     * @return ResultVO
     */
    ResultVO getUserList(int page, int pageSize);

    /**
     * 批量删除用户
     * @param idList 用户id列表
     * @return ResultVO
     */
    ResultVO batchDeleteUserById(ArrayList<String> idList);
}
