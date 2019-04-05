/**
 * Created by 郭欣光 on 2019/4/4.
 */

function openAddChoiceQuestionModel(exam) {
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
        $("#addChoiceQuestionModel").modal({
            backdrop : 'static'
        });
    }
}

function addChoiceQuestion() {
    var examId = $("#addChoiceQuestionExamId").val();
    var subject = $("#examSubject").val();
    var optionA = $("#examOptionA").val();
    var optionB = $("#examOptionB").val();
    var optionC = $("#examOptionC").val();
    var optionD = $("#examOptionD").val();
    var answer = $("#examAnswer").val();
    var score = $("#examScore").val();
    $("#addChoiceQuestionModel").modal('hide');
    if (stringIsEmpty(examId)) {
        openAlertModel("系统获取考试信息失败！");
    } else if (stringIsEmpty(subject)) {
        openAlertModel("题目不可以为空！")
    } else if (subject.length > 1000) {
        openAlertModel("题目长度不能超过1000字符！");
    } else if (stringIsEmpty(optionA)) {
        openAlertModel("选项A不能为空！");
    } else if (optionA.length > 1000) {
        openAlertModel("选项A长度不能超过1000字符！");
    } else if (stringIsEmpty(optionB)) {
        openAlertModel("选项B不能为空！");
    } else if (optionB.length > 1000) {
        openAlertModel("选项B长度不能超过1000字符！");
    } else if (stringIsEmpty(optionC)) {
        openAlertModel("选项C不能为空！");
    } else if (optionC.length > 1000) {
        openAlertModel("选项C长度不能超过1000字符！");
    } else if (stringIsEmpty(optionD)) {
        openAlertModel("选项D不能为空！");
    } else if (optionD.length > 1000) {
        openAlertModel("选项D长度不能超过1000字符！");
    } else if (stringIsEmpty(answer)) {
        openAlertModel("答案不能为空！");
    } else if (stringIsEmpty(score)) {
        openAlertModel("请输入分值");
    } else {
        answer = answer.trim().toUpperCase();
        if (answer == "A" || answer == "B" || answer == "C" || answer == "D") {
            openLoadingModel();
            var obj = new Object();
            obj.examId = examId;
            obj.subject = subject;
            obj.optionA = optionA;
            obj.optionB = optionB;
            obj.optionC = optionC;
            obj.optionD = optionD;
            obj.answer = answer;
            obj.score = score;
            $.ajax({
                url: "/exam/add/choice",
                type: "POST",
                cache: false,//设置不缓存
                data: obj,
                success: addChoiceQuestionSuccess,
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    closeLoadingModel();
                    openAjaxErrorAlert(XMLHttpRequest, textStatus, errorThrown);
                }
            });
        } else {
            openAlertModel("答案只能写A/B/C/D");
        }
    }
}

function addChoiceQuestionSuccess(data) {
    var result = JSON.parse(data);
    if (result.status = "true") {
        closeLoadingModel();
        openAlertModel(result.content);
    } else {
        closeLoadingModel();
        openAlertModel(result.content);
    }
}
