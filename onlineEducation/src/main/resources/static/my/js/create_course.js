/**
 * Created by 郭欣光 on 2019/2/15.
 */

/**
 * 该方法实现预览课程封面图片功能        
 */
function courseImageChange() {
    var courseImage = document.getElementById("courseImage");
    var ext=courseImage.value.substring(courseImage.value.lastIndexOf(".")+1).toLowerCase();
    // gif在IE浏览器暂时无法显示
    if(ext!='png'&& ext!='jpg'&& ext!='jpeg'){
        $("#preCourseImageDiv").html("");
        openAlertModel("图片的格式必须为png或者jpg或者jpeg格式！");
        return;
    }
    var isIE = navigator.userAgent.match(/MSIE/)!= null;
    var isIE6 = navigator.userAgent.match(/MSIE 6.0/)!= null;
    $("#preCourseImageDiv").html("<img class=\"gxg-course-style\" id=\"preCourseImage\" src=\"\" width=\"100%\">");
    var preCourseImage = document.getElementById("preCourseImage");
    if(isIE) {
        courseImage.select();
        var reallocalpath = document.selection.createRange().text;
        // IE6浏览器设置img的src为本地路径可以直接显示图片
        if (isIE6) {
            preCourseImage.src = reallocalpath;
        }else {
            // 非IE6版本的IE由于安全问题直接设置img的src无法显示本地图片，但是可以通过滤镜来实现
            preCourseImage.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod='image',src=\"" + reallocalpath + "\")";
            // 设置img的src为base64编码的透明图片 取消显示浏览器默认图片
            preCourseImage.src = 'data:image/gif;base64,R0lGODlhAQABAIAAAP///wAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==';
        }
    }else {
        html5Reader(courseImage);
    }
}

/**
 * 该方法未非IE浏览器实现预览用户头像功能
 * @param file 图片文件
 */
function html5Reader(file) {
    var file = file.files[0];
    var reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = function(e){
        var pic = document.getElementById("preCourseImage");
        pic.src=this.result;
    }
}

/**
 * 创建课程
 */
function createCourse() {
    var courseName = $("#courseName").val();
    var courseIntroduction = $("#courseIntroduction").val();
    var courseImage =  document.getElementById('courseImage').files[0];
    var courseImagePath = $('#courseImage').val().toLowerCase().split(".");
    var courseImageType =  courseImagePath[courseImagePath.length - 1];
    var isPrivate = $('input:radio[name="isPrivate"]:checked').val();
    if (stringIsEmpty(courseName)) {
        openAlertModel("课程名称不能为空！");
    } else if (courseName.length > 100) {
        openAlertModel("课程名称长度不能超过100字符！");
    } else if (stringIsEmpty(courseIntroduction)) {
        openAlertModel("课程简介不能为空！");
    } else if (courseIntroduction.length > 500) {
        openAlertModel("课程简介长度不能超过500字符！");
    } else if (courseImage == null) {
        openAlertModel("为该课程选一个好看的封面吧~");
    } else if (!isImage(courseImageType)) {
        openAlertModel("课程封面必须是图片类型哦~");
    } else if (courseImage.size > COURSE_IMAGE_MAX_SIZE) {
        openAlertModel("图片太大啦，不要超过" + COURSE_IMAGE_MAX_SIZE_STRING + "哦~");
    } else if (stringIsEmpty(isPrivate) || isPrivate.length > 1) {
        openAlertModel("咦？难道课程类型被篡改了？");
    } else {
        openLoadingModel();
        var formData = new FormData($('#createCourseForm')[0]);
        $.ajax({
            type: "POST",
            url: "/course/create",
            // async: false,//true表示同步，false表示异步
            cache: false,//设置不缓存
            data: formData,
            contentType: false,//必须设置false才会自动加上正确的Content-Tyoe
            processData: false,//必须设置false才避开jquery对formdata的默认处理，XMLHttpRequest会对formdata进行正确的处理
            success: createCourseSuccess,
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                closeLoadingModel();
                openAjaxErrorAlert(XMLHttpRequest, textStatus, errorThrown);
            }
        });
    }
}

function createCourseSuccess(data) {
    var result = JSON.parse(data);
    if (result.status == "true") {
        window.location.href = "/course/my/detail/" + result.content + "/1";
    } else {
        closeLoadingModel();
        openAlertModel(result.content);
    }
}
