/**
 * Created by 郭欣光 on 2019/3/19.
 */

function openAddExamModel() {
    $("#addExamModel").modal({
        backdrop : 'static'
    });
}

$(document).ready(function () {
    $('.form_datetime').datetimepicker({
        language:  'zh_CN',
        weekStart: 1,
        todayBtn:  1,
        autoclose: 1,
        todayHighlight: 1,
        startView: 2,
        forceParse: 0,
        showMeridian: 1
    });
});
