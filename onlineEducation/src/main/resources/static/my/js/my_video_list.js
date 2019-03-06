/**
 * Created by 郭欣光 on 2019/3/6.
 */

function openMyVideoList(lessonId) {
    openLoadingModel();
    $.ajax({
        url: "/video/get/" + lessonId,
        type: "GET",
        cache: false,//设置不缓存
        success: function (data) {
            var result = JSON.parse(data);
            if (result.status == "true") {
                var videoList = result.content;
                if (videoList == null) {
                    $("#myVideoListModelInfo").html("这里空空如也~");
                } else {
                    var str = " <table class=\"table table-hover\">";
                    str += "<tr>";
                    str += "<th>列表</th>";
                    str += "<th>时间</th>";
                    str += "<th>操作</th>";
                    str += "</tr>";
                    for (var i = 0; i < videoList.length; i++) {
                        var video = videoList[i];
                        str += "<tr>";
                        str += "<td>";
                        str += video.name;
                        str += "</td>";
                        str += "<td>";
                        str += video.createTime.split(".")[0];
                        str += "</td>";
                        str += "<td>";
                        str += "<button type=\"button\" class=\"btn btn-success\" onclick=\"openMyVideoPage('" + video.id + "');\">观看视频</button>";
                        str += "&nbsp;&nbsp;";
                        str += "<button type=\"button\" class=\"btn btn-danger\" onclick=\"openDeleteVideoModel('" + video.id + "');\">删除</button>";
                        str += "</td>";
                        str += "</tr>";
                    }
                    $("#myVideoListModelInfo").html(str);
                }
                closeLoadingModel();
                $("#myVideoListModel").modal({
                    backdrop : 'static'
                });
            } else {
                closeLoadingModel();
                openAlertModel(result.content);
            }

        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            closeLoadingModel();
            openAjaxErrorAlert(XMLHttpRequest, textStatus, errorThrown);
        }
    });
}

function openMyVideoPage(videoId) {
    window.location.href = "/video/my/detail/" + videoId;
}

function openDeleteVideoModel(videoId) {
    if (stringIsEmpty(videoId)) {
        openAlertModel("系统未找到视频信息");
    } else {
        $("#deleteVideoId").val(videoId);
        $("#deleteVideoModel").modal({
            backdrop : 'static'
        });
    }
}

function deleteVideo() {
    var videoId = $("#deleteVideoId").val();
    if (stringIsEmpty(videoId)) {
        openAlertModel("系统未找到视频信息");
    } else {
        openLoadingModel();
        var obj = new Object();
        obj.videoId = videoId;
        $.ajax({
            url: "/video/delete",
            type: "POST",
            cache: false,//设置不缓存
            data: obj,
            success: deleteVideoSuccess,
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                closeLoadingModel();
                openAjaxErrorAlert(XMLHttpRequest, textStatus, errorThrown);
            }
        });
    }
}

function deleteVideoSuccess(data) {
    var result = JSON.parse(data);
    closeLoadingModel();
    if (result.status = "true") {
        $("#myVideoListModel").modal('hide');
    }
    openAlertModel(result.content);
}