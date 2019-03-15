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
