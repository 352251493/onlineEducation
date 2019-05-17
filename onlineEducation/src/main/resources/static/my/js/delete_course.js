/**
 * Created by 郭欣光 on 2019/5/17.
 */

function openDeleteCourseConfirmModel() {
    $("#deleteCourseConfirmModel").modal({
        backdrop : 'static'
    });
}

function deleteCourse() {
    $("#deleteCourseConfirmModel").modal('hide');
    var courseId = $("#courseId").val();
    if (courseId == null) {
        openAlertModel("系统获取课程信息失败！");
    } else {
        openLoadingModel();
        var obj = new Object();
        obj.courseId = courseId;
        $.ajax({
            url: "/course/delete",
            type: "POST",
            cache: false,//设置不缓存
            data: obj,
            success: deleteCourseSuccess,
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                closeLoadingModel();
                openAjaxErrorAlert(XMLHttpRequest, textStatus, errorThrown);
            }
        });
    }
}

function deleteCourseSuccess(data) {
    var result = JSON.parse(data);
    if (result.status == "true") {
        window.location.href = "/user/course/create/1";
    } else {
        closeLoadingModel();
        openAlertModel(result.content);
    }
}