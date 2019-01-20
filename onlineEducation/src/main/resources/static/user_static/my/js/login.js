/**
 * Created by 郭欣光 on 2019/1/20.
 */

function login () {
    var email = $("#email").val();
    var password = $("#password").val();
    var nextPage = $("#nextPage").val();
    if (stringIsEmpty(email)) {
        openAlertModel("请输入邮箱！");
    } else if (email.length > 150) {
        openAlertModel("邮箱长度不能大于150个字符！");
    } else if (!isEmail(email)) {
        openAlertModel("邮箱格式不正确！");
    } else if (stringIsEmpty(password)) {
        openAlertModel("请输入密码！");
    } else if (password.length > 20) {
        openAlertModel("密码长度不能超过20个字符！");
    } else {
        if (stringIsEmpty(nextPage)) {
            nextPage = "/";
        }
        openLoadingModel();
        var obj = new Object();
        obj.email = email;
        obj.password = password;
        obj.nextPage = nextPage;
        $.ajax({
            url: "/user/login",
            type: "POST",
            cache: false,//设置不缓存
            data: obj,
            success: loginSuccess,
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                closeLoadingModel();
                openAjaxErrorAlert(XMLHttpRequest, textStatus, errorThrown);
            }
        });
    }
}

function loginSuccess(data) {
    var result = JSON.parse(data);
    closeLoadingModel();
    if (result.status == "true") {
        window.location.href = result.content;
    } else {
        openAlertModel(result.content);
    }
}