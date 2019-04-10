var pageData={
    articles:{
        page:1,
        pageSize:5
    },
    ppt:{
        page:1,
        pageSize:3
    },
    examination:{
        page:1,
        pageSize:5
    },
    project:{
        page:1,
        pageSize:3
    },
    software:{
        page:1,
        pageSize:5
    },
    downloadHot:{
        page:1,
        pageSize:3
    }
}
$(function () {
    init();
})

/**
 * 初始化函数
 */
function init() {
    getNoticeList();
    // 验证加载用户信息是否成功，成功才进行下一步操作
    if(loadUserInfo()){

    }
    loadResourceList();
    loadArticleList();
    loadHotResourceList()
}

/**
 * 加载热门资源列表
 */
function loadHotResourceList() {
    $.ajax({
        url: HOST + COMMON.PUB_PREFIX + API.RESOURCE + "/list_download_hot",
        type:"get",
        dataType:"json",
        data:{
            page:pageData.downloadHot.page,
            page_size:pageData.downloadHot.pageSize
        },
        async:false,
        success:function (res) {
            console.log(res);
            switch (res.status) {
                case -1:
                    systemAlert("red","未知错误，"+res.msg);
                    break;
                case 0:
                    $("#downloadHotResource").empty();
                    for (var i = 0; i < res.data.length; i++) {
                        $("#downloadHotResource").append("<div class=\"col-sm-12 col-md-4\" align=\"center\">\n" +
                            "                            <div class=\"card shadow\" style=\"width: 18rem;margin: 20px 0;\">\n" +
                            "                                <div class=\"card-img-top bf-card-img\" style=\"background:url('"+res.data[i].resource.imgUrl+"') 50% 50% no-repeat;background-size: cover;\"></div>\n" +
                            "                                <div class=\"card-body\">\n" +
                            "                                    <h5 class=\"card-title\">"+res.data[i].resource.title+"</h5>\n" +
                            "                                    <p class=\"card-text\">"+res.data[i].resource.summary+"</p>\n" +
                            "                                    <a href=\"../resource-view.html?id="+res.data[i].resource.id+"\" class=\"btn btn-primary\">Go to view</a>\n" +
                            "                                </div>\n" +
                            "                            </div>\n" +
                            "                        </div>");
                    }
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
        }
    })
}
/**
 * 加载文章列表
 */
function loadArticleList() {
    var digest = $.cookie(USER_DIGEST_COOKIE);
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
                case 100015:
                    if(digest==null){
                        systemAlert("red","您的操作太快，请稍后再试哦~");
                    }
                    break;
                case 100016:
                    if(digest==null){
                        systemAlert("red","由于你的请求过快，被视为恶意请求，系统已禁用你的请求，2小时后失效。<br>为营造良好上网氛围，请不要进行恶意操作！");
                    }
                    break;
                case 100017:
                    if(digest==null){
                        systemAlert("red","你的ip正处于禁用期间，暂无法使用该系统，请解禁后再试（禁用时间2小时）<br>为营造良好上网氛围，请不要进行恶意操作！");
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
 * 加载资源列表
 */
function loadResourceList() {
    $.ajax({
        url: HOST + COMMON.PUB_PREFIX + API.RESOURCE + "/list",
        type:"get",
        dataType:"json",
        data:{
            page:pageData.articles.page,
            page_size:pageData.articles.pageSize
        },
        async:false,
        success:function (res) {
            console.log(res);
            switch (res.status) {
                case -1:
                    systemAlert("red","未知错误，"+res.msg);
                    break;
                case 0:
                    for (var i = 0; i < res.data.length; i++) {
                        var createTime = res.data[i].resource.createTime;
                        $("#resourceList").append("<div class=\"card-body card-list\"><div class='row'>\n" +
                            "                                    <div class=\"col-12\">\n" +
                            "                                        <div class=\"d-flex w-100 justify-content-between\">\n" +
                            "                                            <h5 class=\"card-title\">"+res.data[i].resource.title+"</h5>\n" +
                            "                                            <small class=\"text-muted text-right\">"+createTime+"</small>\n" +
                            "                                        </div>\n" +
                            "                                    </div>\n" +
                            "                                    <div class=\"col-sm-12 col-md-4\">\n" +
                            "                                        <img src=\""+res.data[i].resource.imgUrl+"\" class=\"rounded mx-auto d-block shadow\" style=\"max-width: 100%;\">\n" +
                            "                                    </div>\n" +
                            "                                    <div class=\"col-sm-12 col-md-8\">\n" +
                            "                                        <p class=\"card-text\">"+res.data[i].resource.summary+"</p>\n" +
                            "                                        <a href=\"../resource-view.html?id="+res.data[i].resource.id+"\" class=\"btn btn-primary\">Go to view</a>\n" +
                            "                                        <p class=\"text-right show-num\">" +
                            "<i class=\"fa fa-user\" aria-hidden=\"true\"></i> "+res.data[i].author.realName+" &nbsp;&nbsp;" +
                            "<i class=\"fa fa-eye\" aria-hidden=\"true\"></i> "+res.data[i].resource.seeNum+" &nbsp;&nbsp;" +
                            "<i class=\"fa fa-download\" aria-hidden=\"true\"></i> "+res.data[i].resource.downloadNum+" </p>\n" +
                            "                                    </div>\n" +
                            "                                </div></div>");
                    }
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
        }
    })
}