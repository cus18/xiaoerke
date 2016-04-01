var webpath = "/xiaoerke-healthPlan";
document.write('<scr'+'ipt src="' + webpath + '/js/libs/ionic.bundle.min.js"></scr'+'ipt>');
document.write('<scr'+'ipt src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/jquery-2.1.3.min.js?ver=1.0.7"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/angular.min.js?ver=1.0.7"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/angular-resource.min.js?ver=1.0.7"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/angular-sanitize.min.js?ver=1.0.7"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/angular-route.min.js?ver=1.0.7"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/jquery.event.drag-1.5.min.js"></scr'+'ipt>');
document.write('<scr'+'ipt src="' + webpath + '/js/libs/jquery.touchSlider.js"></scr'+'ipt>');

var nutritionAdd = false;
var constipationAdd = false;
var healthPlanFirstPageInit = function() {
    recordLogs("healthPlanFirstPageAccess");
    $.ajax({
        url: 'ap/info/loginStatus',
        type: 'post',
        data: {},
        complete: function(jqXHR){
            if(jqXHR.status=="404"){
                window.location.href = "ap/firstPage/healthPlan";
            }
        },
        success:function(data){
            healthPlanFirst();
        }
    });
}

var healthPlanFirst = function(){
    var paramValue = '{routePath:"/ap/firstPage/healthPlan"}';
    $.ajaxSetup({
        contentType : 'application/json',
    });
    $.post('ap/info/loginStatus',paramValue,
        function(data) {
             if(data.status=="9"){
                window.location.href = data.redirectURL;
            }
            else{
                var type = GetRequest();
                getAddStatus();
                $.ajax({
                    url:"ap/nutrition/judgeUserManage",// 跳转到 action
                    async:true,
                    type:'post',
                    data:{},
                    cache:false,
                    dataType:'json',
                    success:function(data) {
                        var judge = data.resultMsg.split(";");
                        if(type=="type=second"){
                            if(nutritionAdd){
                                setHide1();
                                showManage(1);
                            }
                            if(constipationAdd){
                                setHide0();
                                showManage(0);
                            }
                        }else {
                            if(nutritionAdd||constipationAdd){
                                if(judge.length==3){
                                    if(judge[1]=="1"){
                                        setHide0();
                                        showManage(0);
                                        window.location.href = "ap/ctp#/constipationIndex";
                                    }else{
                                        setHide1();
                                        showManage(1);
                                        window.location.href = "ap/ntr#/nutritionIndex";
                                    }
                                }else if(judge.length==4){
                                    setHide0();
                                    setHide1();
                                    showManage(0);
                                    showManage(1);
                                }
                            }else if(constipationAdd&&nutritionAdd){
                                setHide0();
                                setHide1();
                                showManage(0);
                                showManage(1);
                            }else if(judge.length==4){
                                setHide0();
                                setHide1();
                                showManage(0);
                                showManage(1);
                            }else if(judge.length==3){
                                if(judge[1]=="1"){
                                    setHide0();
                                    showManage(0);
                                }else if(judge[1]=="2"){
                                    setHide1();
                                    showManage(1);
                                    window.location.href = "ap/ntr#/nutritionIndex";
                                }
                            }
                        }
                    },
                    error : function() {
                    }
                });
            }
        }, 'json');
}

var recordLogs = function(val){
    $.ajax({
        url:"ap/util/recordLogs",// 跳转到 action
        async:true,
        type:'post',
        data:{logContent:val},
        cache:false,
        dataType:'json',
        success:function(data) {
        },
        error : function() {
        }
    });
}

//隐藏便秘管理加号
var setHide0 = function(){
    $(" .add-detail").eq(0).hide();
    $(" .manage-list .add-pic ").eq(0).hide();
}
//隐藏营养管理加号
var setHide1 = function(){
    $(" .add-detail").eq(1).hide();
    $(" .manage-list .add-pic ").eq(1).hide();
}
//显示进入管理按钮
var showManage = function (id) {
    $(" .enter-manage").eq(id).show();
}

var selectManage=function(num){
    if(num=="0"){
        window.location.href = "ap/ctp#/constipationIndex";
    }
    else{
       window.location.href = "ap/ntr#/nutritionIndex";
    }

 }
var addDetail=function(num) {
    $(" .add-detail").eq(num).show();
}

var addManage=function(num){
    if(num=="0"){
        setAddStatus("constipation");
    }else{
        setAddStatus("nutrition");
    }
}

var setAddStatus = function(val){
    var param = '{"addValue":"' + val + '"}';
    $.ajaxSetup({
        contentType : 'application/json'
    });
    $.post('ap/util/user/recordHealthPlanAddItem',param,
        function(data) {
            if(data.result=="success"){
                if(val=="constipation"){
                    $(" .add-detail").eq(0).hide();
                    $(" .manage-list .add-pic ").eq(0).hide();
                    $(" .enter-manage").eq(0).show();
                }else if(val=="nutrition"){
                    $(" .add-detail").eq(1).hide();
                    $(" .manage-list .add-pic ").eq(1).hide();
                    $(" .enter-manage").eq(1).show();
                }
            };
        }, 'json');
}

var getAddStatus = function(){
    $.ajax({
        url:"ap/util/user/findHealthPlanAddItem",// 跳转到 action
        async:true,
        type:'post',
        data:{},
        cache:false,
        dataType:'json',
        success:function(data) {
            $.each(data,function(index,value){
                if(value.addValue=="nutrition"){
                    nutritionAdd = true;
                }else if(value.addValue=="constipation"){
                    constipationAdd = true;
                }
            })
        },
        error : function() {
        }
    });
}

function GetRequest() {
    var url = location.search; //获取url中"?"符后的字串
    if (url.indexOf("?") != -1) {
        var str = url.substr(1);
    }
    return str;
}


