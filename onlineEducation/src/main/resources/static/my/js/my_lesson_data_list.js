/**
 * Created by 郭欣光 on 2019/3/1.
 */


function openMyLessonDataListModel(lessonId) {
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
                    $("#myLessonDataListModelInfo").html("这里空空如也~");
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
                        str += "&nbsp;&nbsp;";
                        str += "<button type=\"button\" class=\"btn btn-danger\" onclick=\"deleteLessonData('" + lessonData.id + "');\">删除</button>";
                        str += "</td>";
                        str += "</tr>";
                    }
                    $("#myLessonDataListModelInfo").html(str);
                }
                closeLoadingModel();
                $("#myLessonDataListModel").modal({
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
function deleteLessonData(lessonDataId) {
    if (stringIsEmpty(lessonDataId)) {
        openAlertModel("系统未找到课时资料信息");
    } else {
        openLoadingModel();
        var obj = new Object();
        obj.lessonDataId = lessonDataId;
        $.ajax({
            url: "/data/delete",
            type: "POST",
            cache: false,//设置不缓存
            data: obj,
            success: deleteLessonDataSuccess,
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                closeLoadingModel();
                openAjaxErrorAlert(XMLHttpRequest, textStatus, errorThrown);
            }
        });
    }
}

function deleteLessonDataSuccess(data) {
    var result = JSON.parse(data);
    closeLoadingModel();
    if (result.status = "true") {
        $("#myLessonDataListModel").modal('hide');
    }
    openAlertModel(result.content);
}