/**
 * Created by 郭欣光 on 2019/1/23.
 */
function openCancelUserConfirmModel() {
    $("#cancelUserConfirmModel").modal({
        backdrop : 'static'
    });
}

function closeCancelUserConfirmModel() {
    $("#cancelUserConfirmModelClose").click();
}

function cancelUser() {
    closeCancelUserConfirmModel();
    openLoadingModel();
    $.ajax({
        url: "/user/cancel",
        type: "POST",
        cache: false,//设置不缓存
        success: cancelUserSuccess,
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            closeLoadingModel();
            openAjaxErrorAlert(XMLHttpRequest, textStatus, errorThrown);
        }
    });
}

function cancelUserSuccess(data) {
    var result = JSON.parse(data);
    if (result.status = "true") {
        window.location.href = "/";
    } else {
        closeLoadingModel();
        openAlertModel(result.content);
    }
}

function logout() {
    openLoadingModel();
    $.ajax({
        url: "/user/logout",
        type: "POST",
        cache: false,//设置不缓存
        success: logoutSuccess,
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            closeLoadingModel();
            openAjaxErrorAlert(XMLHttpRequest, textStatus, errorThrown);
        }
    });
}

function logoutSuccess(data) {
    var result = JSON.parse(data);
    if (result.status = "true") {
        window.location.href = "/";
    } else {
        closeLoadingModel();
        openAlertModel(result.content);
    }
}

function resetPassword() {
    openLoadingModel();
    $.ajax({
        url: "/user/password/reset",
        type: "POST",
        cache: false,//设置不缓存
        success: resetPasswordSuccess,
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            closeLoadingModel();
            openAjaxErrorAlert(XMLHttpRequest, textStatus, errorThrown);
        }
    });
}

function resetPasswordSuccess(data) {
    var result = JSON.parse(data);
    closeLoadingModel();
    openAlertModel(result.content);
}

function resetPasswordSubmit() {
    var password = $("#password").val();
    var repassword = $("#repassword").val();
    if (stringIsEmpty(password)) {
        openAlertModel("请输入密码！");
    } else if (stringIsEmpty(repassword)) {
        openAlertModel("请确认密码！");
    } else if (password != password) {
        openAlertModel("两次密码不一致！");
    } else {
        openLoadingModel();
        var obj = new Object();
        obj.password = password;
        obj.repassword = repassword;
        $.ajax({
            url: "/user/password/reset/submit",
            type: "POST",
            cache: false,//设置不缓存
            data: obj,
            success: resetPasswordSubmitSuccess,
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                closeLoadingModel();
                openAjaxErrorAlert(XMLHttpRequest, textStatus, errorThrown);
            }
        });
    }
}

function resetPasswordSubmitSuccess(data) {
    var resul = JSON.parse(data);
    if (resul.status == "true") {
        window.location.href = "/";
    } else {
        closeLoadingModel();
        openAlertModel(resul.content);
    }
}