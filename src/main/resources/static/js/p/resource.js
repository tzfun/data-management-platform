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
        var rTable = new ResourceTableInit();
        rTable.Init();
    }
}

/**
 * 表格初始化对象
 * 渲染资源表格
 * @returns {Object}
 * @constructor
 */
var ResourceTableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#resourceTable').bootstrapTable({
            url: HOST + COMMON.NONPUBLIC_PREFIX + API.RESOURCE + "/list_all",         //请求后台的URL（*）
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
                        return {
                            "total": res.data.total,//总页数
                            "rows": res.data.rows   //数据
                        }
                    case 10006:
                        systemAlert("red","您暂无查询系统资源的权限！");
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
            columns: [{
                checkbox: true
            }, {
                field: 'id',
                title: 'ID',
                formatter:function (value, row, index) {
                    return value.substr(0,6)+"...";
                }
            }, {
                field: 'type',
                title: 'Type'
            }, {
                field:"hasPass",
                title:"Has Pass"
            },{
                field: 'fileSize',
                title: 'Size',
                formatter:function (value, row, index) {
                    if (value < 1024) {
                        return value + "KB";
                    }else if(value>=1024){
                        return parseInt(value/(1024*1024)) + "MB";
                    }
                }
            }, {
                field: 'downloadNum',
                title: 'download'
            }, {
                field: 'createTime',
                title: 'Create Time'
            },{
                field: 'updateTime',
                title: 'Update Time'
            },{
                field: 'id',
                title: 'Operate',
                formatter: function(value, row, index) {
                    var pass = row.hasPass ? '不通过' : '通过';
                    return '<a class="btn active btn-sm btn-primary" href="../resource-view.html?id='+value+'">查看</a>' +
                        '<a class="btn active btn-sm btn-warning" href="javascript:passResource(\''+value+'\',\''+row.hasPass+'\')">'+pass+'</a>'+
                        '<a class="btn active btn-sm btn-danger" href="javascript:deleteResource(\''+value+'\')">删除</a>';
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
 * 删除资源
 */
function deleteResource(id) {
    systemConfirm("确定删除该资源？",function () {
        $.ajax({
            url:HOST + COMMON.NONPUBLIC_PREFIX + API.RESOURCE + "/delete",
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
                        systemAlert("green","删除成功",function () {
                            $("#resourceTable").bootstrapTable("refresh");
                        });
                        break;
                    case -1:
                        systemAlert("red","系统错误，"+res.msg);
                        break;
                    case 10002:
                        systemAlert("red","登录失效，请重新登录",function () {
                            goLogin();
                        });
                        break;
                    case 10006:
                        systemAlert("red","您暂无删除系统资源的权限！");
                        break;
                    case 100010:
                        systemAlert("red","该资源不存在！");
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
                systemAlert("red","出错了，code："+res.status);
            }
        })
    })
}

/**
 * 修改资源通过状态
 * @param id
 */
function passResource(id,hasPass) {
    $.ajax({
        url:HOST + COMMON.NONPUBLIC_PREFIX + API.RESOURCE + "/update_pass_status",
        type:"put",
        headers:{
            token:$.cookie(USER_DIGEST_COOKIE)
        },
        data:{
            id:id,
            has_pass:(hasPass == "true" ? 0 : 1)
        },
        dataType:"json",
        success:function (res) {
            console.log(res);
            switch (res.status) {
                case 0:
                    $("#resourceTable").bootstrapTable("refresh");
                    break;
                case -1:
                    systemAlert("red","系统错误，"+res.msg);
                    break;
                case 10002:
                    systemAlert("red","登录失效，请重新登录",function () {
                        goLogin();
                    });
                    break;
                case 10006:
                    systemAlert("red","您暂无修改系统资源的权限！");
                    break;
                case 100010:
                    systemAlert("red","该资源不存在！");
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
            systemAlert("red","出错了，code："+res.status);
        }
    })
}
