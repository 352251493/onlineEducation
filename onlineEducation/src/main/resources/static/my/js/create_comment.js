/**
 * Created by 郭欣光 on 2019/5/20.
 */

function openCreateCommentModel() {
    $("#createCommentModel").modal({
        backdrop : 'static'
    });
}

function publishComment() {
    $("#createCommentModel").modal('hide');
    var discussId = $("#createCommentDiscussId").val();
    var commentContent = $("#commentContent").val();
    if (stringIsEmpty(discussId)) {
        openAlertModel("系统未找到讨论帖子信息！");
    } else if (stringIsEmpty(commentContent)) {
        openAlertModel("写点什么吧~");
    } else if (commentContent.length > 1000) {
        openAlertModel("评论内容不能超过1000字符！");
    } else {
        openLoadingModel();
        var obj = new Object();
        obj.discussId = discussId;
        obj.commentContent = commentContent;
        $.ajax({
            url: "/discuss/comment/create",
            type: "POST",
            cache: false,//设置不缓存
            data: obj,
            success: publishCommentSuccess,
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                closeLoadingModel();
                openAjaxErrorAlert(XMLHttpRequest, textStatus, errorThrown);
            }
        });
    }
}

function publishCommentSuccess(data) {
    var result = JSON.parse(data);
    if (result.status == "true") {
        window.location.reload();
    } else {
        closeLoadingModel();
        openAlertModel(result.content);
    }
}