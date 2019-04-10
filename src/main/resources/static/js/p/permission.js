// 权限图基本结构
var permissionMap = {
    name:"资源管理平台",
    parent:"null",
    children:[]
};
var tempUserId; //当前操作用户id
var renderUserRoleTable = false;    // 是否渲染用户角色选择模态框的表格
var hasRenderPermissionSelector = false;    //是否渲染权限选择器
var systemRole=[]; // 系统的所有角色
var myChart;
$(function () {
    myChart = echarts.init(document.getElementById('tree'));
    // 验证加载用户信息是否成功，成功才进行下一步操作
    if(loadUserInfo()){
        // 渲染当前用户权限列表
        getUserPermissionList();
        renderPage();
        getRolePermission();
        // 初始化Table
        var oTable = new TableInit();
        oTable.Init();
    }

})

/**
 * 获取所有角色所对应的权限
 */
function getRolePermission(){
    var token = $.cookie(USER_DIGEST_COOKIE);
    if(token != null){
        $.ajax({
            url:HOST + COMMON.NONPUBLIC_PREFIX + API.PERMISSION + "/list_role_permission",
            type:"get",
            headers:{
                token:token
            },
            dataType:"json",
            async:false,
            beforeSend:function(){
                myChart.showLoading();
            },
            success:function (res) {
                console.log(res);
                switch (res.status) {
                    case -1:
                        systemAlert("red","获取权限列表错误，"+res.msg);
                        break;
                    case 0:
                        RenderPermissionTree(res.data);
                        break;
                    case 10006:
                        systemAlert("red","您暂无查询权限信息的权限！");
                        break;
                }
            },
            error:function (res) {
                systemAlert("red","请求错误，code："+res.status);
            }
        })
    }
}

/**
 * 表格初始化对象
 * @returns {Object}
 * @constructor
 */
var TableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#userTable').bootstrapTable({
            url: HOST + COMMON.NONPUBLIC_PREFIX + API.PERMISSION + "/list_uer_role",         //请求后台的URL（*）
            ajaxOptions:{
                headers:{
                    token:$.cookie(USER_DIGEST_COOKIE)
                }
            },
            method: 'get',                      //请求方式（*）
            undefinedText: "空",//当数据为 undefined 时显示的字符
            toolbar: '#toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: false,                   //是否显示分页（*）
            sortable: false,                     //是否启用排序
            sortOrder: "asc",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       //初始化加载第一页，默认第一页
            pageSize: 10,                       //每页的记录行数（*）
            pageList: [5,10, 25, 50, 100],        //可供选择的每页的行数（*）
            paginationPreText: '‹',//指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',//指定分页条中下一页按钮的图标或文字,这里是>
            search: true,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: true,                  //是否显示所有的列
            showRefresh: true,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: false,                //是否启用点击选中行
            height: 500,                        //行高
            uniqueId: "id",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            contentType: 'application/json',
            dataType: 'json',
            refresh:true,
            responseHandler: function (res) {
                console.log(res);
                switch (res.status) {
                    case -1:
                        systemAlert("red","获取列表错误，"+res.msg);
                        break;
                    case 0:
                        return res.data;
                    case 10006:
                        systemAlert("red","您暂无查询用户权限的权限！");
                        break;
                }
            },
            columns: [ {
                field: 'role',
                title: 'role',
                formatter:function (value) {
                    return "<button type=\"button\" class=\"btn btn-success btn-sm\">"+value.roleName+"</button>";
                }
            },{
                field: 'user',
                title: 'account',
                formatter:function (value) {
                    return value.account
                }
            }, {
                field: 'user',
                title: 'Real Name',
                formatter:function (value) {
                    return value.realName;
                }
            }, {
                field: 'user',
                title: 'Sex',
                formatter:function (value) {
                    if (value.sex == 0) {
                        return "男";
                    }else{
                        return "女"
                    }
                }
            },{
                field: 'user',
                title: 'Sicau Number',
                formatter:function (value) {
                    return value.sicauId;
                }
            }, {
                field: 'user',
                title: 'Email',
                formatter:function (value) {
                    return value.email;
                }
            }, {
                field: 'user',
                title: 'Telephone',
                formatter:function (value) {
                    return value.telephone;
                }
            }, {
                field: 'user',
                title: 'Website',
                formatter:function (value) {
                    return value.website;
                }
            }, {
                field: 'user',
                title: 'User Register Time',
                formatter:function (value, row, index) {
                    return value.createTime;
                }
            },{
                field: 'user',
                title: 'User Update Time',
                formatter:function (value, row, index) {
                    return value.updateTime;
                }
            },{
                field:'user',
                title:'Opera',
                formatter:function (value,row) {
                    return "<button type=\"button\" class=\"btn btn-warning btn-sm\" onclick=\"editUserRole('"+row.user.id+"','"+row.role.id+"')\">编辑</button>&nbsp;" +
                        "<button type=\"button\" class=\"btn btn-danger btn-sm\" onclick=\"delUserRole('"+row.user.id+"')\">删除关系</button>";
                }
            }]
        });
    };

    //得到查询的参数
    oTableInit.queryParams = function (params) {
        var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            page_size: params.limit,   //页面大小
            page: params.offset,  //页码
        };
        return temp;
    };
    return oTableInit;
};

