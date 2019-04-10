var resourceType;
var pageData={
    articles:{
        page:1,
        pageSize:10,
        total:10
    },
    resource:{
        page:1,
        pageSize:10,
        total:10
    }
}
var hasRender = {
    resource:false,
    articles:false
}
$(function () {
    getNoticeList();
    // 验证加载用户信息是否成功，成功才进行下一步操作
    if(loadUserInfo()){

    }
    // 初始化Table
    var oTable = new TableInit();
    var type = getUrlData().t;
    if(type != "" && type!=null){
        if(type == "article"){
            $("#article-tab").tab("show");
            loadArticleList();
        }else {
            resourceType = type;
            $("#resourceType").val(resourceType);
        }
    }
    resourceType = $("#resourceType").val();
    $("#resourceType").change(function () {
        resourceType = $("#resourceType").val();
        $('#resourceTable').bootstrapTable("refresh",{
            url: HOST + COMMON.PUB_PREFIX + API.RESOURCE + "/type_list?type="+resourceType
        });
    })
    oTable.Init();
    $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
        // e.target // newly activated tab
        // e.relatedTarget // previous active tab
        if($(e.target).attr("id") == "article-tab" && !hasRender.articles){
            loadArticleList();
        }else if($(e.target).attr("id") == "resource-tab" && !hasRender.resource){
            oTable.Init();
        }
    })
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
        $('#resourceTable').bootstrapTable({
            url: HOST + COMMON.PUB_PREFIX + API.RESOURCE + "/type_list?type="+resourceType,         //请求后台的URL（*）
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
            clickToSelect: true,                //是否启用点击选中行
            height: 700,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "id",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            contentType: 'application/json',
            dataType: 'json',
            refresh:true,
            responseHandler: function (res) {
                // console.log(res);
                switch (res.status) {
                    case -1:
                        systemAlert("red","获取列表错误，"+res.msg);
                        break;
                    case 0:
                        hasRender.resource = true;
                        return {
                        "total": res.data.total,//总页数
                        "rows": res.data.rows   //数据
                    }
                }

            },
            columns: [{
                checkbox: true
            }, {
                field: 'resource',
                title: 'ID',
                formatter:function (value, row, index) {
                    return value.id.substr(0,6)+"...";
                }
            }, {
                field: 'resource',
                title: 'Type',
                formatter:function (value, row, index) {
                    return value.type;
                }
            }, {
                field: 'author',
                title: 'Author',
                formatter:function (value, row, index) {
                    return value.realName;
                }
            }, {
                field: 'resource',
                title: 'Size',
                formatter:function (value, row, index) {
                    if (value.fileSize < 1024) {
                        return value.fileSize + "KB";
                    }else if(value.fileSize>=1024){
                        return parseInt(value.fileSize/(1024*1024)) + "MB";
                    }
                }
            }, {
                field: 'resource',
                title: 'download',
                formatter:function (value, row, index) {
                    return value.downloadNum;
                }
            }, {
                field: 'resource',
                title: 'Create Time',
                formatter:function (value, row, index) {
                    return value.createTime;
                }
            },{
                field: 'resource',
                title: 'Update Time',
                formatter:function (value, row, index) {
                    return value.updateTime;
                }
            },{
                field: 'resource',
                title: 'Operate',
                formatter: function(value, row, index) {
                    return [
                        '<a class="btn active btn-sm btn-primary" href="../resource-view.html?id='+value.id+'">Show</a>'
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
 * 加载文章列表
 */
function loadArticleList() {
    $.ajax({
        url: HOST + COMMON.PUB_PREFIX + API.ARTICLE + "/get_list",
        type:"get",
        dataType:"json",
        data:{
            page:pageData.articles.page,
            page_size:pageData.articles.pageSize
        },
        success:function (res) {
            console.log(res);
            switch (res.status) {
                case -1:
                    systemAlert("red","未知错误，"+res.msg);
                    break;
                case 0:
                    hasRender.articles = true;
                    pageData.articles.total = res.data.total;
                    $("#articleList").empty();
                    for (var i = 0; i < res.data.rows.length; i++) {
                        var createTime = res.data.rows[i].article.createTime;
                        $("#articleList").append("<div class=\"card-body card-list\">\n" +
                            "                                    <div class=\"d-flex w-100 justify-content-between\">\n" +
                            "                                        <h5 class=\"card-title\">"+res.data.rows[i].article.title+"</h5>\n" +
                            "                                        <small class=\"text-muted text-right\">"+createTime+"</small>\n" +
                            "                                    </div>\n" +
                            "                                    <p class=\"card-text\">"+res.data.rows[i].article.summary+"</p>\n" +
                            "                                    <a href=\"../article.html?id="+res.data.rows[i].article.id+"\" class=\"btn btn-primary\">Go to view</a>\n" +
                            "<p class=\"text-right show-num\">\n" +
                            "                                            <i class=\"fa fa-user\" aria-hidden=\"true\"></i> "+res.data.rows[i].author.realName+" &nbsp;&nbsp;\n" +
                            "                                            <i class=\"fa fa-eye\" aria-hidden=\"true\"></i> "+res.data.rows[i].article.seeNum+" &nbsp;&nbsp;\n" +
                            "                                        </p>" +
                            "                                </div>");
                    }
                    break;
            }
        },
        error:function (res) {
            console.log(res);
        }
    })
}

/**
 * 上一篇文章
 */
function preArticles() {
    if(pageData.articles.pageSize > pageData.articles.total){
        if (pageData.articles.page > 1) {
            pageData.articles.page -- ;
            loadArticleList();
        }
    }

}

/**
 * 下一篇文章
 */
function nextArticles() {
    if(pageData.articles.pageSize > pageData.articles.total){
        if (pageData.articles.page < pageData.articles.total/pageData.articles.pageSize) {
            pageData.articles.page ++ ;
            loadArticleList();
        }
    }
}