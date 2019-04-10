var verifyCodeKey;
$(function () {
    loadVerifyCode();
})

/**
 * 检查表单
 */
function checkRegisterForm() {
    var errorClass = "form-control is-valid";
    var formData = $("#registerForm").serializeJson();
    if(formData.account == ""){
        $("input[name='account']").addClass(errorClass);
        return ;
    } else {
        $("input[name='account']").removeClass(errorClass);
    }
    if(formData.password == ""){
        $("input[name='password']").addClass(errorClass);
        return ;
    }else{
        $("input[name='password']").removeClass(errorClass);
    }
    if(formData.password != formData.password_again){
        systemAlert("red","两次密码不一致！");
        return ;
    }

    if(formData.verifyCode == "" ){
        $("input[name='verifyCode']").addClass(errorClass);
        return ;
    }else{
        $("input[name='verifyCode']").removeClass(errorClass);
    }
    if(formData.account != "" &&
        formData.password != "" &&
        formData.verifyCode != "" &&
        formData.password == formData.password_again){
        register(formData);
    }
}

/**
 * 加载验证码
 */
function loadVerifyCode() {
    var url = HOST+COMMON.PUB_PREFIX+API.CREDENTIAL+"/get_verify_code";
    var xhr = new XMLHttpRequest();
    $("#verifyCodeImg").empty();
    xhr.open("get", url, true);
    xhr.responseType = "blob";
    xhr.onload = function() {
        if (this.status == 200) {
            var blob = this.response;
            var img = document.createElement("img");
            img.setAttribute("onclick","loadVerifyCode()");
            img.onload = function(e) {
                window.URL.revokeObjectURL(img.src);
            };
            img.src = window.URL.createObjectURL(blob);
            $("#verifyCodeImg").html(img);
            verifyCodeKey = xhr.getResponseHeader("Verify-Code-Key");
        }
    }
    xhr.send();
}

/**
 * 注册
 * @param data
 */
function register(data) {
    $.ajax({
        url:HOST+COMMON.PUB_PREFIX+API.USER+"/register",
        type:"post",
        data:{
            account:data.account,
            password:data.password,
            credential_key:verifyCodeKey,
            credential_value:data.verifyCode
        },
        dataType:"json",
        success:function (res) {
            console.log(res);
            switch (res.status) {
                case -1:
                    systemAlert("red","系统出错！");
                    break;
                case 10004:
                    systemAlert("red","用户名或密码错误！");
                    break;
                case 100013:
                    systemAlert("red","验证码错误！");
                    break;
                case 100014:
                    systemAlert("red","验证码已失效，请点击图片重新获取！");
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
                case 100018:
                    systemAlert("red","该账户已存在，换一个试试？");
                    break;
                case 0:
                    autoCloseAlert("green","注册成功！3秒后自动跳转",3000,function () {
                        $.cookie(USER_DIGEST_COOKIE,res.data);
                        var client_url = getUrlData().client_url;
                        if(client_url == null || client_url == ""){
                            location.href = "index.html";
                        }else{
                            location.href = urlDecode(client_url);
                        }
                    });
                    break;
            }
        },
        error:function (res) {
            console.log(res.status);
        }
    })
}