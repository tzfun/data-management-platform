package demo.sicau.datamanagementplatform.dao;

import demo.sicau.datamanagementplatform.entity.DTO.User;
import demo.sicau.datamanagementplatform.entity.POJO.PO.UserPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PostMapping;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 19:23 2018/11/7
 * @Description:
 */
@Mapper
public interface UserDao {
    /**
     * 查询所有用户信息
     * @return UserPO
     */
    ArrayList<UserPO> selectAllUser();

    /**
     * 通过账户和密码查询用户信息
     * @param account 账户
     * @param password 密码
     * @return UserPO
     */
    UserPO selectUserByAccountAndPassword(@Param("account") String account, @Param("password") String password);

    /**
     * 通过账户查询用户信息
     * @param account 账户
     * @return UserPO
     */
    UserPO selectUserByAccount(@Param("account") String account);

    /**
     * 通过用户名和密码新增用户信息
     * @param id 用户id
     * @param account 账户
     * @param password 密码
     * @return boolean
     */
    boolean insertUserByAccountAndPassword(@Param("id") String id,@Param("account") String account, @Param("password") String password);

    /**
     * 插入用户
     * @param userPO 用户对象
     * @return boolean
     */
    boolean insertUser(@Param("userPO") UserPO userPO);

    /**
     * 通过id查询用户
     * @param id 用户id
     * @return UserPO
     */
    UserPO selectUserById(@Param("id") String id);

    /**
     * 通过id删除用户
     * @param id 用户id
     * @return boolean
     */
    boolean deleteUserById(@Param("id") String id);

    /**
     * 更新用户数据
     * @param user 用户对象
     * @return boolean
     */
    boolean updateUser(@Param("user") User user);

    /**
     * 更新面
     * @param id 用户id
     * @param password 新密码
     * @return boolean
     */
    boolean updatePassword(@Param("id") String id,@Param("password") String password);

    /**
     * 统计用户数量
     * @return int
     */
    int countUser();

    /**
     * 分页查询用户
     * @param page 当前索引
     * @param pageSize 查询数量
     * @return UserPO
     */
    List<UserPO> selectUserByPagination(@Param("page") int page,@Param("pageSize") int pageSize);

    /**
     * 通过id批量删除用户
     * @param idList 用户id列表
     * @return boolean
     */
    boolean batchDeleteUserById(ArrayList<String> idList);
}
