$(function () {
    init();
})

/**
 * 初始化函数
 */
function init() {
    // 验证加载用户信息是否成功，成功才进行下一步操作
    if(loadUserInfo()){
        getUserPermissionList();
        renderPage();   //根据权限渲染页面
    }
}