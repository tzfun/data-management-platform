var tags = {};
var editor;
var uploadArticle = false;
var isCreateNew = true; // 是否是创建新文章
var tempArticleId = ""; // 当前文章id
var articleContent = "";// 当前文章内容
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
    }
    var articleId = getUrlParam("id");
    if (articleId != null){
        renderArticleInfo(articleId);
    }

    /**
     * 按钮失去焦点
     */
    $("#addTagsForm").focusout(function () {
        $("#addTagsForm").css({
            animation: "addSportsHide ease 1s",
            opacity:0,
            width:0
        });
    })
    /**
     * 按钮获取焦点
     */
    $("#addTagsForm").keydown(function (e) {
        var inputValue = $("#addTagsForm").val().trim();
        if(e.keyCode == 13 && inputValue!=""){
            var tagId = new Date().getTime();
            tags[tagId] = inputValue;
            $(".tagContainer").append(
                "<span class=\"bf-tags\" data-tagID='"+tagId+"'>" +
                "<i class=\"fa fa-times-circle tagClose\" aria-hidden=\"true\" title=\"删除\" onclick=\"deleteTag(this)\"></i>"
                + inputValue +
                "</span>");
            $("#addTagsForm").val("");
        }
    })
}

/**
 * saveArticle 保存文章
 */
function saveArticle() {
    var formData = $("#articleForm").serializeJson();
    var tagsArray = [];
    for (var tag in tags) {
        tagsArray.push(tags[tag]);
    }
    formData.tags = tagsArray;  // 添加自定义标签
    formData.content = editor.getData();    // 获取富文本编辑器内容
    console.log(formData);
    if(formData.title.trim() == ""){
        systemAlert("red","文章标题不能为空")
    }else if(formData.summary.trim() == ""){
        systemAlert("red","文章摘要不能为空")
    }else{
        if (isCreateNew) {
            systemConfirm("确定上传文章？",function () {
                uploadArticles(formData);
            })
        }else{
            systemConfirm("确定对此文章进行修改？",function () {
                updateArticles(formData);
            })
        }

    }
}

/**
 * 更新文章
 * @param data
 */
function updateArticles(data) {
    data.id = tempArticleId;
    data.content = strFilter(data.content);
    $.ajax({
        url:HOST + COMMON.NONPUBLIC_PREFIX + API.ARTICLE + "/update",
        type:"put",
        headers:{
            token:$.cookie(USER_DIGEST_COOKIE),
            "Content-Type": "application/json;charset=UTF-8"
        },
        data:JSON.stringify(data),
        dataType:"json",
        async:false,
        success:function (res) {
            console.log(res);
            switch (res.status) {
                case 0:
                    uploadArticle = true;
                    autoCloseAlert("green","更新成功！3秒后自动跳转",3000,function () {
                        location.href = "/article.html?id="+tempArticleId;
                    });
                    break;
                case -1:
                    systemAlert("red","系统错误："+res.data);
                    break;
                case 10006:
                    systemAlert("red","您暂无修改文章的权限!！如需开通权限请联系管理员。");
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
            systemAlert("red","文章上传失败，code："+res.status);
        }
    })
}
/**
 * 上传文章
 */
function uploadArticles(data) {
    data.id = 1;
    data.content = strFilter(data.content);
    $.ajax({
        url:HOST + COMMON.NONPUBLIC_PREFIX + API.ARTICLE + "/upload",
        type:"post",
        headers:{
            token:$.cookie(USER_DIGEST_COOKIE),
            "Content-Type": "application/json;charset=UTF-8"
        },
        data:JSON.stringify(data),
        dataType:"json",
        async:false,
        success:function (res) {
            console.log(res);
            switch (res.status) {
                case 0:
                    uploadArticle = true;
                    autoCloseAlert("green","上传成功！3秒后自动跳转",3000,function () {
                        location.href = "/article.html?id="+res.data;
                    });
                    break;
                case -1:
                    systemAlert("red","上传失败，错误信息："+res.data);
                    break;
                case 10006:
                    systemAlert("red","您暂无撰写文章的权限!！如需开通权限请联系管理员。");
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
            systemAlert("red","文章上传失败，code："+res.status);
        }
    })
}

/**
 * 显示新增表单
 */
function showAddTagsForm() {
    if($("#addTagsForm")[0].offsetWidth>100){
        $("#addTagsForm").css({
            animation: "addSportsHide ease 1s",
            opacity:0,
            width:0
        });
    }else{
        $("#addTagsForm").css({
            animation: "addSportsShow ease 1s",
            opacity:1,
            width:"200px"
        });
        $("#addTagsForm").focus();
    }
}

/**
 * 删除标签
 * @param e Dom对象
 */
function deleteTag(e) {
    var delTagID = e.parentNode.getAttribute("data-tagID");
    delete tags[delTagID];
    e.parentElement.remove();
}

/**
 * 渲染文章信息
 * @param articleId
 */
function renderArticleInfo(articleId) {
    $.ajax({
        url: HOST + COMMON.PUB_PREFIX + API.ARTICLE + "/get",
        type:"get",
        data:{
            id:articleId
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
                    // 设置当前文章id
                    tempArticleId = res.data.id;
                    // 设置当前操作状态
                    isCreateNew = false;
                    // 将文章内容重新添加到页面
                    $("input[name='title']").val(res.data.title);
                    $("input[name='summary']").val(res.data.summary);
                    if (res.data.strTags != null){
                        var tagList = res.data.strTags.split(",");
                        tags = {};
                        for (var i in tagList) {
                            var tagId = new Date().getTime() + Math.random() * 1000000;
                            tags[tagId] = tagList[i];
                            $(".tagContainer").append(
                                "<span class=\"bf-tags\" data-tagID='"+tagId+"'>" +
                                "<i class=\"fa fa-times-circle tagClose\" aria-hidden=\"true\" title=\"删除\" onclick=\"deleteTag(this)\"></i>"
                                + tagList[i] +
                                "</span>");
                        }
                    }
                    articleContent = labelDecode(res.data.content);
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
        editor.setData(articleContent);
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