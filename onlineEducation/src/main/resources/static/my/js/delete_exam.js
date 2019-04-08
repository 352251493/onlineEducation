/**
 * Created by 郭欣光 on 2019/4/8.
 */

function openDeleteExamConfirmModel(exam) {
    var isAdd = true;
    if (exam.startTime != null) {
        var startTime = exam.startTime.split(".")[0].split("T")[0] + " " + exam.startTime.split(".")[0].split("T")[1];
        var curDate = new Date();
        var curDateString = curDate.getFullYear() + "-" + (curDate.getMonth() + 1) + "-" + curDate.getDate() + " " + curDate.getHours() + ":" + curDate.getMinutes() + ":" + curDate.getSeconds();
        var startTimeDate = new Date(startTime.replace("-","/"));
        var curDateStringDate = new Date(curDateString.replace("-","/"));
        if (curDateStringDate > startTimeDate) {
            isAdd = false;
            openAlertModel("考试已经开始，无法删除试题！");
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
                openAlertModel("考试已经结束，无法删除试题！");
            }
        }
    }
    if (isAdd) {
        $("#deleteExamId").val(exam.id);
        $("#deleteExamConfirmModel").modal({
            backdrop : 'static'
        });
    }
}

function deleteExam() {
    $("#deleteExamConfirmModel").modal('hide');
    var examId = $("#deleteExamId").val();
    if (examId == null) {
        openAlertModel("系统获取考试信息失败！");
    } else {
        openLoadingModel();
        var obj = new Object();
        obj.examId = examId;
        $.ajax({
            url: "/exam/delete",
            type: "POST",
            cache: false,//设置不缓存
            data: obj,
            success: deleteExamSuccess,
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                closeLoadingModel();
                openAjaxErrorAlert(XMLHttpRequest, textStatus, errorThrown);
            }
        });
    }
}

function deleteExamSuccess(data) {
    var result = JSON.parse(data);
    if (result.status == "true") {
        window.location.href = "/exam/my/list/" + result.content + "/1";
    } else {
        closeLoadingModel();
        openAlertModel(result.content);
    }
}
