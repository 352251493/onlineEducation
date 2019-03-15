/**
 * Created by 郭欣光 on 2019/3/14.
 */

function openCourseDetail(courseId) {
    window.location.href = "/course/my/detail/" + courseId + "/1";
}

function openAddUserStudyModel() {
    $("#addUserStudyModel").modal({
        backdrop : 'static'
    });
}


function addUserStudy() {
    var courseId = $("#addUserStudyCourseId").val();
    var userStudyEmail = $("#userStudy").val();
    if (stringIsEmpty(courseId)) {
        openAlertModel("系统未找到课程信息！");
    } else if (stringIsEmpty(userStudyEmail)) {
        openAlertModel("请输入学习人员邮箱！");
    } else {
        openLoadingModel();
        var obj = new Object();
        obj.courseId = courseId;
        obj.userStudyEmail = userStudyEmail;
        $.ajax({
            url: "/study/user/add",
            type: "POST",
            cache: false,//设置不缓存
            data: obj,
            success: addUserStudySuccess,
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                closeLoadingModel();
                openAjaxErrorAlert(XMLHttpRequest, textStatus, errorThrown);
            }
        });
    }
}

function addUserStudySuccess(data) {
    var result = JSON.parse(data);
    if (result.status == "true") {
        window.location.reload();
    } else {
        closeLoadingModel();
        openAlertModel(result.content);
    }
}