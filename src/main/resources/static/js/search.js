$(function () {
    init();
})

/**
 * 初始化函数
 */
function init() {
    // 验证加载用户信息是否成功，成功才进行下一步操作
    if(loadUserInfo()){

    }
    goSearch();
}

/**
 * 搜索
 */
function goSearch(){
    var keyword = getUrlParam("keyword");
    if (keyword.trim() != "") {
        $.ajax({
            url:HOST+COMMON.PUB_PREFIX+API.SEARCH+"/public_search",
            type:"get",
            data:{
                keyword : keyword
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
                        renderResult(res.data);
                        break;
                }
            },
            error:function (res) {
                systemAlert("red","出错了，code:"+res.status);
            }
        })
    }else{
        $("#resultContainer").html("<div align=\"center\">\n" +
            "                    <img src=\"images/icon/filed.png\" style=\"width: 250px;\">\n" +
            "                    <h2>暂无您所搜索的结果</h2>\n" +
            "                </div>");
    }
}

/**
 * 渲染搜索的结果
 * @param data
 */
function renderResult(data) {
    var total = data.resources.length + data.articles.length + data.notices.length;
    if (total !=0){
        $("#resultContainer").append("<h2 class=\"result-total\">共检索到<strong>"+total+"</strong>条结果：</h2>");
        // 渲染资源
        if (data.resources.length != 0) {
            $("#resultContainer").append("<div class=\"card-container\" >\n" +
                "                    <div class=\"card shadow\">\n" +
                "                        <h5 class=\"card-header\">Resources </h5>\n" +
                "                        <ul id=\"resourceList\"></ul>\n" +
                "                    </div>\n" +
                "                </div>")
            for (var i in data.resources) {
                $("#resourceList").append("<li class=\"result-list\">\n" +
                    "                    <h4 class=\"result-title\"><a href=\"/resource-view.html?id="+data.resources[i].id+"\">"+data.resources[i].title+"</a></h4>\n" +
                    "                    <span class=\"result-type-container\">\n" +
                    "                                    <span class=\"badge badge-pill badge-info\">"+data.resources[i].type+"</span>\n" +
                    "                                </span>\n" +
                    "                    <p>"+data.resources[i].summary+"</p>\n" +
                    "                    <div class=\"result-time\">"+data.resources[i].createTime+"</div>\n" +
                    "                </li>")
            }
        }
        // 渲染文章
        if(data.articles.length !=0){
            $("#resultContainer").append("<div class=\"card-container\" >\n" +
                "                    <div class=\"card shadow\">\n" +
                "                        <h5 class=\"card-header\">Articles </h5>\n" +
                "                        <ul id=\"articleList\"></ul>\n" +
                "                    </div>\n" +
                "                </div>")
            for (var i in data.articles) {
                var tagHTML="";
                if (data.articles[i].strTags != null && data.articles[i].strTags != "") {
                    var tagList = data.articles[i].strTags.split(",");
                    for (var j = 0; j < tagList.length; j++) {
                        tagHTML += "<span class=\"badge badge-pill badge-info\">"+tagList[j]+"</span>";
                    }
                }
                $("#articleList").append("<li class=\"result-list\">\n" +
                    "                    <h4 class=\"result-title\"><a href=\"/article.html?id="+data.articles[i].id+"\">"+data.articles[i].title+"</a></h4>\n" +
                    "                    <span class=\"result-type-container\">\n" +tagHTML+
                    "                    <p>"+data.articles[i].summary+"</p>\n" +
                    "                    <div class=\"result-time\">"+data.articles[i].createTime+"</div>\n" +
                    "                </li>")
            }
        }
        // 渲染公告
        if(data.notices.length !=0){
            $("#resultContainer").append("<div class=\"card-container\" >\n" +
                "                    <div class=\"card shadow\">\n" +
                "                        <h5 class=\"card-header\">Notices </h5>\n" +
                "                        <ul id=\"noticeList\"></ul>\n" +
                "                    </div>\n" +
                "                </div>")
            for (var i in data.notices) {
                var type = data.notices[i].longTerm ? "长期" : "短期";
                $("#noticeList").append("<li class=\"result-list\">\n" +
                    "                    <h4 class=\"result-title\"><a href=\"/notice-view.html?id="+data.notices[i].id+"\">"+data.notices[i].title+"</a></h4>\n" +
                    "                    <span class=\"result-type-container\">\n" +
                    "                                    <span class=\"badge badge-pill badge-info\">"+type+"</span>\n" +
                    "                                </span>\n" +
                    "                    <p>"+data.notices[i].summary+"</p>\n" +
                    "                    <div class=\"result-time\">"+data.notices[i].createTime+"</div>\n" +
                    "                </li>")
            }
        }
    }else {
        $("#resultContainer").html("<div align=\"center\">\n" +
            "                    <img src=\"images/icon/filed.png\" style=\"width: 250px;\">\n" +
            "                    <h2>暂无您所搜索的结果</h2>\n" +
            "                </div>");
    }
}