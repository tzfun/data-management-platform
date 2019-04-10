var uploadSource = false;
$(function () {
    init();

})
/**
 * 监听页面变化
 */
window.onbeforeunload = function(e) {
    if(!uploadSource){
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
        renderPage();
    }
    bs_input_file();
    // 开启tooltip提示
    $('[data-toggle="tooltip"]').tooltip()
    $("#sourceFile").change(function (e) {
        var selectedFile = $('#sourceFile').get(0).files[0];
        if(selectedFile == null){
            $("#fileType").text("");
            $("#fileSize").text("");
            $("#fileLastChange").text("");
        }else{
            var fileInfo = getFileInfo(selectedFile);
            $("#fileType").text(fileInfo.fileType);
            $("#fileSize").text(fileInfo.fileSize);
            $("#fileLastChange").text(fileInfo.lastChange);
        }
    })
    $("#chooseImg").click(function () {
        $("#imgFile").click();
    })
    $("#imgFile").change(function (e) {
        var selectedFile = $('#imgFile').get(0).files[0];
        if(selectedFile == null){
            $("#chooseImg").attr("src","../images/img-icon.png");
        }else{
            var fileInfo = getFileInfo(selectedFile);
            console.log(fileInfo);
            var tempUrl;
            var reader = new FileReader();
            reader.readAsDataURL(selectedFile);
            reader.onload=function(e){
                tempUrl=e.target.result;
                $("#chooseImg").attr("src",tempUrl);
            };
        }
    })
}

/**
 * 获取文件信息
 * @param file
 */
function getFileInfo(file) {
    var fileInfo = {};
    var d=new Date(file.lastModifiedDate);
    //获取最后一个.的位置
    var index= file.name.lastIndexOf(".");
    if(index == -1){
        fileInfo.fileType = "类型错误";
    }else{
        //获取后缀
        fileInfo.fileType = file.name.substr(index+1);
    }
    fileInfo.lastChange=d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate() + ' ' + d.getHours() + ':' + d.getMinutes();
    fileInfo.fileSize = (file.size/(1024*1024)).toFixed(2)   // 单位M
    fileInfo.fileName = file.name;
    return fileInfo;
}
/**
 * 上传资源
 */
function checkResource() {
    var resourceForm = $("#resourceForm").serializeJson();
    var file = $('#sourceFile').get(0).files[0];
    var img = $('#imgFile').get(0).files[0];
    if(img == null){
        systemAlert("red","请为资源文件选择一张封面图片")
    }else if (resourceForm.title.trim() == "") {
        systemAlert("red","资源标题不能为空")
    }else if(resourceForm.summary.trim() == ""){
        systemAlert("red","资源摘要不能为空")
    }else if(resourceForm.type.trim() == ""){
        systemAlert("red","资源类型不能为空")
    }else if(file == null){
        systemAlert("red","请选择资源文件")
    }else{
        systemConfirm("确定上传该资源吗？",function () {
            var formData = new FormData();
            formData.append("file",file);
            formData.append("img",img);
            formData.append("title",resourceForm.title);
            formData.append("summary",resourceForm.summary);
            formData.append("type",resourceForm.type);
            formData.append("introduction",editor.getData());
            uploadResource(formData);
        })
    }
}

/**
 * 将文件上传至服务器
 * @param formData
 */
function uploadResource(formData) {
    if($.cookie(USER_DIGEST_COOKIE) == null){
        systemConfirm("您未登录，是否前往登录？",function () {
            location.href = "login.html?client_url="+urlEncode(location.href);
        })
    }else{
        $(".progress-model").fadeIn("fast");
        $.ajax({
            type:"post",
            async:true,  //这里要设置异步上传，才能成功调用myXhr.upload.addEventListener('progress',function(e){}),progress的回掉函数
            data:formData,
            headers:{
                token:$.cookie(USER_DIGEST_COOKIE)
            },
            url: HOST + COMMON.NONPUBLIC_PREFIX + API.RESOURCE + "/upload",
            processData: false, // 告诉jQuery不要去处理发送的数据
            contentType: false, // 告诉jQuery不要去设置Content-Type请求头
            xhr:function(){
                myXhr = $.ajaxSettings.xhr();
                if(myXhr.upload){ // check if upload property exists
                    myXhr.upload.addEventListener('progress',function(e){
                        var loaded = e.loaded;                  //已经上传大小情况
                        var total = e.total;                      //附件总大小
                        var percent = Math.floor(100*loaded/total)+"%";     //已经上传的百分比
                        $("#progressPercent").text(percent);
                        $("#progressBar").css("width",percent);
                    }, false); // for handling the progress of the upload
                }
                return myXhr;
            },
            success:function(res){
                console.log(res);
                switch (res.status) {
                    case -1:
                        systemAlert("red","系统出错！上传失败，"+res.msg);
                        break;
                    case 0:
                        uploadSource = true;    // 修改页面上传状态
                        autoCloseAlert("green","上传成功！3秒后跳转",3000,function () {
                            location.href = "/resource-view.html?id="+res.data;
                        });
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
                    case 10006:
                        systemAlert("red","您暂无权限上传资源文件！如需开通权限请联系管理员。");
                        break;
                    case 10009:
                        systemAlert("red","文件格式错误！");
                        break;
                    case 100011:
                        systemAlert("red","资源文件太大");
                        break;
                }
            },
            error:function(res){
                systemAlert("red","出错啦，code："+res.status);
            },
            complete:function () {
                $(".progress-model").fadeOut("fast");
                $("#progressBar").css("width","0%");
            }
        });
    }
}

/**
 * 上传文件按钮
 */
function bs_input_file() {
    $(".input-file").before(
        function() {
            if ( ! $(this).prev().hasClass('input-ghost') ) {
                var element = $("<input type='file' class='input-ghost' id='sourceFile' style='visibility:hidden; height:0;width: 0'>");
                element.attr("name",$(this).attr("name"));
                element.change(function(){
                    element.next(element).find('input').val((element.val()).split('\\').pop());
                });
                $(this).find("button.btn-choose").click(function(){
                    element.click();
                });
                $(this).find("button.btn-reset").click(function(){
                    element.val(null);
                    $(this).parents(".input-file").find('input').val('');
                });
                $(this).find('input').css("cursor","pointer");
                $(this).find('input').mousedown(function() {
                    $(this).parents('.input-file').prev().click();
                    return false;
                });
                return element;
            }
        }
    );
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