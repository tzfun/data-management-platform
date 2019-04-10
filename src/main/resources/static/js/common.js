var HOST = "/";
var COMMON = {
    API_VERSION : "v1", //api版本号
    NONPUBLIC_PREFIX : "v1/nonpub", //非公共api的前缀
    PUB_PREFIX : "v1/pub" //公共api前缀
}
var HTTP_PARAM_KEY = {
    CLIENT_DIGEST : "token",    //  摘要
    CLIENT_ID : "client_id",    //  用户id
    CURRENT_TIME : "current_time"   //  当前时间
}
var RESOURCE = {
    USER : "user"
}
var API = {
    USER : "/user",
    CREDENTIAL : "/credential",
    PERMISSION : "/permission",
    FILE:"/file",
    ARTICLE:"/article",
    RESOURCE:"/resource",
    NOTICE:"/notice",
    SEARCH:"/search"
}
var USER_DIGEST_COOKIE = "user_digest_cookie";  // cookie名
var SAVE_COLOR=["#a6cb12","#364e68","#94d2e6","#6b76ff","#5c8d89","#c5d86d","#fbeed7","#8293ff"] // web安全色
var userPermissionList;
var noticeParam = { // notice参数
    long_term_num:3,
    short_term_num:5
}
var userInfo;   //用户信息

/**
 * 渲染图片浏览器
 */
function renderViewer(){
    $('#articleContent').viewer({
        url: 'src'
    });
}
/**
 * 序列化表单，将其转化为json格式
 */
$.fn.serializeJson = function() {
    var arr = this.serializeArray();
    var json = {};
    arr.forEach(function(item) {
        var name = item.name;
        var value = item.value;
        if (!json[name]) {
            json[name] = value;
        } else if ($.isArray(json[name])) {
            json[name].push(value);
        } else {
            json[name] = [json[name], value];
        }
    });
    return json;
}

/**
 * 渲染用户信息按钮
 * @param data
 */
function renderInfoButton(data) {
    $("#headerInfoTitle").text(data.account);
    $("#userInfoContainer").html("<button class=\"dropdown-item\" type=\"button\" onclick=\"location.href='/p/homepage.html'\">Homepage</button>\n" +
        "                <button class=\"dropdown-item\" type=\"button\" onclick=\"logout()\">Log Out</button>");
}

/**
 * 初始化用户信息按钮
 */
function initInfoButton(){
    $("#headerInfoTitle").text("Not Login");
    $("#userInfoContainer").html("<button class=\"dropdown-item\" type=\"button\" onclick=\"location.href='/p/homepage.html'\">Homepage</button>\n" +
        "                <button class=\"dropdown-item\" type=\"button\" onclick=\"goLogin()\">Log In</button>");
}

/**
 * 前往登录，并添加回调页面
 */
function goLogin() {
    location.href = "login.html?client_url="+urlEncode(location.href);
}
/**
 * 加载用户信息
 */
function loadUserInfo(){
    var flag = false;
    var digestCookie = $.cookie(USER_DIGEST_COOKIE);
    if(digestCookie != null){
        $.ajax({
            url:HOST+COMMON.NONPUBLIC_PREFIX+API.USER+"/info",
            type:"get",
            headers:{
                token : digestCookie
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
                        userInfo = res.data;
                        renderInfoButton(res.data);
                        flag = true;
                        break;
                    default:
                        systemAlert("red",res.msg);
                }
            },
            error:function (res) {
                console.log("获取用户信息失败！状态码："+res.status)
                initInfoButton();
                // systemAlert("red","获取用户信息失败！状态码："+res.status);
            }
        })
    }else{
        initInfoButton();
        flag = true;
    }
    return flag;
}
/**
 * 获取用户权限列表
 */
function getUserPermissionList() {
    var token = $.cookie(USER_DIGEST_COOKIE);
    if(token!=null){
        $.ajax({
            url:HOST + COMMON.NONPUBLIC_PREFIX + API.USER + "/permission",
            type:"get",
            headers:{
                token:token
            },
            async:false,
            dataType:"json",
            success:function (res) {
                console.log(res);
                switch (res.status) {
                    case -1:
                        systemAlert("red","系统出错！");
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
                        userPermissionList = res.data;
                        break;
                }
            },
            error:function (res) {
                console.log(res);
                switch (res.status) {
                    case 401:
                        $.removeCookie(USER_DIGEST_COOKIE, { path: '/' }); // 删除Cookie
                        location.href= "../login.html?client_url="+urlEncode(location.href);
                        break;

                }
            }
        })
    }else{
        location.href= "../login.html?client_url="+urlEncode(location.href);
    }
}
/**
 * 根据权限渲染页面
 */
