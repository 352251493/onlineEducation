/**
 * Created by 郭欣光 on 2019/1/28.
 */

var deleteMessageId = "";

function openDeleteMessageConfirmModel(messageId) {
    deleteMessageId = messageId;
    $("#deleteMessageConfirmModel").modal({
        backdrop : 'static'
    });
}

function deleteMessage() {
    closeDeleteMessageConfirmModel();
    if (stringIsEmpty(deleteMessageId)) {
        openAlertModel("系统获取消息ID出错！");
    } else {
        openLoadingModel();
        var obj = new Object();
        obj.messageId=deleteMessageId;
        $.ajax({
            url: "/user/message/delete",
            type: "POST",
            cache: false,//设置不缓存
            data: obj,
            success: deleteMessageSuccess,
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                closeLoadingModel();
                openAjaxErrorAlert(XMLHttpRequest, textStatus, errorThrown);
            }
        });
    }
}

function deleteMessageSuccess(data) {
    var result = JSON.parse(data);
    if (result.status == "true") {
        if (window.location.href.indexOf("/message/detail/") != -1) {
            history.go(-1);
        } else {
            window.location.reload();
        }
    } else {
        closeLoadingModel();
        openAlertModel(result.content);
    }
}

function closeDeleteMessageConfirmModel() {
    $("#deleteMessageConfirmModelClose").click();
}