/**
 * 初始化用户角色表格对象
 * @returns {Object}
 * @constructor
 */
var UserRoleTableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#userInfoTable').bootstrapTable({
            url: HOST + COMMON.NONPUBLIC_PREFIX + API.USER + "/get_list",         //请求后台的URL（*）
            ajaxOptions:{
                headers:{
                    token:$.cookie(USER_DIGEST_COOKIE)
                }
            },
            method: 'get',                      //请求方式（*）
            undefinedText: "空",//当数据为 undefined 时显示的字符
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            sortable: false,                     //是否启用排序
            sortOrder: "asc",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       //初始化加载第一页，默认第一页
            pageSize: 10,                       //每页的记录行数（*）
            pageList: [5,10, 25, 50, 100],        //可供选择的每页的行数（*）
            paginationPreText: '‹',//指定分页条中上一页按钮的图标或文字,这里是<
            paginationNextText: '›',//指定分页条中下一页按钮的图标或文字,这里是>
            search: true,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: true,                  //是否显示所有的列
            showRefresh: true,                  //是否显示刷新按钮
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: false,                //是否启用点击选中行
            height: 400,                        //行高
            uniqueId: "id",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            contentType: 'application/json',
            dataType: 'json',
            refresh:true,
            responseHandler: function (res) {
                console.log(res);
                switch (res.status) {
                    case -1:
                        systemAlert("red","获取列表错误，"+res.msg);
                        break;
                    case 0:
                        return {
                            "total": res.data.total,//总页数
                            "rows": res.data.rows   //数据
                        }
                    case 10006:
                        systemAlert("red","您暂无查询用户的权限！");
                        break;
                }
            },
            columns: [{
                checkbox: true
            }, {
                field: 'account',
                title: 'account'
            }, {
                field: 'realName',
                title: 'Real Name'
            }, {
                field: 'sex',
                title: 'Sex',
                formatter:function (value) {
                    if (value == 0) {
                        return "男";
                    }else{
                        return "女"
                    }
                }
            },{
                field:'role',
                title:'Role',
                formatter:function (value) {
                    var roleName = value == null ? "普通用户" : value;
                    var btnStyle = value == null ? "primary" : "success";
                    return "<button type=\"button\" class=\"btn btn-"+btnStyle+" btn-sm\">"+roleName+"</button>";
                }
            },{
                field: 'sicauId',
                title: 'Sicau Number'
            }, {
                field: 'email',
                title: 'Email'
            }, {
                field: 'telephone',
                title: 'Telephone'
            }, {
                field: 'website',
                title: 'Website'
            }]
        });
        renderUserRoleTable = true;
    };

    //得到查询的参数
    oTableInit.queryParams = function (params) {
        var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            page_size: params.limit,   //页面大小
            page: params.offset,  //页码
        };
        return temp;
    };
    return oTableInit;
};

/**
 * 渲染权限树
 * @constructor
 */