function renderPage() {
    var permissionRetrieval = {
        USER:false,
        PERMISSION:false,
        RESOURCE:false,
        ARTICLE:false,
        MESSAGE:false,
        NOTICE:false,
        SYSTEM:false
    }
    for (var i =0;i<userPermissionList.length;i++){
        switch (userPermissionList[i].resource) {
            case "resource":
                permissionRetrieval.RESOURCE = true;
                break;
            case "article":
                permissionRetrieval.ARTICLE = true;
                break;
            case "user":
                permissionRetrieval.USER = true;
                break;
            case "permission":
                permissionRetrieval.PERMISSION = true;
                break;
            case "message":
                permissionRetrieval.MESSAGE = true;
                break;
            case "notice":
                permissionRetrieval.NOTICE = true;
                break;
            case "system":
                permissionRetrieval.SYSTEM = true;
                break;
        }
    }
    for (var temp in permissionRetrieval) {
        switch (temp) {
            case "USER":
                if (permissionRetrieval[temp]) {
                    $("#permissionButtonList").append("<li class=\"list-group-item\"><a href=\"user.html\">Users Management</a></li>");
                }
                break;
            case "RESOURCE":
                if (permissionRetrieval[temp]) {
                    $("#permissionButtonList").append("<li class=\"list-group-item\"><a href=\"resource.html\">Resource Management</a></li>");
                }
                break;
            case "ARTICLE":
                if (permissionRetrieval[temp]) {
                    $("#permissionButtonList").append("<li class=\"list-group-item\"><a href=\"article.html\">Article Management</a></li>");
                }
                break;
            case "PERMISSION":
                if (permissionRetrieval[temp]) {
                    $("#permissionButtonList").append("<li class=\"list-group-item\"><a href=\"permission.html\">Permission Management</a></li>");
                }
                break;
            case "MESSAGE":
                if (permissionRetrieval[temp]) {
                    $("#permissionButtonList").append("<li class=\"list-group-item\"><a href=\"message.html\">Messages Management</a></li>");
                }
                break;
            case "NOTICE":
                if (permissionRetrieval[temp]) {
                    $("#permissionButtonList").append("<li class=\"list-group-item\"><a href=\"notice.html\">Notice Management</a></li>");
                }
                break;
            case "SYSTEM":
                if (permissionRetrieval[temp]) {
                    $("#permissionButtonList").append("<li class=\"list-group-item\"><a href=\"system.html\">System Settings</a></li>");
                }
                break;
        }
    }
}
/**
 * 退出登录
 */
function logout() {
    var digestCookie = $.cookie(USER_DIGEST_COOKIE)
    if(digestCookie != null){
        systemConfirm("确定退出登录？",function () {
            $.ajax({
                url:HOST+COMMON.NONPUBLIC_PREFIX+API.USER+"/logout",
                type:"get",
                headers:{
                    token : digestCookie
                },
                dataType:"json",
                success:function (res) {
                    console.log(res);
                    switch (res.status) {
                        case -1:
                            systemAlert("red","系统出错！");
                            break;
                        case 10004:
                            systemAlert("red","用户名或密码错误！");
                            break;
                        case 10006:
                            systemAlert("red","操作无权限！");
                            break;
                        case 100013:
                            systemAlert("red","验证码错误！");
                            break;
                        case 100014:
                            systemAlert("red","验证码已失效，请点击图片重新获取！");
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
                            location.reload();
                            break;
                    }
                },
                error:function (res) {
                    console.log(res.status);
                }
            })
        })
    }
    $.removeCookie(USER_DIGEST_COOKIE, { path: '/' }); // 删除Cookie
}

/**
 * 渲染左侧公告栏
 */
function getNoticeList() {
    $.ajax({
        url:HOST+COMMON.PUB_PREFIX + API.NOTICE+"/list_by_type",
        type:"get",
        data:{
            long_term_num:noticeParam.long_term_num,
            short_term_num:noticeParam.short_term_num
        },
        dataType:"json",
        success:function (res) {
            console.log(res);
            switch (res.status) {
                case -1:
                    systemAlert("red","系统出错！");
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
                    renderLeftNotice(res.data);
                    break;
            }
        },
        error:function (res) {
            console.log(res.status);
        }
    })
}

/**
 * 渲染左侧公告栏
 */
