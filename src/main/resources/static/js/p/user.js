var tempValue = {
    userList:[],
    editUser:{},    // 修改用户信息当前用户
    updatePassword:"",  //更新密码当前用户的id
    saveUser:false  // true，保存用户；false，新增用户
}
$(function () {
    // 验证加载用户信息是否成功，成功才进行下一步操作
    if(loadUserInfo()){
        getUserPermissionList();
        renderPage();   //根据权限渲染页面
        // 初始化Table
        var oTable = new TableInit();
        oTable.Init();
    }
})

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
            url: HOST + COMMON.NONPUBLIC_PREFIX + API.USER + "/get_list",         //请求后台的URL（*）
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
            height: 700,                        //行高
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
                        tempValue.userList = res.data.rows;
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
            }, {
                field: 'createTime',
                title: 'Register Time'
            },{
                field: 'updateTime',
                title: 'Update Time'
            },{
                field: 'id',
                title: 'Operate',
                formatter: function(value, row, index) {
                    return [
                        '<button class="btn active btn-sm btn-warning" onclick="editUser(\''+value+'\','+index+')">Edit</button>' +
                        '<button class="btn active btn-sm btn-info" onclick="showPasswordModal(\''+value+'\')">Password</button>' +
                        '<button class="btn active btn-sm btn-danger" onclick="deleteUser(\''+value+'\')">Delete</button>'
                    ].join('');
                }
            } ]
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
 * 删除用户
 * @param id
 */
function deleteUser(id) {
    systemConfirm("删除后该用户所上传的文章、资源等均将被删除，确定删除该用户？",function () {
        $.ajax({
            url:HOST + COMMON.NONPUBLIC_PREFIX + API.USER + "/delete",
            type:"delete",
            headers:{
                token:$.cookie(USER_DIGEST_COOKIE)
            },
            data:{
                id:id
            },
            dataType:"json",
            success:function (res) {
                console.log(res);
                switch (res.status) {
                    case 0:
                        systemAlert("green","删除成功");
                        $('#userTable').bootstrapTable("refresh");
                        break;
                    case -1:
                        systemAlert("red","未知错误，"+res.msg);
                        break;
                    case 10006:
                        systemAlert("red","你暂无删除该用户的权限!（非超级管理员无法删除超级管理员）");
                        break;
                    case 100020:
                        systemAlert("red","无法删除自己哦~");
                        break;
                    default:
                        systemAlert("red",res.msg);
                }
            },
            error:function (res) {
                systemAlert("red","出错了，code："+res.status);
            }
        })
    })
}

/**
 * 保存用户
 */
function saveUser() {
    var userFormData = $("#userForm").serializeJson();
    userFormData.id = tempValue.editUser.id;
    userFormData.sex = parseInt(userFormData.sex);
    if (tempValue.saveUser) {
            // 修改用户
            $.ajax({
                url:HOST + COMMON.NONPUBLIC_PREFIX + API.USER + "/update",
                type:"put",
                headers:{
                    token:$.cookie(USER_DIGEST_COOKIE),
                    "Content-Type":"application/json"
                },
                data:JSON.stringify(userFormData),
                dataType:"json",
                success:function (res) {
                    console.log(res);
                    switch (res.status) {
                        case 0:
                            systemAlert("green","修改成功",function () {
                                $("#userModal").modal("hide");
                                $('#userTable').bootstrapTable("refresh");
                            });
                            break;
                        case -1:
                            systemAlert("red","未知错误，"+res.msg);
                            break;
                        case 10006:
                            systemAlert("red","你没有修改用户信息的权限!（非超级管理员无法修改超级管理员）");
                            break;
                        case 10003:
                            systemAlert("red","用户不存在");
                            break;
                        default:
                            systemAlert("red",res.msg);
                    }
                },
                error:function (res) {
                    systemAlert("red","出错了，code："+res.status);
                }
            })
        } else {
        if(userFormData.account.trim() == ""){
            systemAlert("red","账户为必填项，不能为空");
        }else if(userFormData.password.length <6){
            systemAlert("red","密码不可小于6位");
        }else{
            // 新增用户
            $.ajax({
                url:HOST + COMMON.NONPUBLIC_PREFIX + API.USER + "/add",
                type:"post",
                headers:{
                    token:$.cookie(USER_DIGEST_COOKIE),
                    "Content-Type":"application/json"
                },
                data:JSON.stringify(userFormData),
                dataType:"json",
                success:function (res) {
                    console.log(res);
                    switch (res.status) {
                        case 0:
                            systemAlert("green","新增成功",function () {
                                $("#userModal").modal("hide");
                                $('#userTable').bootstrapTable("refresh");
                            });
                            break;
                        case -1:
                            systemAlert("red","未知错误，"+res.msg);
                            break;
                        case 10006:
                            systemAlert("red","你没有修改用户信息的权限!");
                            break;
                        case 10003:
                            systemAlert("red","用户不存在");
                            break;
                        case 100018:
                            systemAlert("red","该账户已存在，换一个试试");
                            break;
                    }
                },
                error:function (res) {
                    systemAlert("red","出错了，code："+res.status);
                }
            })
        }
        }
}

