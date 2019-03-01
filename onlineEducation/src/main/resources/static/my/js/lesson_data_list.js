/**
 * Created by 郭欣光 on 2019/3/1.
 */

function openLessonDataListModel(lessonId) {
    openLoadingModel();
    $.ajax({
        url: "/data/get/" + lessonId,
        type: "GET",
        cache: false,//设置不缓存
        success: function (data) {
            var result = JSON.parse(data);
            if (result.status == "true") {
                var lessonDataList = result.content;
                if (lessonDataList == null) {
                    $("#lessonDataListModelInfo").html("这里空空如也~");
                } else {
                    var str = " <table class=\"table table-hover\">";
                    str += "<tr>";
                    str += "<th>列表</th>";
                    str += "<th>时间</th>";
                    str += "<th>操作</th>";
                    str += "</tr>";
                    for (var i = 0; i < lessonDataList.length; i++) {
                        var lessonData = lessonDataList[i];
                        str += "<tr>";
                        str += "<td>";
                        str += lessonData.name;
                        str += "</td>";
                        str += "<td>";
                        str += lessonData.createTime.split(".")[0];
                        str += "</td>";
                        str += "<td>";
                        str += "<button type=\"button\" class=\"btn btn-success\" onclick=\"openLessonDataPage('" + lessonData.path + "');\">查看</button>";
                        str += "</td>";
                        str += "</tr>";
                    }
                    $("#lessonDataListModelInfo").html(str);
                }
                closeLoadingModel();
                $("#lessonDataListModel").modal({
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

function openLessonDataPage(lessonDataPath) {
    window.open("/course_resource/" + lessonDataPath, "_blank");
}