function renderLeftNotice(data) {
    $("#shortTermNoticeContainer").empty();
    $("#longTermNoticeContainer").empty();
    if (data.longTerm.length == 0) {
        $("#longTermNoticeContainer").append("<p style='text-align: center'><small>Nothing here.</small></p>")
    }
    for (var i = 0; i < data.longTerm.length; i++) {
        $("#longTermNoticeContainer").append("<blockquote class=\"blockquote mb-0\">\n" +
            "                                    <p><a href='/notice-view.html?id="+data.longTerm[i].id+"'>"+data.longTerm[i].title+"</a></p>\n" +
            "                                    <footer class=\"blockquote-footer\"><cite title=\"Source Title\">"+data.longTerm[i].createTime+"</cite></footer>\n" +
            "                                </blockquote>")
    }

    for (var i = 0; i < data.shortTerm.length; i++){
        $("#shortTermNoticeContainer").append("<a href=\"/notice-view.html?id="+data.shortTerm[i].id+"\" class=\"list-group-item list-group-item-action flex-column align-items-start\">\n" +
            "                            <div class=\"d-flex w-100 justify-content-between\">\n" +
            "                                <h5 class=\"mb-1\">"+data.shortTerm[i].title+"</h5>\n" +
            // "                                <small>3 days ago</small>\n" +
            "                            </div>\n" +
            "                            <p class=\"mb-1\">"+data.shortTerm[i].summary+"</p>\n" +
            "                            <small>"+data.shortTerm[i].createTime+"</small>\n" +
            "                        </a>");
    }
}
/**
 * 自动关闭式提示
 * */
function autoCloseAlert(color,msg, time,callback) {
    $.alert({
        title: '系统提示',
        content: msg,
        icon: 'fa fa-comment',
        type: color,
        autoClose: '好的|'+time,
        escapeKey: '好的',
        buttons: {
            "好的": {
                btnClass: 'btn-success',
                action: function() {
                    if(callback != null){
                        return callback();
                    }
                }
            }
        }
    });
}

/**
 * 系统提示框
 * */
function systemAlert(color,msg,callback){
    $.alert({
        title: '系统提示',
        content: msg,
        icon: 'fa fa-comment',
        type: color,
        buttons: {
            "我知道了": function (){
                if(callback != null){
                    return callback();
                }
            }
        }
    });
}

/**
 * 确认提示框
 * */
function systemConfirm(msg,yesCallback,noCallback){
    $.alert({
        title: '系统提示',
        content: msg,
        icon: 'fa fa-question-circle-o',
        type: 'green',
        buttons: {
            "取消":function () {
                if(noCallback != null){
                    return noCallback();
                }
            },
            "确定": {
                btnClass: 'btn-success',
                action:function() {
                    if (yesCallback != null) {
                        return yesCallback();
                    }
                }
            }

        }
    });
}
/**
 * 获取地址参数
 */
function getUrlData() {
    var dataList = location.search.split("&");
    var data = {};
    for (var i = 0; i < dataList.length; i++) {
        var tempIndex = dataList[i].indexOf("=");
        data[dataList[i].substring(1,tempIndex)] = dataList[i].substring(tempIndex + 1 );
    }
    return data;
}

/**
 * 获取地址栏参数//可以是中文参数
 * @param key
 * @returns {any}
 */
function getUrlParam(key) {
    // 获取参数
    var url = window.location.search;
    // 正则筛选地址栏
    var reg = new RegExp("(^|&)" + key + "=([^&]*)(&|$)");
    // 匹配目标参数
    var result = url.substr(1).match(reg);
    //返回参数值
    return result ? decodeURIComponent(result[2]) : null;
}

/**
 * 自定义url编码
 * 主要对 & 符号进行编码，便于登录跳转
 * 特殊：& 编码成 %bf
 * @param url
 */
function urlEncode(url){
    return url.replace(/&/g,"%bf");
}

/**
 * 自定义url解码
 * @param url
 * @returns {string}
 */
function urlDecode(url) {
    return url.replace(/%bf/g,"&");
}

/**
 * 字符串符号转义，转义规则如下
 * <   &lt;
 * >   &gt;
 * '   &apos;
 * "   &quot;
 * @param str
 * @returns {*}
 */
function strFilter(str) {
    str = str.replace(/</g,"&lt;");
    str = str.replace(/>/g,"&gt;");
    str = str.replace(/\'/g,"&apos;");
    str = str.replace(/\"/g,"&quot;");
    return str;
}

/**
 * 标签解码
 * @param str
 * @returns {string}
 */
function labelDecode(str){
    str = str.replace(/&lt;/g,"<");
    str = str.replace(/&gt;/g,">");
    str = str.replace(/&apos;/g,"'");
    str = str.replace(/&quot;/g,"\"");
    return str;
}