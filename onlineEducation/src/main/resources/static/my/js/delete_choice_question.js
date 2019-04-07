/**
 * Created by 郭欣光 on 2019/4/7.
 */

function openDeleteChoiceQuestionConfirmModel(choiceQuestionId) {
    $("#deleteChoiceQuestionId").val(choiceQuestionId);
    $("#deleteChoiceQuestionConfirmModel").modal({
        backdrop : 'static'
    });
}

function deleteChoiceQuestion() {
    $("#deleteChoiceQuestionConfirmModel").modal('hide');
    var choiceQuestionId = $("#deleteChoiceQuestionId").val();
    if (choiceQuestionId == null) {
        openAlertModel("系统获取选择题信息失败！");
    } else {
        openLoadingModel();
        var obj = new Object();
        obj.choiceQuestionId = choiceQuestionId;
        $.ajax({
            url: "/exam/choice/delete",
            type: "POST",
            cache: false,//设置不缓存
            data: obj,
            success: deleteChoiceQuestionSuccess,
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                closeLoadingModel();
                openAjaxErrorAlert(XMLHttpRequest, textStatus, errorThrown);
            }
        });
    }
}

function deleteChoiceQuestionSuccess(data) {
    var result = JSON.parse(data);
    if (result.status == "true") {
        window.location.reload();
    } else {
        closeLoadingModel();
        openAlertModel(result.content);
    }
}