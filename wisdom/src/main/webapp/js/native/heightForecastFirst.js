var webpath = "/wisdom";
document.write('<scr'+'ipt src="' + webpath + '/js/libs/ionic.bundle.min.js"></scr'+'ipt>');
document.write('<scr'+'ipt src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/jquery-2.1.3.min.js?ver=1.0.7"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/angular.min.js?ver=1.0.7"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/angular-resource.min.js?ver=1.0.7"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/angular-sanitize.min.js?ver=1.0.7"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/angular-route.min.js?ver=1.0.7"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/jquery.event.drag-1.5.min.js"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/jquery.touchSlider.js"></scr'+'ipt>');

var heightForecastFirstInit = function () {
    recordLogs('YYHD_SG_YDY');
};
var goBirthTest = function () {
    recordLogs('YYHD_SG_YDY_YCS');
    window.location.href="heightForecast#/heightForecastBirth"
};
var goNoBirthTest = function () {
    recordLogs('YYHD_SG_YDY_WCS');
    window.location.href="heightForecast#/heightForecastNoBirth"
};
var recordLogs = function(val){
    $.ajax({
        url:"util/recordLogs",// 跳转到 action
        async:true,
        type:'get',
        data:{logContent:encodeURI(val)},
        cache:false,
        dataType:'json',
        success:function(data) {
        },
        error : function() {
        }
    });
};

