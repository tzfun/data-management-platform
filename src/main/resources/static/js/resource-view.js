var fileDownloadPath;
var author;
var downloadFile = true;
var tempFileId;
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
 * 监听页面变化
 */
window.onbeforeunload = function(e) {
    if (!downloadFile) {
        return 1;
    }
};

/**
 * 加载文章并渲染
 */
function loadArticle() {
    var urlData = getUrlData();
    if (urlData.id == "" || urlData.id == null){
        location.href = "error/404.html";
    }else{
        $.ajax({
            url: HOST + COMMON.PUB_PREFIX + API.RESOURCE + "/get",
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
                        tempFileId = res.data.resource.id;
                        fileDownloadPath = res.data.resource.fileUrl;
                        author = res.data.author;
                        $("#articleTitle").text(res.data.resource.title);   //标题
                        $("#seeNum").text(res.data.resource.seeNum);    //  浏览量
                        $(".article-summary").text(res.data.resource.summary);  // 摘要
                        $("#downloadNum").text(res.data.resource.downloadNum);  //  下载数量
                        var lastTime = res.data.resource.updateTime;
                        var createTime = res.data.resource.createTime;
                        $("#createTime").text(createTime);  //  渲染创建时间
                        $("#articleContent").html(labelDecode(res.data.resource.introduction)); //  文章内容
                        $("#articleContent table").addClass("table table-bordered");    //  文章里的表格
                        $("#download").attr("onclick","download('"+fileDownloadPath+"')");
                        $("#author").text(author.realName);
                        $(".lastTime").text(lastTime);
                        $("#downloadTimes").text(res.data.resource.downloadNum);
                        $("#fileName").text(res.data.resource.fileName);
                        $("#fileType").text(res.data.resource.fileType+"（"+res.data.resource.fileFullType+"）");
                        $("#fileSize").text(parseFloat(parseInt(res.data.resource.fileSize/(1024*1024)))+"M");
                        $("#resourceType").text(res.data.resource.type);
                        renderViewer();
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
/**
 * 下载文件
 * @param url 例如：/v1/nonpub/download/3ab44b5afb83487caea2272984c59f96/2018/11/27/1543327807299.pdf
 */
function download(url) {
    var token = $.cookie(USER_DIGEST_COOKIE);
    if(token == null){
        systemAlert("red","请登录后下载");
    }else{
        downloadFile = false;   // 开启页面保护，防止用户关闭页面
        var fileName = url.substring(url.lastIndexOf(".")-13,url.length);
        var xhr = new XMLHttpRequest();
        xhr.open('GET', url, true);    // 也可以使用POST方式，根据接口
        xhr.responseType = "blob";  // 返回类型blob
        xhr.setRequestHeader("token",token);
        xhr.setRequestHeader("Download-File-Digest",tempFileId);
        $(".progress").fadeIn("fast");
        //监听进度事件
        xhr.addEventListener("progress", function (evt) {
            var percent = (evt.loaded/evt.total)*100;
            $("#progressBar").css("width",percent+"%");
        }, false);

        // 定义请求完成的处理函数，请求前也可以增加加载框/禁用下载按钮逻辑
        $("#download").attr("disabled",true);
        xhr.onload = function () {
            // 请求完成
            if (this.status === 200) {
                downloadFile = true;
                // 返回200
                var blob = this.response;
                // downFile(blob,fileName);
                saveAs(blob,"E resource-"+fileName);
                $(".progress").fadeOut("fast");
                $("#progressBar").css("width","0%");
                $("#download").attr("disabled",false);
            }
        };
        // 发送ajax请求
        xhr.send()
    }
}

/**
 * js下载文件流（兼容性不好）
 * @param blob
 * @param fileName
 */
function downFile(blob, fileName) {
    if (window.navigator.msSaveOrOpenBlob) {
        navigator.msSaveBlob(blob, fileName);
    } else {
        var link = document.createElement('a');
        link.href = window.URL.createObjectURL(blob);
        link.download = fileName;
        link.click();
        window.URL.revokeObjectURL(link.href);
    }
}