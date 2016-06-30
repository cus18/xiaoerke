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
document.write('<scr'+'ipt src="' + webpath + '/js/libs/moment.min.js"></scr'+'ipt>');

$(function(){
    getUserInfo();
    getUserListImage();
    count();
    sumMoney();
    addNoteAndDonation();
    lastNote();
});

var lovePlanFirsInit = function(){

};

var goComment = function(){
    $(".c-shadow").show();
    $(".real-edit").show();
};
var cancelComment = function(){
    $(".c-shadow").hide();
    $(".real-edit").hide();
};

var goLovePlanList = function(){
    window.location.href="market#/lovePlanList"
};

/*var createPoster = function(){
    window.location.href="market#/lovePlanPoster"
};*/
// 点击 我要捐款
var goContribute = function(){
    window.location.href="http://xiaork.cn/keeper/wxPay/patientPay.do?serviceType=lovePlanPay"
};
// 宝护伞 查看详情
var goUmbrella = function(){
    window.location.href="http://s165.baodf.com/wisdom/umbrella#/umbrellaLead/130000000/a"
};

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
                $("#headImage").attr("src",data.headImage);
            }
        },
        error : function() {
        }
    }, 'json');
};
var count=function () {
    $.ajax({
        url:"mutualHelp/donation/count",
        async:false,
        type:'POST',
        data:"",
        contentType: "application/json; charset=utf-8",
        dataType:'json',
        success:function(data) {
            console.log('counts',data.count);
            if(data.count != undefined){
                $("#counts").html(data.count);
            }else{
                $("#counts").html('30');
            }
        },
        error : function() {
        }
    }, 'json');
};
var sumMoney=function () {
    $.ajax({
        url:"mutualHelp/donation/sumMoney",
        async:false,
        type:'POST',
        data:"",
        contentType: "application/json; charset=utf-8",
        dataType:'json',
        success:function(data) {
            if(data.count != undefined){
                console.log('sumMoney',data.count);
                var length = data.count / 300;
                $(".lovePlanFirst .ruler .line").css('width',length+'%');
                $("#lovemoneyCount").html(data.count);
            }else{
                $(".lovePlanFirst .ruler .line").css('width','20%');
                $("#lovemoneyCount").html('3000');
            }
        },
        error : function() {
        }
    }, 'json');
};
var lastNote=function () {
    $.ajax({
        url:"mutualHelp/donation/lastNote",
        async:false,
        type:'POST',
        data:"{}",
        contentType: "application/json; charset=utf-8",
        dataType:'json',
        success:function(data) {
            var date = moment(data.createTime).format('YYYY-MM-DD');
            $("#newNoteContent").html(data.leaveNote);
            $("#createTime").html(date);
            if(data.headImgUrl != ''){
                $("#headImgUrl").attr("src",data.headImgUrl);
            }
        },
        error : function() {
        }
    }, 'json');
};

//发布留言
var addNoteAndDonation = function(){
    var leaveNote = $('#leaveNoteContent').val();
    if(leaveNote != ''){
        $.ajax({
            url:"mutualHelp/donation/addNoteAndDonation",
            //async:false,
            type:'POST',
            data: "{'leaveNote':'"+leaveNote+"'}",
            contentType: "application/json; charset=utf-8",
            dataType:'json',
            success:function() {
                lastNote();
                $('#leaveNoteContent').val('');
                cancelComment();
            },
            error : function() {
            }
        }, 'json');
    }

};
var getUserListImage=function () {
    $.ajax({
        url:"mutualHelp/donation/photoWall",// 跳转到 action
        async:false,
        type:'POST',
        data:"{}",
        contentType: "application/json; charset=utf-8",
        dataType:'json',
        success:function(data) {
            var userImage=data.donationList;
            console.log("data",data);
            if(userImage.length != 0){
                for(var i=0;i<userImage.length;i++){
                    if(userImage[i].headImgUrl != ''){
                        $("#loverHearts"+i).attr("src",userImage[i].headImgUrl);
                    }
                }
            }
        },
        error : function() {
        }
    }, 'json');
}