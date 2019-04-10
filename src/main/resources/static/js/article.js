
$(function () {
    init();
})

/**
 * 初始化函数
 */
function init() {
    getNoticeList();
    loadArticle();
    // 验证加载用户信息是否成功，成功才进行下一步操作
    if(loadUserInfo()){

    }
}

/**
 * 加载文章
 */
function loadArticle() {
    var urlData = getUrlData();
    if (urlData.id == "" || urlData.id == null){
        location.href = "error/404.html";
    }else{
        $.ajax({
            url: HOST + COMMON.PUB_PREFIX + API.ARTICLE + "/get",
            type:"get",
            data:{
                id:urlData.id
            },
            dataType:"json",
            async:false,
            success:function (res) {
                console.log(res);
                switch (res.status) {
                    case -1:
                        systemAlert("red","未知错误，"+res.msg);
                        break;
                    case 100010:
                        location.href = "error/404.html";
                        break;
                    case 0:

                        $("#articleTitle").text(res.data.title);
                        $("#seeNum").text(res.data.seeNum);
                        var createTime = res.data.createTime;
                        $("#createTime").text(createTime);
                        // 渲染标签，如果有标签则渲染，没有则不做处理
                        if (res.data.strTags != null) {
                            var tags = res.data.strTags.split(",");
                            for (var i = 0; i < tags.length; i++) {
                                var color = SAVE_COLOR[Math.ceil(SAVE_COLOR.length*Math.random())];
                                $("#articleTags").append("<span class=\"article-tag\" style='background-color: "+color+";'>"+tags[i]+"</span>");
                            }
                        }
                        $("#articleContent").html(labelDecode(res.data.content));
                        $("#articleContent table").addClass("table table-bordered");
                        renderViewer();
                        break;
                    case 100017:
                        systemAlert("red","你的ip正处于禁用期间，暂无法使用该系统，请解禁后再试（禁用时间2小时）<br>为营造良好上网氛围，请不要进行恶意操作！");
                        break;
                }
            },
            error:function (res) {
                console.log(res);
                systemAlert("red","请求错误，code："+res.status);
            }

        })
    }
}