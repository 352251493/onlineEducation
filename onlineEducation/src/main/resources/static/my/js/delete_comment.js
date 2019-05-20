/**
 * Created by 郭欣光 on 2019/5/20.
 */

function openDeleteCommentModel(commentId) {
    if (stringIsEmpty(commentId)) {
        openAlertModel("系统未找到评论信息");
    } else {
        $("#deleteCommentCommentId").val(commentId);
        $("#deleteCommentConfirmModel").modal({
            backdrop : 'static'
        });
    }
}

function deleteComment() {
    $("#deleteCommentConfirmModel").modal('hide');
    var commentId = $("#deleteCommentCommentId").val();
    if (stringIsEmpty(commentId)) {
        openAlertModel("系统未找到评论信息");
    } else {
        openLoadingModel();
        var obj = new Object();
        obj.commentId = commentId;
        $.ajax({
            url: "/discuss/comment/delete",
            type: "POST",
            cache: false,//设置不缓存
            data: obj,
            success: deleteCommentSuccess,
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                closeLoadingModel();
                openAjaxErrorAlert(XMLHttpRequest, textStatus, errorThrown);
            }
        });
    }
}

function deleteCommentSuccess(data) {
    var result = JSON.parse(data);
    if (result.status = "true") {
        window.location.reload();
    } else {
        closeLoadingModel();
        openAlertModel(result.content);
    }
}