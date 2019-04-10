package demo.sicau.datamanagementplatform.controller;

import demo.sicau.datamanagementplatform.constants.*;
import demo.sicau.datamanagementplatform.entity.POJO.VO.ResultVO;
import demo.sicau.datamanagementplatform.enums.ResultEnum;
import demo.sicau.datamanagementplatform.service.PermissionService;
import demo.sicau.datamanagementplatform.util.TokenUtil;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 13:50 2018/11/15
 * @Description: 权限控制器
 */
@RestController
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    /**
     * 获取资源接口
     * @param token 用户摘要
     * @return ResultVO
     */
    @RequiresPermissions(ResourceConstants.PERMISSION + PermissionActionConstant.SELECT)
    @GetMapping(CommonConstants.NONPUBLIC_PREFIX + "/" + ApiConstants.PERMISSION + "/list_resource")
    public ResultVO getResourceList(){
        return permissionService.getResourceList();
    }

    /**
     * 获取操作接口
     * @return ResultVO
     */
    @RequiresPermissions(ResourceConstants.PERMISSION + PermissionActionConstant.SELECT)
    @GetMapping(CommonConstants.NONPUBLIC_PREFIX + "/" + ApiConstants.PERMISSION + "/list_action")
    public ResultVO getActionList(){
        return permissionService.getActionList();
    }

    /**
     * 获取角色权限列表
     * @return ResultVO
     */
    @RequiresPermissions(ResourceConstants.PERMISSION + PermissionActionConstant.SELECT)
    @GetMapping(CommonConstants.NONPUBLIC_PREFIX + "/" + ApiConstants.PERMISSION + "/list_role_permission")
    public ResultVO getRoleAndPermissionList(){
        return permissionService.getRoleAndPermissionList();
    }

    /**
     * 获取用户和角色列表
     * @return ResultVO
     */
    @RequiresPermissions(ResourceConstants.PERMISSION + PermissionActionConstant.SELECT)
    @GetMapping(CommonConstants.NONPUBLIC_PREFIX+"/"+ApiConstants.PERMISSION+"/list_uer_role")
    public ResultVO getUserAndRoleList(){
        return permissionService.getUserAndRoleList();
    }

    /**
     * 通过关系id删除角色关系
     * @param id 关系id
     * @return ResultVO
     */
    @RequiresPermissions(ResourceConstants.PERMISSION + PermissionActionConstant.DELETE)
    @DeleteMapping(CommonConstants.NONPUBLIC_PREFIX + "/" + ApiConstants.PERMISSION + "/del_user_role")
    public ResultVO delUserAndRole(@RequestParam("uid") String uid,
                                   @RequestHeader(HttpParamKeyConstants.CLIENT_DIGEST) String token){
        return permissionService.delUserAndRole(uid,token);
    }

    /**
     * 查询所有权限
     * @return ResultVO
     */
    @RequiresPermissions(ResourceConstants.PERMISSION + PermissionActionConstant.SELECT)
    @GetMapping(CommonConstants.NONPUBLIC_PREFIX + "/" + ApiConstants.PERMISSION + "/list_all_permission")
    public ResultVO getAllPermission(){
        return permissionService.getAllPermission();
    }

    /**
     * 新增角色，并分配相应权限
     * @param roleName  角色名
     * @param permissions 权限
     * @return ResultVO
     */
    @RequiresPermissions(ResourceConstants.PERMISSION + PermissionActionConstant.ADD)
    @PostMapping(CommonConstants.NONPUBLIC_PREFIX + "/" +ApiConstants.PERMISSION + "/add_role")
    public ResultVO addRole(@RequestParam("role_name") String roleName,
                            @RequestParam("permissions[]") ArrayList<String> permissions){
        return permissionService.addRole(roleName,permissions);
    }

    /**
     * 查询所有角色
     * @return  ResultVO
     */
    @RequiresPermissions(ResourceConstants.PERMISSION + PermissionActionConstant.SELECT)
    @GetMapping(CommonConstants.NONPUBLIC_PREFIX + "/" + ApiConstants.PERMISSION + "/list_role")
    public ResultVO getAllRole(){
        return permissionService.getAllRole();
    }

    /**
     * 添加用户角色
     * @param role  角色id
     * @param userIds   用户id
     * @return ResultVO
     */
    @RequiresPermissions({
            ResourceConstants.PERMISSION + PermissionActionConstant.ADD,
            ResourceConstants.PERMISSION + PermissionActionConstant.UPDATE
    })
    @PostMapping(CommonConstants.NONPUBLIC_PREFIX + "/" + ApiConstants.PERMISSION + "/add_user_role")
    public ResultVO addUserRole(@RequestParam("role") String role,
                                @RequestParam("userId[]") ArrayList<String> userIds,
                                @RequestHeader(HttpParamKeyConstants.CLIENT_DIGEST) String token){
        return permissionService.addUserRole(role,userIds,new TokenUtil().getUserKeyByToken(token));
    }

    /**
     * 更新用户角色
     * @param roleId    角色id
     * @param userId    用户id
     * @return  ResultVO
     */
    @RequiresPermissions(ResourceConstants.PERMISSION + PermissionActionConstant.UPDATE)
    @PostMapping(CommonConstants.NONPUBLIC_PREFIX + "/" + ApiConstants.PERMISSION + "/update_user_role")
    public ResultVO updateUserRole(@RequestParam("roleId") String roleId,
                                @RequestParam("userId") String userId,
                                   @RequestHeader(HttpParamKeyConstants.CLIENT_DIGEST) String token){
        return permissionService.updateUserRole(roleId,userId,new TokenUtil().getUserKeyByToken(token));
    }
}
