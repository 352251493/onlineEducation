/**
 * Created by 郭欣光 on 2019/4/7.
 */

function openDeleteChoiceQuestionConfirmModel(exam, choiceQuestionId) {
    var isAdd = true;
    if (exam.startTime != null) {
        var startTime = exam.startTime.split(".")[0].split("T")[0] + " " + exam.startTime.split(".")[0].split("T")[1];
        var curDate = new Date();
        var curDateString = curDate.getFullYear() + "-" + (curDate.getMonth() + 1) + "-" + curDate.getDate() + " " + curDate.getHours() + ":" + curDate.getMinutes() + ":" + curDate.getSeconds();
        var startTimeDate = new Date(startTime.replace("-","/"));
        var curDateStringDate = new Date(curDateString.replace("-","/"));
        if (curDateStringDate > startTimeDate) {
            isAdd = false;
            openAlertModel("考试已经开始，无法修改试题！");
        }
    }
    if (isAdd) {
        if (exam.endTime != null) {
            var endTime = exam.endTime.split(".")[0].split("T")[0] + " " + exam.endTime.split(".")[0].split("T")[1];
            var endTimeDate = new Date(endTime.replace("-","/"));
            var curDate = new Date();
            var curDateString = curDate.getFullYear() + "-" + (curDate.getMonth() + 1) + "-" + curDate.getDate() + " " + curDate.getHours() + ":" + curDate.getMinutes() + ":" + curDate.getSeconds();
            var curDateStringDate = new Date(curDateString.replace("-","/"));
            if (curDateStringDate > endTimeDate) {
                isAdd = false;
                openAlertModel("考试已经结束，无法修改试题！");
            }
        }
    }
    if (isAdd) {
        $("#deleteChoiceQuestionId").val(choiceQuestionId);
        $("#deleteChoiceQuestionConfirmModel").modal({
            backdrop : 'static'
        });
    }
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