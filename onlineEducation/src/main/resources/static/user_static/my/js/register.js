/**
 * 该方法实现预览用户头像功能
 */
function headImageChange() {
	var preHeadImage = document.getElementById("preHeadImage");
	var headImage = document.getElementById("headImage");
	var ext=headImage.value.substring(headImage.value.lastIndexOf(".")+1).toLowerCase();
	// gif在IE浏览器暂时无法显示
	if(ext!='png'&& ext!='jpg'&& ext!='jpeg'){
		openAlertModel("图片的格式必须为png或者jpg或者jpeg格式！");
		return;
	}
	var isIE = navigator.userAgent.match(/MSIE/)!= null;
	var isIE6 = navigator.userAgent.match(/MSIE 6.0/)!= null;
	if(isIE) {
		headImage.select();
		var reallocalpath = document.selection.createRange().text;
        // IE6浏览器设置img的src为本地路径可以直接显示图片
		if (isIE6) {
			preHeadImage.src = reallocalpath;
		}else {
			// 非IE6版本的IE由于安全问题直接设置img的src无法显示本地图片，但是可以通过滤镜来实现
			preHeadImage.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod='image',src=\"" + reallocalpath + "\")";
			// 设置img的src为base64编码的透明图片 取消显示浏览器默认图片
			preHeadImage.src = 'data:image/gif;base64,R0lGODlhAQABAIAAAP///wAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==';
         }
     }else {
		html5Reader(headImage);
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
         var pic = document.getElementById("preHeadImage");
         pic.src=this.result;
     }
 }

/**
 * 该方法提交用户注册信息
 */
function register() {
	var email = $("#email").val();
	var name = $("#name").val();
	var password = $("#password").val();
	var repassword = $("#repassword").val();
	var sex = $('input:radio[name="sex"]:checked').val();
	var job = $('input:radio[name="job"]:checked').val();
	var headImage =  document.getElementById('headImage').files[0];
	var headImagePath = $('#headImage').val().toLowerCase().split(".");
	var headImageType =  headImagePath[headImagePath.length - 1];
	if (stringIsEmpty(email)) {
		openAlertModel("邮箱不能为空，请输入您的邮箱");
	} else if (email.length > 150) {
		openAlertModel("邮箱长度不能超过150个字符！");
	} else if (!isEmail(email)) {
		openAlertModel("给个正确的邮箱格式吧~");
	} else if (stringIsEmpty(name)) {
		openAlertModel("留下您的姓名吧！");
	} else if (name.length > 100) {
		openAlertModel("您的姓名太长啦，超过100字符我有点记不住");
	} else if (stringIsEmpty(password)) {
		openAlertModel("留下密码才安全");
	} else if (password.length > 20) {
		openAlertModel("密码20字符以内就很安全啦！");
	} else if (password != repassword) {
		openAlertModel("两次输入的密码不一致，我该记住哪一个呢？");
	} else if (stringIsEmpty(sex)) {
		openAlertModel("咦？难道没有默认性别吗？");
	} else if (sex.length > 1) {
		openAlertModel("难道性别长度被篡改了？");
	} else if (stringIsEmpty(job)) {
		openAlertModel("咦？难道没有默认职业吗？");
	} else if (job.length > 10) {
		openAlertModel("难道职位长度被篡改了？");
	} else if (headImage == null) {
		openAlertModel("选一个头像更有吸引力哦~");
	} else if (!isImage(headImageType)) {
		openAlertModel("上传的头像必须为图片类型哦~");
	} else if (headImage.size > HEAD_IMAG_MAX_SIZE) {
		openAlertModel("头像太大啦，不要超过" + HEAD_IMAGE_MAX_SIZE_STRING + "哦~");
	} else {
		openLoadingModel();
		var formData = new FormData($('#registerForm')[0]);
		$.ajax({
			type: "POST",
			url: "/user/register",
			// async: false,//true表示同步，false表示异步
			cache: false,//设置不缓存
			data: formData,
			contentType: false,//必须设置false才会自动加上正确的Content-Tyoe
			processData: false,//必须设置false才避开jquery对formdata的默认处理，XMLHttpRequest会对formdata进行正确的处理
			success: registerSuccess,
			error: function (XMLHttpRequest, textStatus, errorThrown) {
				closeLoadingModel();
				openAjaxErrorAlert(XMLHttpRequest, textStatus, errorThrown);
			}
		});
	}
}

function registerSuccess(data) {
	var result = JSON.parse(data);
	closeLoadingModel();
	openAlertModel(result.content);
}