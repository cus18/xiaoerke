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


var umbrellaFirstPageInit = function() {
    /*获取当前年月日*/
    var date = new Date();
     date = date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate();
     $("#date").html(date);
}

/*重大疾病名称及定义 展开隐藏*/
var lookHelpPlan = function() {
    $(".helpPlan .fold").toggleClass("show");
    $(".helpPlan .foldText").toggleClass("change");

}
/*运营保障 关怀 展开隐藏*/
var care = function() {
    $(".operation .text .care1").toggleClass("show");
    $(".operation .text .care2").toggleClass("change");

}
/*常见问题 展开隐藏*/
var lookQuestion = function(index) {
    $(".questions dt").eq(index).siblings("dt").children("a").removeClass("show");
    $(".questions dd").eq(index).siblings("dd").removeClass("change");
    $(".questions dt a").eq(index).toggleClass("show");
    $(".questions dd").eq(index).toggleClass("change");

}
/*宝大夫儿童重疾互助计划公约  60种重大疾病名称及定义 15种轻症名称及定义 名词释义*/
var lookProtocol = function(index) {
    $(".c-shadow").show();
    $(".protocol").eq(index).show();

}

/*分享好友*/
var goShare = function() {
    $(".c-shadow").show();
    $(".shadow-content.share").show();
}
/*关闭分享提示*/
var cancelRemind = function() {
    $(".c-shadow").hide();
    $(".shadow-content").hide();
}

/*跳转到参与成功页面*/
var myGuarantee = function() {
    window.location.href = "umbrella#/umbrellaJoin";

}
/*跳转到领取成功页面*/
var attentionLock=false;
var goJoin = function() {
   /* window.location.href = "umbrella#/umbrellaJoin";*/
    if(!attentionLock){
        $(".c-shadow").show();
        $(".shadow-content.attention").show();
    }
    else{
        window.location.href = "umbrella#/umbrellaJoin";
    }


}
