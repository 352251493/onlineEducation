var endTime = 5; //倒计时

$(document).ready(function () {	
	setInterval("calculationEndTime()", 1000);
});

function calculationEndTime() {
	$("#endTime").html(endTime);
	if (endTime <= 0){
		window.location.href = '/';
	} else {
		endTime--;
	}
}