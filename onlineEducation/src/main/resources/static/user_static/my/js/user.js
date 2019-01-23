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