function RenderPermissionTree(data) {
    for (var i = 0; i < data.length; i++) {
        var tempPermission = {
            name:"",
            parent:""
        };
        var tempResource = {
            name:"",
            parent:"",
            children:[tempPermission]
        }
        var tempRole = {
            name:"",
            parent:permissionMap.name,
            children:[tempResource]
        };
        var hasPermission = false; // 默认没有权限
        var hasRole = false;    //默认没有角色
        var hasResource =false; //默认没有资源
        // 匹配权限
        tempPermission.name = data[i].permission.action;
        tempPermission.parent = data[i].permission.resource;
        // 匹配资源
        tempResource.name = data[i].permission.resource;
        tempResource.parent = data[i].role.roleName;
        // tempResource.children.push(tempPermission);

        for (var j =0 ; j<permissionMap.children.length;j++) {
            // 如果存在这个角色，直接追加资源
            if (permissionMap.children[j].name == data[i].role.roleName) {
                for (var k = 0; k < permissionMap.children[j].children.length; k++) {
                    // 如果存在这个资源，直接追加权限
                    if (permissionMap.children[j].children[k].name == tempResource.name) {
                        hasResource = true;
                        for (var p = 0; p < permissionMap.children[j].children[k].children.length; p++) {
                            if (permissionMap.children[j].children[k].children[p] == tempPermission.name) {
                                hasPermission = true;
                            }
                        }
                        if(!hasPermission){
                            permissionMap.children[j].children[k].children.push(tempPermission);
                        }
                    }
                }
                hasRole = true;
                if (!hasResource) {
                    permissionMap.children[j].children.push(tempResource);
                }
            }
        }
        if (!hasRole) {
            tempRole.name=data[i].role.roleName;
            permissionMap.children.push(tempRole);
        }
    }
    console.log(permissionMap);
    myChart.hideLoading();
    echarts.util.each(permissionMap.children, function (datum, index) {
        index % 2 === 0 && (datum.collapsed = true);
    });

    myChart.setOption(option = {
        tooltip: {
            trigger: 'item',
            triggerOn: 'mousemove'
        },
        series: [
            {
                type: 'tree',

                data: [permissionMap],

                top: '1%',
                left: '7%',
                bottom: '1%',
                right: '20%',

                symbolSize: 7,

                label: {
                    normal: {
                        position: 'left',
                        verticalAlign: 'middle',
                        align: 'right',
                        fontSize: 9
                    }
                },

                leaves: {
                    label: {
                        normal: {
                            position: 'right',
                            verticalAlign: 'middle',
                            align: 'left'
                        }
                    }
                },

                expandAndCollapse: true,
                animationDuration: 550,
                animationDurationUpdate: 750
            }
        ]
    });
}

/**
 * 新增角色
 * addRole
 */
function addRole(){
    $("#permissionModal").modal("show");
    if (!hasRenderPermissionSelector) {
        selectAllPermission();
    }

}

/**
 * 新增用户角色关系
 */
function addUserRole() {
    var uTable = new UserRoleTableInit();
    if (!renderUserRoleTable) {
        if (systemRole.length == 0) {
            selectAllRole();
        }
        // 渲染选择框
        $("#roleSelector").empty();
        for (var i = 0; i < systemRole.length; i++) {
            $("#roleSelector").append("<option value=\""+systemRole[i].id+"\">"+systemRole[i].roleName+"</option>")
        }
        uTable.Init();
    }
    $("#userRoleModal").modal("show");
}

/**
 * 编辑用户角色关系
 */
function editUserRole(userId,roleId) {
    tempUserId = userId;
    if (systemRole.length == 0) {
        selectAllRole();
    }
    $("#editRoleSelector").empty();
    for (var i = 0; i < systemRole.length; i++) {
        $("#editRoleSelector").append("<option value=\""+systemRole[i].id+"\">"+systemRole[i].roleName+"</option>")
    }
    $("#editRoleSelector").val(roleId);
    $("#editUserRoleModal").modal("show");
}

/**
 * 解除用户角色关系
 */
