var webpath = "/market";
document.write('<scr'+'ipt src="' + webpath + '/js/libs/ionic.bundle.min.js"></scr'+'ipt>');
document.write('<scr'+'ipt src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/jquery-2.1.3.min.js?ver=1.0.7"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/angular.min.js?ver=1.0.7"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/angular-resource.min.js?ver=1.0.7"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/angular-sanitize.min.js?ver=1.0.7"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/angular-route.min.js?ver=1.0.7"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/jquery.event.drag-1.5.min.js"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/jquery.touchSlider.js"></scr'+'ipt>');


var lovePlanFirsInit = function(){

}

var goComment = function(){
    $(".c-shadow").show();
    $(".real-edit").show();
}
var cancelComment = function(){
    $(".c-shadow").hide();
    $(".real-edit").hide();
}
var goLovePlanList = function(){
    window.location.href="market#/lovePlanList"
}
// 点击 我要捐款
var goContribute = function(){
    window.location.href="http://localhost:8080/keeper/wxPay/patientPay.do?serviceType=lovePlanPay"
}
// 宝护伞 查看详情
var goUmbrella = function(){
    window.location.href="http://s165.baodf.com/wisdom/umbrella#/umbrellaLead/130000000/a"
}
/*var createPoster = function(){
    window.location.href="market#/lovePlanPoster"
}*/

$(function(){
    getUserInfo();
    getUserListImage();
});

var getUserInfo=function () {
    $.ajax({
        url:"loveMarketing/visitPage",// 跳转到 action
        async:false,
        type:'POST',
        data:"",
        contentType: "application/json; charset=utf-8",
        dataType:'json',
        success:function(data) {
            if(data.fault=="null"){
                $(".info").hide();
            }else{
                $("#friendNums").html(data.friendNum);
                $("#transcend").html(data.transcend);
                $("#loveMoney").html(data.lovemoney);
                $("#headImage").attr("src",data.headImage.headImage);
            }
        },
        error : function() {
        }
    }, 'json');
}

var getUserListImage=function () {
    $.ajax({
        url:"loveMarketing/getAllLoverHeart",// 跳转到 action
        async:false,
        type:'POST',
        data:"",
        contentType: "application/json; charset=utf-8",
        dataType:'json',
        success:function(data) {
            var userImage=data.userImageList;
            for(var i=0;i<userImage.length;i++){
                $("#loverHearts"+i).attr("src",userImage[i].headImage);
            }
            $("#counts").val(data.counts);
            $("#lovemoneyCount").html(data.countMoney);
        },
        error : function() {
        }
    }, 'json');
}