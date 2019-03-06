/**
 * Created by 郭欣光 on 2019/3/6.
 */

function openVideoList(lessonId, courseType) {
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
                    $("#videoListModelInfo").html("这里空空如也~");
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
                        str += "<button type=\"button\" class=\"btn btn-success\" onclick=\"openVideoDetailPage('" + courseType + "', '" + video.id + "');\">观看视频</button>";
                        str += "</td>";
                        str += "</tr>";
                    }
                    $("#videoListModelInfo").html(str);
                }
                closeLoadingModel();
                $("#videoListModel").modal({
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

function openVideoDetailPage(courseType, videoId) {
    window.location.href = "/video/" + courseType + "/detail/" + videoId
}