function delUserRole(userId) {
    var token = $.cookie(USER_DIGEST_COOKIE);
    if(token != null){
        systemConfirm("你确定解除该用户的权限吗？",function () {
            $.ajax({
                url:HOST + COMMON.NONPUBLIC_PREFIX + API.PERMISSION + "/del_user_role",
                type:"delete",
                headers:{
                    token:token
                },
                data:{
                    uid:userId
                },
                dataType:"json",
                async:false,
                success:function (res) {
                    console.log(res);
                    switch (res.status) {
                        case -1:
                            systemAlert("red","系统错误，"+res.msg);
                            break;
                        case 0:
                            systemAlert("green","删除成功！",function () {
                                $('#userTable').bootstrapTable("refresh");
                            });
                            break;
                        case 10002:
                            systemAlert("red","登录失效，请重新登录",function () {
                                goLogin();
                            })
                            break;
                        case 10006:
                            systemAlert("red","您的权限不足！（超级管理员才可解除超级管理员关系）");
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
                        case 100020:
                            systemAlert("red","无法解除自己的关系！");
                            break;
                    }
                },
                error:function (res) {
                    systemAlert("red","请求错误，code："+res.status);
                }
            })
        })
    }else{
        systemAlert("red","登录失效，请重新登录",function () {
            goLogin();
        })
    }
}

/**
 * 查询所有的权限
 */
function selectAllPermission() {
    var token = $.cookie(USER_DIGEST_COOKIE);
    if(token != null){
        $.ajax({
            url:HOST + COMMON.NONPUBLIC_PREFIX + API.PERMISSION + "/list_all_permission",
            type:"get",
            headers:{
                token:token
            },
            dataType:"json",
            async:false,
            success:function (res) {
                console.log(res);
                switch (res.status) {
                    case -1:
                        systemAlert("red","获取权限列表错误，"+res.msg);
                        break;
                    case 0:
                        renderPermissionSelector(res.data); // 渲染权限选择器
                        break;
                    case 10002:
                        systemAlert("red","登录失效，请重新登录",function () {
                            goLogin();
                        })
                        break;
                    case 10006:
                        systemAlert("red","操作无权限！");
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
                }
            },
            error:function (res) {
                systemAlert("red","请求错误，code："+res.status);
            }
        })
    }else{
        systemAlert("red","登录失效，请重新登录",function () {
            goLogin();
        })
    }
}

/**
 * 查询所有的角色
 */
function selectAllRole() {
    var token = $.cookie(USER_DIGEST_COOKIE);
    if(token != null){
        $.ajax({
            url:HOST + COMMON.NONPUBLIC_PREFIX + API.PERMISSION + "/list_role",
            type:"get",
            headers:{
                token:token
            },
            dataType:"json",
            async:false,
            success:function (res) {
                console.log(res);
                switch (res.status) {
                    case -1:
                        systemAlert("red","获取角色列表错误，"+res.msg);
                        break;
                    case 0:
                        systemRole = res.data;
                        break;
                    case 10002:
                        systemAlert("red","登录失效，请重新登录",function () {
                            goLogin();
                        })
                        break;
                    case 10006:
                        systemAlert("red","操作无权限！");
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
                }
            },
            error:function (res) {
                systemAlert("red","请求错误，code："+res.status);
            }
        })
    }else{
        systemAlert("red","登录失效，请重新登录",function () {
            goLogin();
        })
    }
}

/**
 * 渲染权限选择器
 */
function renderPermissionSelector(data) {
    $("#permissionTotalNum").text(data.length);
    $(".permission-selector").empty();
    for (var i=0;i<data.length;i++){
        var tempId = data[i].resource+"_"+data[i].action;
        $("#resource_"+data[i].resource+" .row").append("<div class=\"col-md-3\">\n" +
            "                                            <div class=\"custom-control custom-checkbox\">\n" +
            "                                                <input type=\"checkbox\" name='permission' class=\"custom-control-input\" value='"+data[i].id+"' id=\""+tempId+"\">\n" +
            "                                                <label class=\"custom-control-label\" for=\""+tempId+"\">"+data[i].action+"</label>\n" +
            "                                            </div>\n" +
            "                                        </div>");
    }
    hasRenderPermissionSelector = true;
}

/**
 * 上传新增的角色
 */
