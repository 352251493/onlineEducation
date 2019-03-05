/**
 * Created by 郭欣光 on 2019/3/5.
 */

function openUploadVideoModel (lessonId) {
    if (stringIsEmpty(lessonId)) {
        openAlertModel("系统未找到课时信息！");
    } else {
        openLoadingModel();
        $.ajax({
            url: "/video/get/number/" + lessonId,
            type: "GET",
            cache: false,//设置不缓存
            success: function (data) {
                var result = JSON.parse(data);
                closeLoadingModel();
                if (result.status == "true") {
                    if (result.content <= VIDEO_MAX_COUNT_EACH_LESSON) {
                        $("#uploadVideoLessonId").val(lessonId);
                        $("#uploadVideoModel").modal({
                            backdrop : 'static'
                        });
                    } else {
                        openAlertModel("每个课时的视频数量不能超过" + VIDEO_MAX_COUNT_EACH_LESSON);
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
}

function uploadVideo() {
    var lessonId = $("#uploadVideoLessonId").val();
    var videoName = $("#videoName").val();
    var videoFile =  document.getElementById('videoFile').files[0];
    var videoFilePath = $('#videoFile').val().toLowerCase().split(".");
    var videoFileType =  videoFilePath[videoFilePath.length - 1];
    if (stringIsEmpty(lessonId)) {
        openAlertModel("系统获取课时信息失败,请刷新页面后重试");
    } else if (stringIsEmpty(videoName)) {
        openAlertModel("请填写视频名称！");
    } else if (videoName.length > 100) {
        openAlertModel("视频名称长度不能超过100字符！");
    } else if (videoFile == null) {
        openAlertModel("请选择视频！");
    } else if (!isMp4AndOggVideo(videoFileType)) {
        openAlertModel("仅支持mp4(avc(h264))以及Ogg格式！");
    } else if (videoFile.size > VIDEO_MAX_SIZE) {
        openAlertModel("视频大小不能超过" + VIDEO_MAX_SIZE_STRING);
    } else {
        openLoadingModel();
        var formData = new FormData($('#uploadVideoForm')[0]);
        $.ajax({
            type: "POST",
            url: "/video/create",
            // async: false,//true表示同步，false表示异步
            cache: false,//设置不缓存
            data: formData,
            contentType: false,//必须设置false才会自动加上正确的Content-Tyoe
            processData: false,//必须设置false才避开jquery对formdata的默认处理，XMLHttpRequest会对formdata进行正确的处理
            success: uploadVideoSuccess,
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                closeLoadingModel();
                openAjaxErrorAlert(XMLHttpRequest, textStatus, errorThrown);
            }
        });
    }
}

function uploadVideoSuccess(data) {
    var result = JSON.parse(data);
    closeLoadingModel();
    if (result.status == "true") {
        closeUploadVideoModel();
    }
    openAlertModel(result.content);
}

function closeUploadVideoModel() {
    $("#uploadVideoModel").modal('hide');
}