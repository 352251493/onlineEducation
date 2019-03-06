/**
 * Created by 郭欣光 on 2019/3/6.
 */

var deleteLessonSuccessPath = "";

function openDeleteLesson(lessonId, courseId) {
    deleteLessonSuccessPath = "/course/my/detail/" + courseId + "/1";
    if (stringIsEmpty(lessonId)) {
        openAlertModel("系统未找到课时信息");
    } else {
        $("#deleteLessonId").val(lessonId);
        $("#deleteLessonModel").modal({
            backdrop : 'static'
        });
    }
}

function deleteLesson() {
    var lessonId = $("#deleteLessonId").val();
    if (stringIsEmpty(lessonId)) {
        openAlertModel("系统未找到课时信息");
    } else {
        openLoadingModel();
        var obj = new Object();
        obj.lessonId = lessonId;
        $.ajax({
            url: "/lesson/delete",
            type: "POST",
            cache: false,//设置不缓存
            data: obj,
            success: deleteLessonSuccess,
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                closeLoadingModel();
                openAjaxErrorAlert(XMLHttpRequest, textStatus, errorThrown);
            }
        });
    }
}

function deleteLessonSuccess(data) {
    var result = JSON.parse(data);
    closeLoadingModel();
    if (result.status = "true") {
        $("#myLessonDataListModel").modal('hide');
        openAlertModel(result.content);
        window.location.href = deleteLessonSuccessPath;
    }
    openAlertModel(result.content);
}