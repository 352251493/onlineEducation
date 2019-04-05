/**
 * Created by 郭欣光 on 2019/4/5.
 */

function openAddObjectiveQuestionModel(exam) {
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
        $("#addObjectiveQuestionModel").modal({
            backdrop : 'static'
        });
    }
}

function addObjectiveQuestion() {
    var examId = $("#addObjectiveQuestionExamId").val();
    var subject = $("#addObjectiveQuestionExamSubject").val();
    var score = $("#addObjectiveQuestionExamScore").val();
    $("#addObjectiveQuestionModel").modal('hide');
    if (stringIsEmpty(examId)) {
        openAlertModel("系统获取考试信息失败！");
    } else if (stringIsEmpty(subject)) {
        openAlertModel("题目不可以为空！")
    } else if (subject.length > 1000) {
        openAlertModel("题目长度不能超过1000字符！");
    } else if (stringIsEmpty(score)) {
        openAlertModel("请输入分值");
    } else {
        openLoadingModel();
        var obj = new Object();
        obj.examId = examId;
        obj.subject = subject;
        obj.score = score;
        $.ajax({
            url: "/exam/add/objective",
            type: "POST",
            cache: false,//设置不缓存
            data: obj,
            success: addObjectiveQuestionSuccess,
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                closeLoadingModel();
                openAjaxErrorAlert(XMLHttpRequest, textStatus, errorThrown);
            }
        });
    }
}

function addObjectiveQuestionSuccess(data) {
    var result = JSON.parse(data);
    if (result.status = "true") {
        closeLoadingModel();
        openAlertModel(result.content);
    } else {
        closeLoadingModel();
        openAlertModel(result.content);
    }
}
