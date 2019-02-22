/**
 * Created by 郭欣光 on 2019/2/19.
 */

function openEditCourseModel() {
    $("#editCourseModel").modal({
        backdrop : 'static'
    });
}

function closeEditCourseModel() {
    setTimeout(function () {
        $("#editCourseModel").modal('hide');
    }, 500);
}

function editCourse(courseId) {
    var courseName =  $("#courseName").val();
    var courseIntroduction = $("#courseIntroduction").val();
    if (stringIsEmpty(courseName)) {
        openAlertModel("课程名称不能为空！");
    } else if (courseName.length > 100) {
        openAlertModel("课程名称长度不能超过100字符！");
    } else if (stringIsEmpty(courseIntroduction)) {
        openAlertModel("课程简介不能为空！");
    } else if (courseIntroduction.length > 500) {
        openAlertModel("课程简介长度不能超过500字符！");
    } else {
        closeEditCourseModel();
        openLoadingModel();
        var obj = new Object();
        obj.courseId = courseId;
        obj.courseName = courseName;
        obj.courseIntroduction = courseIntroduction;
        $.ajax({
            url: "/course/edit",
            type: "POST",
            cache: false,//设置不缓存
            data: obj,
            success: editCourseSuccess,
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                closeLoadingModel();
                openEditCourseModel();
                openAjaxErrorAlert(XMLHttpRequest, textStatus, errorThrown);
            }
        });
    }
}

function editCourseSuccess(data) {
    var result = JSON.parse(data);
    if (result.status == "true") {
        window.location.reload();
    } else {
        closeLoadingModel();
        openEditCourseModel();
        openAlertModel(result.content);
    }
}

function openEditCourseImageModel() {
    $("#editCourseImageModel").modal({
        backdrop : 'static'
    });
}

function closeEditCourseImageModel() {
    $("#editCourseImageModel").modal('hide');
}

function editCourseImage() {
    var courseId = $("#courseId").val();
    var courseImage =  document.getElementById('courseImage').files[0];
    var courseImagePath = $('#courseImage').val().toLowerCase().split(".");
    var courseImageType =  courseImagePath[courseImagePath.length - 1];
    if (stringIsEmpty(courseId)) {
        openAlertModel("系统获取课程信息失败！");
    } else if (courseImage == null) {
        openAlertModel("为该课程选一个好看的封面吧~");
    } else if (!isImage(courseImageType)) {
        openAlertModel("课程封面必须是图片类型哦~");
    } else if (courseImage.size > COURSE_IMAGE_MAX_SIZE) {
        openAlertModel("图片太大啦，不要超过" + COURSE_IMAGE_MAX_SIZE_STRING + "哦~");
    } else {
        closeEditCourseImageModel();
        openLoadingModel();
        var formData = new FormData($('#editCourseImageForm')[0]);
        $.ajax({
            type: "POST",
            url: "/course/edit/image",
            // async: false,//true表示同步，false表示异步
            cache: false,//设置不缓存
            data: formData,
            contentType: false,//必须设置false才会自动加上正确的Content-Tyoe
            processData: false,//必须设置false才避开jquery对formdata的默认处理，XMLHttpRequest会对formdata进行正确的处理
            success: editCourseImageSuccess,
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                closeLoadingModel();
                openEditCourseImageModel();
                openAjaxErrorAlert(XMLHttpRequest, textStatus, errorThrown);
            }
        });
    }
}

function editCourseImageSuccess(data) {
    var result = JSON.parse(data);
    if (result.status = "true") {
        window.location.reload();
    } else {
        closeLoadingModel();
        openEditCourseImageModel();
        openAlertModel(result.content);
    }
}

function openMyCourseDetailPage(courseId, lessonPage) {
    window.location.href = "/course/my/detail/" + courseId + "/" + lessonPage;
}

function openCreateLessonPage(courseId) {
    window.location.href = "/lesson/create/" + courseId;
}

function openMyLessonPage(lessonId) {
    window.location.href = "/lesson/my/detail/" + lessonId;
}