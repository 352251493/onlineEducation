/**
 * Created by 郭欣光 on 2019/2/21.
 */

function openCourseDetail(courseId) {
    window.location.href = "/course/my/detail/" + courseId + "/1";
}

function getUser() {
    $.ajax({
        type: "POST",
        url: "/user/get",
        // async: false,//true表示同步，false表示异步
        cache: false,//设置不缓存
        success: function (data) {
            console.log(data);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            console.log(XMLHttpRequest + textStatus + errorThrown);
        }
    });
}

function createLesson() {
    var courseId = $("#courseId").val();
    var lessonName = $("#lessonName").val();
    var lessonContent = editor.txt.html();
    if (stringIsEmpty(courseId)) {
        openAlertModel("系统获取课程信息失败！");
    } else if (stringIsEmpty(lessonName)) {
        openAlertModel("给课时起个名字吧~");
    } else if (lessonName.length > 100) {
        openAlertModel("课时名称不能超过100字符~");
    } else if (stringIsEmpty(lessonContent)) {
        openAlertModel("写点内容呗~");
    } else {
        openLoadingModel();
        var obj = new Object();
        obj.courseId = courseId;
        obj.lessonName = lessonName;
        obj.lessonContent = lessonContent;
        $.ajax({
            url: "/lesson/create",
            type: "POST",
            cache: false,//设置不缓存
            data: obj,
            success: createLessonSuccess,
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                closeLoadingModel();
                openAjaxErrorAlert(XMLHttpRequest, textStatus, errorThrown);
            }
        });
    }
}

function createLessonSuccess(data) {
    var result = JSON.parse(data);
    closeLoadingModel();
    openAlertModel(result.content);
}

$(document).ready(function () {
    var E = window.wangEditor;
    editor = new E('#lessonContent');
    // 下面两个配置，使用其中一个即可显示“上传图片”的tab。但是两者不要同时使用！！！
    editor.customConfig.uploadImgShowBase64 = true;   // 使用 base64 保存图片
    // editor.customConfig.uploadImgServer = '/upload_experimental_img';  // 上传图片到服务器
    // editor.customConfig.uploadImgMaxSize = 10 * 1024 * 1024;//将图片大小限制为10M，默认大小是5M
    editor.customConfig.uploadImgMaxLength = 1;//限制一次最多上传1张图片
    // editor.customConfig.uploadFileName = 'experimentalImage';
    // editor.customConfig.uploadImgHeaders = {
    //     'Accept': 'text/x-json'
    // };
    editor.customConfig.pasteTextHandle = function(content) {
        var reTag = /<img[^>]*>/gi;
        if(content.match(reTag) != null) {
            openAlertModel("复制的内容含有图片，图片内容无法复制，请您在相应的地方手动插入图片");
            return content.replace(reTag, '');
        } else {
            return content;
        }
    };
    editor.customConfig.zIndex = 50;
    // 自定义菜单配置
    editor.customConfig.menus = [
        'head',  // 标题
        'bold',  // 粗体
        'italic',  // 斜体
        'underline',  // 下划线
        'foreColor',  // 文字颜色
        'backColor',  // 背景颜色
        'link',  // 插入链接
        'list',  // 列表
        'justify',  // 对齐方式
        'quote',  // 引用
        'emoticon',  // 表情
        'image',  // 插入图片
        'table',  // 表格
        'code',  // 插入代码
        'undo',  // 撤销
        'redo'  // 重复
    ];
    editor.create();
    setInterval(getUser, 3 * 60 * 1000);
});