/**
 * 批量删除
 */
function batchDelete() {
    systemConfirm("确定删除选择的所有用户？用户删除后所有的文章、资源将全部删除！",function () {
        var selectList = $("#userTable").bootstrapTable("getSelections");
        var deleteList = [];
        if(selectList.length == 0){
            systemAlert("red","请先选择用户！");
        }else{
            for (var i =0;i<selectList.length;i++){
                deleteList.push(selectList[i].id);
            }
            $.ajax({
                url:HOST + COMMON.NONPUBLIC_PREFIX + API.USER + "/batch_delete",
                type:"delete",
                headers:{
                    token:$.cookie(USER_DIGEST_COOKIE)
                },
                data:{
                    id_list:deleteList
                },
                dataType:"json",
                success:function (res) {
                    console.log(res);
                    switch (res.status) {
                        case 0:
                            systemAlert("green","删除成功",function () {
                                $('#userTable').bootstrapTable("refresh");
                            });
                            break;
                        case -1:
                            systemAlert("red","未知错误，"+res.msg);
                            break;
                        case 10006:
                            systemAlert("red","你没有删除用户的权限!");
                            break;
                    }
                },
                error:function (res) {
                    systemAlert("red","出错了，code："+res.status);
                }
            })
        }
    })

}

/**
 * 新增用户
 */
function addUser() {
    $("#userForm")[0].reset();
    $("#userForm input[name='password']").attr("disabled",false);
    tempValue.saveUser = false;
    $("#userModal").modal("show");
}

/**
 * 修改用户
 * @param id
 */
function editUser(id,index) {
    var user  = tempValue.userList[index];
    tempValue.editUser = user;
    $("#userForm input[name='account']").val(user.account);
    $("#userForm input[name='account']").attr("disabled",true);
    $("#userForm input[name='password']").val(user.password);
    $("#userForm input[name='password']").attr("disabled",true);
    if(user.sex == 0){
        $("#boyRadio").attr("checked",true);
    }else{
        $("#girlRadio").attr("checked",true);
    }
    $("#userForm input[name='realName']").val(user.realName);
    $("#userForm input[name='sicauId']").val(user.sicauId);
    $("#userForm input[name='email']").val(user.email);
    $("#userForm input[name='telephone']").val(user.telephone);
    $("#userForm input[name='website']").val(user.website);
    tempValue.saveUser = true;
    $("#userModal").modal("show");
}

/**
 * 显示修改密码模态框
 * @param id
 */
function showPasswordModal(id) {
    tempValue.updatePassword = id;
    $("#passwordModal").modal("show");
}

/**
 * 修改密码
 * @param id
 */
function updatePassword() {
    var newPassword = $("#passwordForm").serializeJson().password;
    if(newPassword.trim().length < 6){
        systemAlert("red","密码不可少于6位");
    }else{
        $.ajax({
            url:HOST + COMMON.NONPUBLIC_PREFIX + API.USER + "/permission_update_password",
            type:"put",
            headers:{
                token:$.cookie(USER_DIGEST_COOKIE)
            },
            data:{
                id:tempValue.updatePassword,
                password:$("#passwordForm").serializeJson().password
            },
            dataType:"json",
            success:function (res) {
                console.log(res);
                switch (res.status) {
                    case 0:
                        systemAlert("green","修改成功",function () {
                            $("#passwordModal").modal("hide");
                        });

                        break;
                    case -1:
                        systemAlert("red","未知错误，"+res.msg);
                        break;
                    case 10006:
                        systemAlert("red","操作无权限!（超级管理员的信息只能由超级管理员修改）");
                        break;
                }
            },
            error:function (res) {
                systemAlert("red","出错了，code："+res.status);
            }
        })
    }
}