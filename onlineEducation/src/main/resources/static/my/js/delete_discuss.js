/**
 * Created by 郭欣光 on 2019/5/20.
 */

function openDeleteDiscussConfirmModel(discussId) {
    if (stringIsEmpty(discussId)) {
        openAlertModel("系统未找到讨论帖子信息");
    } else {
        $("#deleteDiscussDiscusstId").val(discussId);
        $("#deleteDiscussConfirmModel").modal({
            backdrop : 'static'
        });
    }
}

function deleteDiscuss() {
    $("#deleteDiscussConfirmModel").modal('hide');
    var discussId = $("#deleteDiscussDiscusstId").val();
    if (stringIsEmpty(discussId)) {
        openAlertModel("系统未找到讨论帖子信息");
    } else {
        openLoadingModel();
        var obj = new Object();
        obj.discussId = discussId;
        $.ajax({
            url: "/discuss/delete",
            type: "POST",
            cache: false,//设置不缓存
            data: obj,
            success: deleteDiscussSuccess,
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                closeLoadingModel();
                openAjaxErrorAlert(XMLHttpRequest, textStatus, errorThrown);
            }
        });
    }
}

function deleteDiscussSuccess(data) {
    var result = JSON.parse(data);
    if (result.status = "true") {
        if (window.location.href.indexOf("detail") != -1) {
            var prePageUrl = document.referrer;
            if (prePageUrl == "") {
                window.location.href = "/discuss/1";
            } else if (prePageUrl.indexOf("login") != -1) {
                window.location.href = "/discuss/1";
            } else if (prePageUrl.indexOf("discuss") == -1) {
                window.location.href = "/discuss/1";
            } else {
                window.location.href = prePageUrl;
            }
        } else {
            window.location.reload();
        }
    } else {
        closeLoadingModel();
        openAlertModel(result.content);
    }
}