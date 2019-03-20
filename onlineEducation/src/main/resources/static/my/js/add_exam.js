/**
 * Created by 郭欣光 on 2019/3/19.
 */

function openAddExamModel() {
    $("#addExamModel").modal({
        backdrop : 'static'
    });
}

$(document).ready(function () {
    var myDate = new Date();
    $('.form_datetime').datetimepicker({
        language:  'zh_CN',
        // weekStart: 1,
        todayBtn:  1,
        autoclose: 1,
        todayHighlight: 1
        // startView: 2,
        // forceParse: 0,
        // showMeridian: 1
    });
});

function clearTime(id, showId) {
    id = "#" + id;
    $(id).val("");
    showId = "#" + showId;
    $(showId).val("");
}

function addExam() {
    $("#addExamModel").modal('hide');
    var courseId = $("#addExamCourseId").val();
    var examName = $("#examName").val();
    var examRequirement = $("#examRequirement").val();
    var examStartTime = $("#examStartTime").val();
    var examEndTime = $("#examEndTime").val();
    var examDuration = $("#examDuration").val();
    if (stringIsEmpty(courseId)) {
        openAlertModel("系统未找到课程信息！");
    } else if (stringIsEmpty(examName)) {
        openAlertModel("考试名称不能为空！");
    } else if (examName.length > 100) {
        openAlertModel("考试名称不能超过100字符！");
    } else if (stringIsEmpty(examRequirement)) {
        openAlertModel("考试要求不能为空！");
    } else if (examRequirement.length > 1000) {
        openAlertModel("考试要求不能超过1000字符！");
    } else {
        if (!stringIsEmpty(examEndTime)) {
            var endTime = new Date(Date.parse(examEndTime));
            if (!stringIsEmpty(examStartTime)) {
                var startTime = new Date(Date.parse(examStartTime));
                if (startTime > endTime) {
                    openAlertModel("开始时间不能超过结束时间！");
                    return;
                }
            } else {
                var curTime = new Date();
                if (curTime > endTime) {
                    openAlertModel("结束时间小于当前时间！");
                    return;
                }
            }
        }
        if (!stringIsEmpty(examDuration)) {
            var testNumber = parseInt(examDuration).toString();
            if (testNumber == "NaN") {
                openAlertModel("考试时长必须是整数");
                return;
            }
        }
        alert("成功！")
    }
}