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
        renderUserInfo();
    }
}

/**
 * 渲染用户信息
 */
function renderUserInfo() {
    $("input[name='account']").val(userInfo.account);
    $("input[name='realName']").val(userInfo.realName);
    switch (userInfo.sex) {
        case 0:
            $("#boyRadio").attr("checked",true);
            break;
        case 1:
            $("#girlRadio").attr("checked",true);
            break;
    }
    $("input[name='sicauId']").val(userInfo.sicauId);
    $("input[name='telephone']").val(userInfo.telephone);
    $("input[name='email']").val(userInfo.email);
    $("input[name='website']").val(userInfo.website);
}

/**
 * 编辑个人信息
 */
function editUserInfo() {
    var formData = $("#userInfoForm").serializeJson();
    for (var temp in formData) {
        if (formData[temp].trim() == "") {
            delete formData[temp];
        }
    }
    formData.sex = parseInt(formData.sex);
    if (formData.telephone != null) {
        formData.telephone = parseInt(formData.telephone);
    }
    $.ajax({
        url:HOST+COMMON.NONPUBLIC_PREFIX+API.USER+"/update/"+userInfo.id,
        type:"put",
        headers:{
            token : $.cookie(USER_DIGEST_COOKIE),
            "Content-Type":"application/json"
        },
        data:JSON.stringify(formData),
        dataType:"json",
        async:false,
        success:function (res) {
            console.log(res);
            initInfoButton();
            switch (res.status) {
                case -1:
                    systemAlert("red","系统出错！获取用户信息失败");
                    break;
                case 100015:
                    systemAlert("red","您的操作太快，请稍后再试哦~");
                    break;
                case 100016:
                    systemAlert("red","由于你的请求过快，被视为恶意请求，系统已禁用你的请求，2小时后失效。<br>为营造良好上网氛围，请不要进行恶意操作！");
                    break;
                case 100017:
                    systemAlert("red","你的ip正处于禁用期间，暂无法使用该系统，请解禁后再试（禁用时间2小时）<br>为营造良好上网氛围，请不要进行恶意操作！");
                    break;
                case 0:
                    systemAlert("green","修改成功",function () {
                        location.reload();
                    });
                    break;
                default:
                    systemAlert("red",res.msg);
            }
        },
        error:function (res) {
            systemAlert("red","更改信息失败！状态码："+res.status);
        }
    })
}

/**
 * 修改用户密码
 */
function changePassword() {
    var oldPassword = $("input[name='old_password']").val();
    var newPassword = $("input[name='new_password']").val();
    var newPasswordR = $("input[name='new_password_r']").val();
    if (newPassword != newPasswordR) {
        systemAlert("red","两次新密码不一致！");
    }else {
        $.ajax({
            url:HOST+COMMON.NONPUBLIC_PREFIX+API.USER+"/update_password",
            type:"put",
            headers:{
                token : $.cookie(USER_DIGEST_COOKIE)
            },
            data:{
                old_password:oldPassword,
                new_password:newPassword
            },
            dataType:"json",
            async:false,
            success:function (res) {
                console.log(res);
                initInfoButton();
                switch (res.status) {
                    case -1:
                        systemAlert("red","系统出错！获取用户信息失败");
                        break;
                    case 10004:
                        systemAlert("red","旧密码错误");
                        break;
                    case 100015:
                        systemAlert("red","您的操作太快，请稍后再试哦~");
                        break;
                    case 100016:
                        systemAlert("red","由于你的请求过快，被视为恶意请求，系统已禁用你的请求，2小时后失效。<br>为营造良好上网氛围，请不要进行恶意操作！");
                        break;
                    case 100017:
                        systemAlert("red","你的ip正处于禁用期间，暂无法使用该系统，请解禁后再试（禁用时间2小时）<br>为营造良好上网氛围，请不要进行恶意操作！");
                        break;
                    case 0:
                        autoCloseAlert("green","修改密码成功，你现在需要重新登录","3000",function () {
                            $.removeCookie(USER_DIGEST_COOKIE, { path: '/' });
                            goLogin();
                        });
                        break;
                    default:
                        systemAlert("red",res.msg);
                }
            },
            error:function (res) {
                systemAlert("red","更改密码失败！状态码："+res.status);
            }
        })
    }
}