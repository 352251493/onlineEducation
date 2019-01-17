/**
 * Created by 郭欣光 on 2019/1/15.
 */

var HEAD_IMAG_MAX_SIZE = 10 * 1024 * 1024; //用户头像图片最大大小10MB
var HEAD_IMAGE_MAX_SIZE_STRING = "10MB"; //用户头像图片最大大小10MB

/**
 * 该方法判断字符串是否为空
 * @param str 字符创
 * @returns {boolean} true 为空 false 不为空
 */
function stringIsEmpty(str) {
    if (str == null || str.trim == "" || str.length == 0) {
        return true;
    } else {
        return false;
    }
}

/**
 * 该方法为打开提示框
 * @param str 提示信息
 */
function openAlertModel(str) {
    $("#alertModelInfo").html(str);
    $("#alertModel").modal({
        backdrop : 'static'
    });
}

/**
 * 根据文件类型判断是否为图片
 * @param fileType 文件类型
 */
function isImage(fileType) {
    if (fileType != "bmf" && fileType != "png" && fileType != "gif" && fileType != "jpg" && fileType != "jpeg") {
        return false;
    } else {
        return true;
    }
}

/**
 * 验证邮箱格式是否正确
 * @param email 邮箱
 */
function isEmail(email) {
    if(!email.match(/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/)) {
        return false;
    } else {
        return true;
    }
}

/**
 * 打开等待模态框
 */
function openLoadingModel() {
    $("#loadingModel").modal({
        backdrop : 'static'
    });
}

/**
 * 关闭等待模态框
 */
function closeLoadingModel() {
    setTimeout(function () {
        $("#loadingModel").modal('hide');
    }, 500);
    // $("#loadingModelClose").click();
}

/**
 * ajax错误信息提示
 * @param XMLHttpRequest
 * @param textStatus
 * @param errorThrown
 */
function openAjaxErrorAlert(XMLHttpRequest, textStatus, errorThrown) {
    if (XMLHttpRequest.status >= 400 && XMLHttpRequest.status < 500) {
        openAlertModel("客户端请求出错！错误代码（" + XMLHttpRequest.status + "," + XMLHttpRequest.readyState + "," + textStatus + "）");
    } else if (XMLHttpRequest.status >= 500 || XMLHttpRequest.status <= 600) {
        openAlertModel("服务器出错！错误代码（" + XMLHttpRequest.status + "," + XMLHttpRequest.readyState + "," + textStatus + "）");
    } else if (XMLHttpRequest.status >= 300 || XMLHttpRequest.status < 400) {
        openAlertModel("重定向问题！错误代码（" + XMLHttpRequest.status + "," + XMLHttpRequest.readyState + "," + textStatus + "）");
    } else {
        openAlertModel("抱歉，系统好像出现一些异常！错误代码（" + XMLHttpRequest.status + "," + XMLHttpRequest.readyState + "," + textStatus + "）");
    }
}