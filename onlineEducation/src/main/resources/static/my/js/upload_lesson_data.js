/**
 * Created by 郭欣光 on 2019/2/28.
 */

function uploadLessonData() {
    var lessonId = $("#uploadLessonDataLessonId").val();
    var lessonDataFile =  document.getElementById('lessonDataFile').files[0];
    if (stringIsEmpty(lessonId)) {
        openAlertModel("系统获取课时信息失败,请刷新页面后重试");
    } else if (lessonDataFile == null) {
        openAlertModel("请选择文件！");
    } else if (lessonDataFile.size > DATA_MAX_SIZE) {
        openAlertModel("资料大小不能超过" + DATA_MAX_SIZE_STRING);
    } else {
        openLoadingModel();
        var formData = new FormData($('#uploadLessonDataForm')[0]);
        $.ajax({
            type: "POST",
            url: "/data/create",
            // async: false,//true表示同步，false表示异步
            cache: false,//设置不缓存
            data: formData,
            contentType: false,//必须设置false才会自动加上正确的Content-Tyoe
            processData: false,//必须设置false才避开jquery对formdata的默认处理，XMLHttpRequest会对formdata进行正确的处理
            success: uploadLessonDataSuccess,
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                closeLoadingModel();
                openAjaxErrorAlert(XMLHttpRequest, textStatus, errorThrown);
            }
        });
    }
}

function uploadLessonDataSuccess(data) {
    var result = JSON.parse(data);
    closeLoadingModel();
    if (result.status == "true") {
        closeUploadLessonDataModel();
    }
    openAlertModel(result.content);
}

function openUploadLessonDataModel(lessonId) {
    openLoadingModel();
    $.ajax({
        url: "/data/get/number/" + lessonId,
        type: "GET",
        cache: false,//设置不缓存
        success: function (data) {
            var result = JSON.parse(data);
            closeLoadingModel();
            if (result.status == "true") {
                if (result.content <= DATA_MAX_COUNT_EACH_LESSON) {
                    $("#uploadLessonDataLessonId").val(lessonId);
                    $("#uploadLessonDataModel").modal({
                        backdrop : 'static'
                    });
                } else {
                    openAlertModel("每个课时的资料数量不能超过" + DATA_MAX_COUNT_EACH_LESSON);
                }
            } else {
                openAlertModel(result.content);
            }

        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            closeLoadingModel();
            openAjaxErrorAlert(XMLHttpRequest, textStatus, errorThrown);
        }
    });
}

function closeUploadLessonDataModel() {
    $("#uploadLessonDataModel").modal('hide');
}