function uploadRole() {
    var formData = $("#permissionSelectorForm").serializeJson();
    if (formData.role_name.trim() == ""){
        systemAlert("red","角色名不能为空");
    } else if(formData.role_name.trim() == "sAdmin"){
        systemAlert("red","角色名不能为sAdmin");
    }else if (formData.permission == null) {
        systemAlert("red","你必须至少选择一个权限");
    }else{
        if (typeof formData.permission == "string"){
            formData.permission = [formData.permission];    // 如果只选择了一个权限，对其进行组装成数组对象
        }
        var token = $.cookie(USER_DIGEST_COOKIE);
        if(token != null){
            $.ajax({
                url:HOST + COMMON.NONPUBLIC_PREFIX + API.PERMISSION + "/add_role",
                type:"post",
                headers:{
                    token:token
                },
                data:{
                    role_name:formData.role_name,
                    permissions:formData.permission
                },
                dataType:"json",
                async:false,
                success:function (res) {
                    console.log(res);
                    switch (res.status) {
                        case -1:
                            systemAlert("red","系统错误，"+res.msg);
                            break;
                        case 0:
                            systemAlert("green","新增角色成功",function () {
                                location.reload();
                            });
                            break;
                        case 10002:
                            systemAlert("red","登录失效，请重新登录",function () {
                                goLogin();
                            })
                            break;
                        case 10006:
                            systemAlert("red","操作无权限！");
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
                        default:
                            systemAlert("red",res.msg);
                    }
                },
                error:function (res) {
                    systemAlert("red","请求错误，code："+res.status);
                }
            })
        }else{
            systemAlert("red","登录失效，请重新登录",function () {
                goLogin();
            })
        }
    }
}

/**
 * 上传用户角色关系
 */
function uploadUserRole() {
    var role = $("#roleSelector").val();
    var selectList = $("#userInfoTable").bootstrapTable("getSelections");
    if (selectList.length == 0) {
        systemAlert("red","你必须至少选择一个用户！");
    }else{
        systemConfirm("确定为这<strong>"+selectList.length+"</strong>位用户分配该角色吗？",function () {
            var userIdArray = [];
            for (var i = 0; i < selectList.length; i++) {
                userIdArray.push(selectList[i].id);
            }
            $.ajax({
                url:HOST + COMMON.NONPUBLIC_PREFIX + API.PERMISSION + "/add_user_role",
                type:"post",
                headers:{
                    token:$.cookie(USER_DIGEST_COOKIE)
                },
                data:{
                    role:role,
                    userId:userIdArray
                },
                dataType:"json",
                success:function (res) {
                    console.log(res);
                    switch (res.status) {
                        case -1:
                            systemAlert("red","系统错误，"+res.msg);
                            break;
                        case 0:
                            systemAlert("green","添加成功！",function () {
                                $("#userTable").bootstrapTable("refresh");
                                $("#userInfoTable").bootstrapTable("refresh");
                                $("#userRoleModal").modal("hide");
                            });
                            break;
                        case 10002:
                            systemAlert("red","登录失效，请重新登录",function () {
                                goLogin();
                            })
                            break;
                        case 10006:
                            systemAlert("red","操作无权限！");
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
                        case 100018:
                            systemAlert("red","你所操作的用户中已有角色，不可操作。");
                            break;
                        default:
                            systemAlert("red",res.msg);
                    }
                },
                error:function (res) {
                    systemAlert("red","请求错误，code："+res.status);
                }
            })
        })
    }
}

/**
 * 更改用户角色
 */
function updateUserRole() {
    var token = $.cookie(USER_DIGEST_COOKIE);
    if(token != null){
        $.ajax({
            url:HOST + COMMON.NONPUBLIC_PREFIX + API.PERMISSION + "/update_user_role",
            type:"post",
            headers:{
                token:token
            },
            data:{
                userId:tempUserId,
                roleId:$("#editRoleSelector").val()
            },
            dataType:"json",
            async:false,
            success:function (res) {
                console.log(res);
                switch (res.status) {
                    case -1:
                        systemAlert("red","获取角色列表错误，"+res.msg);
                        break;
                    case 0:
                        systemAlert("green","更新成功！",function () {
                            $("#userTable").bootstrapTable("refresh");
                            $("#userInfoTable").bootstrapTable("refresh");
                            $("#editUserRoleModal").modal("hide");
                        })
                        break;
                    case 10002:
                        systemAlert("red","登录失效，请重新登录",function () {
                            goLogin();
                        })
                        break;
                    case 10006:
                        systemAlert("red","你暂无权限！（超级管理员权限只能由超级管理员修改）");
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
                }
            },
            error:function (res) {
                systemAlert("red","请求错误，code："+res.status);
            }
        })
    }else{
        systemAlert("red","登录失效，请重新登录",function () {
            goLogin();
        })
    }
}