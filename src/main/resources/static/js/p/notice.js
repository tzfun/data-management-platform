var tags = {};
var uploadArticle = false;
$(function () {
    init();

})
/**
 * 监听页面变化
 */
window.onbeforeunload = function(e) {
    if (!uploadArticle) {
        return 1;
    }
};

/**
 * 初始化函数
 */
function init() {
    renderEditor();
    // 验证加载用户信息是否成功，成功才进行下一步操作
    if(loadUserInfo()){
        getUserPermissionList();
        renderPage();   //根据权限渲染页面
        var nTable = new NoticeTableInit();
        nTable.Init();
    }
}

/**
 * 表格初始化对象
 * 渲染文章表格
 * @returns {Object}
 * @constructor
 */
var NoticeTableInit = function () {
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function () {
        $('#noticeTable').bootstrapTable({
            url: HOST + COMMON.NONPUBLIC_PREFIX + API.NOTICE + "/list_all",         //请求后台的URL（*）
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
            height: 400,                        //行高
            uniqueId: "id",                     //每一行的唯一标识，一般为主键列
            showToggle:false,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                   //是否显示父子表
            contentType: 'application/json',
            dataType: 'json',
            refresh:false,
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
            columns: [ {
                checkbox: true
            }, {
                field: 'id',
                title: 'ID',
                formatter:function (value) {
                    return value.substr(0,6)+"...";
                }
            },{
                field: 'title',
                title: 'Title'
            },{
                field: 'seeNum',
                title: 'View'
            }, {
                field: 'longTerm',
                title: 'Type',
                formatter:function (value) {
                    return value ? "长期" : "短期";
                }
            },{
                field: 'createTime',
                title: 'Create Time'
            },{
                field:'id',
                title:'Opera',
                formatter:function (value,row) {
                    return '<a class="btn active btn-sm btn-primary" href="../notice-view.html?id='+value+'">Show</a>' +
                        '<a class="btn active btn-sm btn-danger" href="javascript:deleteNotice(\''+value+'\')">Delete</a>';
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
 * 删除公告
 * @param id
 */
function deleteNotice(id) {
    systemConfirm("你确定删除此公告？",function () {
        $.ajax({
            url:HOST + COMMON.NONPUBLIC_PREFIX + API.NOTICE + "/delete",
            type:"delete",
            headers:{
                token:$.cookie(USER_DIGEST_COOKIE)
            },
            data:{
                id:id
            },
            dataType:"json",
            async:false,
            success:function (res) {
                console.log(res);
                switch (res.status) {
                    case 0:
                        systemAlert("green","删除成功！",function () {
                            $('#noticeTable').bootstrapTable("refresh");
                        })
                        break;
                    case -1:
                        systemAlert("red","删除失败，错误信息："+res.data);
                        break;
                    case 10006:
                        systemAlert("red","您暂无删除公告的权限!！如需开通权限请联系管理员。");
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
                console.log(res);
                systemAlert("red","公告删除失败，code："+res.status);
            }
        })
    })
}

/**
 * saveArticle 保存文章
 */
function saveArticle() {
    var formData = $("#articleForm").serializeJson();
    formData.content = editor.getData();    // 获取富文本编辑器内容
    console.log(formData);
    if(formData.title.trim() == ""){
        systemAlert("red","文章标题不能为空")
    }else{
        systemConfirm("确定发布公告？",function () {
            uploadArticles(formData);
        })
    }
}

/**
 * 上传文章
 */
function uploadArticles(data) {
    data.content = strFilter(data.content);
    $.ajax({
        url:HOST + COMMON.NONPUBLIC_PREFIX + API.NOTICE + "/add",
        type:"post",
        headers:{
            token:$.cookie(USER_DIGEST_COOKIE)
        },
        data:{
            title:data.title,
            content:data.content,
            long_term:data.long_term,
            summary:document.getElementsByClassName("ck-content")[0].textContent.substring(0,64)
        },
        dataType:"json",
        async:false,
        success:function (res) {
            console.log(res);
            switch (res.status) {
                case 0:
                    uploadArticle = true;
                    systemAlert("green","上传成功！",function () {
                        location.reload();
                    })
                    break;
                case -1:
                    systemAlert("red","上传失败，错误信息："+res.data);
                    break;
                case 10006:
                    systemAlert("red","您暂无发布公告的权限!！如需开通权限请联系管理员。");
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
            console.log(res);
            systemAlert("red","公告上传失败，code："+res.status);
        }
    })
}

/**
 * 渲染富文本编辑器
 * @returns {UploadAdapter}
 */
function renderEditor() {
    ClassicEditor.create( document.querySelector( '#editor' ),{
        language:"zh-cn"
    })
        .then( newEditor => {
        editor = newEditor;
    // 这个地方加载了适配器
    editor.plugins.get('FileRepository').createUploadAdapter = (loader)=>{
        return new UploadAdapter(loader);
    };
})
.catch( error => {
        console.error( error );
} );
}

/**
 * 富文本编辑器文件上传类
 */
class UploadAdapter {
    constructor(loader) {
        this.loader = loader;
    }
    upload() {
        return new Promise((resolve, reject) => {
            const data = new FormData()
            data.append('upload', this.loader.file);
        data.append('allowSize', 3);//允许图片上传的大小/兆
        $.ajax({
            url: HOST + COMMON.NONPUBLIC_PREFIX + API.FILE + "/article_img",
            type: 'POST',
            headers:{
                token:$.cookie(USER_DIGEST_COOKIE)
            },
            data: data,
            dataType: 'json',
            processData: false,
            contentType: false,
            success: function (data) {
                console.log(data);
                if (data.uploaded) {
                    resolve({
                        default: data.url
                    });
                } else {
                    reject(data.msg);
                }
            }
        });

    })
    }
    abort() {
    }
}