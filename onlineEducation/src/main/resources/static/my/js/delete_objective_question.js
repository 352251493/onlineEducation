/**
 * Created by 郭欣光 on 2019/4/7.
 */

function openDeleteObjectiveQuestionConfirmModel(objectiveQuestionId) {
    $("#deleteObjectiveQuestionId").val(objectiveQuestionId);
    $("#deleteObjectiveQuestionConfirmModel").modal({
        backdrop : 'static'
    });
}

function deleteObjectiveQuestion() {
    $("#deleteObjectiveQuestionConfirmModel").modal('hide');
    var objectiveQuestionId = $("#deleteObjectiveQuestionId").val();
    if (objectiveQuestionId == null) {
        openAlertModel("系统获取客观题题信息失败！");
    } else {
        openLoadingModel();
        var obj = new Object();
        obj.objectiveQuestionId = objectiveQuestionId;
        $.ajax({
            url: "/exam/objective/delete",
            type: "POST",
            cache: false,//设置不缓存
            data: obj,
            success: deleteObjectiveQuestionSuccess,
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                closeLoadingModel();
                openAjaxErrorAlert(XMLHttpRequest, textStatus, errorThrown);
            }
        });
    }
}

function deleteObjectiveQuestionSuccess(data) {
    var result = JSON.parse(data);
    if (result.status == "true") {
        window.location.reload();
    } else {
        closeLoadingModel();
        openAlertModel(result.content);
    }
}