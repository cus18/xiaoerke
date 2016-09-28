document.write('<scr'+'ipt src="js/libs/ionic.bundle.min.js"></scr'+'ipt>');
document.write('<scr'+'ipt src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></scr'+'ipt>');
document.write('<scr'+'ipt src="js/libs/jquery-2.1.3.min.js?ver=1.0.7"></scr'+'ipt>');
document.write('<scr'+'ipt src="js/libs/angular.min.js?ver=1.0.7"></scr'+'ipt>');
document.write('<scr'+'ipt src="js/libs/angular-resource.min.js?ver=1.0.7"></scr'+'ipt>');
document.write('<scr'+'ipt src="js/libs/angular-sanitize.min.js?ver=1.0.7"></scr'+'ipt>');
document.write('<scr'+'ipt src="js/libs/angular-route.min.js?ver=1.0.7"></scr'+'ipt>');
document.write('<scr'+'ipt src="js/libs/jquery.event.drag-1.5.min.js"></scr'+'ipt>');
document.write('<scr'+'ipt src="js/libs/jquery.touchSlider.js"></scr'+'ipt>');

var appointmentFirstPageInit = function(){
    recordLogs("accessAppointmentFirstPageInit");
    chooseHospital();
    roundPage();
    initLight();
}
// 轮播图
var roundPage = function(){
    $dragBln = false;
    $(".banner_image").touchSlider({
        flexible : true,
        speed : 200,
        btn_prev : $("#btn_prev"),
        btn_next : $("#btn_next"),
        paging : $(".flicking_con a"),
        counter : function (e){
            $(".flicking_con a").removeClass("on").eq(e.current-1).addClass("on");
        }
    });
    $(".banner_image").bind("mousedown", function() {
        $dragBln = false;
    });
    $(".banner_image").bind("dragstart", function() {
        $dragBln = true;
    });
    $(".banner_image a").click(function(){
        if($dragBln) {
            return false;
        }
    });
    var timer = setInterval(function(){
        $("#btn_next").click();
    }, 5000);
    $(".banner_visual").hover(function(){
        clearInterval(timer);
    },function(){
        timer = setInterval(function(){
            $("#btn_next").click();
        },5000);
    });
    $(".banner_image").bind("touchstart",function(){
        clearInterval(timer);
    }).bind("touchend", function(){
        clearInterval(timer);
        timer = setInterval(function(){
            $("#btn_next").click();
        }, 5000);
    });
}

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
}

/*四个菜单选择*/
var chooseMenu=function(item,index){
    $("#menu li").removeClass("cur").eq(index).addClass("cur");
    $(".menuItem").hide().eq(index).show();;
    if(item=="hospital"){
        chooseHospital();
    }
    else if(item=="illness"){
        chooseIllness();
    }
    else if(item=="time"){
        chooseTime();
    }
    else {
        chooseDoctorGroup();
    }

}

/*医院列表*/
var chooseHospital = function(){

    var param = '{pageNo: "1", pageSize: "200",orderBy:"1"}';
    $.ajaxSetup({
        contentType : 'application/json'
    });
    $.post('sys/hospital',param,
        function(data) {
            if(data!=null ){
                var hospitalList = "";
                $.each(data.hospitalData,function(index,value){
                    var imgItem = "";
                    var paramHos = "'" +value.hospitalId + "'" + ","  + "'" + value.hospitalName + "'"+ ","  + "'" +value.hospitalType + "'";
                    if( value.hospitalName=="八里庄第二社区卫生服务中心" || value.hospitalName=="北京幸福嘉园儿童医院定福庄北街分院" || value.hospitalName=="北京幸福嘉园儿童医院嘉园三里分院"){
                        imgItem = '<img src="http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/ap%2Fbali_warn.png"' +
                            'width="58" height="23.5" style="margin-left:9px;vertical-align: middle;margin-top:0;">';
                    }
                    hospitalList = hospitalList + '<li class="item" onclick="directHospital(' + paramHos + ')">' +
                        '<div class="f6 c3">'+ getLimitString(value.hospitalName) +
                        imgItem +
                        '</div><span class="f2 c4">' + value.hospitalLocation + '</span></li>';
                })
                $('#hospitalItem').html(hospitalList);
            }
        }, 'json');
}
var directHospital = function(hospitalId,hospitalName,hospitalType){

    if( hospitalId=="626487c34f954abd90e188bb40341995"|| hospitalId=="faa875abfe9748438111aa208151f1ed"|| hospitalId=="dda909f2fd79403b9a47b63ce11fd5a4"|| hospitalId=="e6845738a532467d8257a0f69a18903c"|| hospitalId=="508c345c3ddb4d25b1551cf7ff55031f"|| hospitalId=="c1e4456b874d44208b58f1fa72e24e96")
    {
        window.location.href = "appoint#/cooperationHospital/" + hospitalId;
    }
    else{
        window.location.href = "appoint#/searchList/" + hospitalId + ",searchDoctorByHospitalName," + hospitalName;
    }
}

/*疾病列表*/
var chooseIllness = function(){

    var param = '{"pageNo":"1","pageSize":"100","orderBy":"0"}';
    $.ajaxSetup({
        contentType : 'application/json'
    });
    $.post('sys/illness/first',param,
        function(data) {
            if(data!=null ){
                var illnessFirstList = "";
                var initChoose = ";"
                $.each(data.illnessData,function(index,value){
                    if(index==0) {
                        initChoose = value.illnessName;
                    }
                    var chooseFirstIllnessName = "'" + value.illnessName +"','firstIllnessItem" + index + "'";
                    illnessFirstList = illnessFirstList + '<li class="border2" id="firstIllnessItem' + index + '" ' +
                        'onclick="selectIllnessDetail(' + chooseFirstIllnessName + ')"><a>'+ value.illnessName +'</a></li>';
                })
                $('.ill-left').html(illnessFirstList);
                selectIllnessDetail(initChoose,"firstIllnessItem0");
            }
        }, 'json');
}
var selectIllnessDetail = function(chooseFirstIllnessName,firstIllnessItem){
    $(".ill-left li").removeAttr("style");
    $("#"+firstIllnessItem).css('background','#f6f6f6 url(http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/ap%2Fa_illness_bg.png)' +
        ' no-repeat 2px center');
    var param = '{"illnessName":"' + chooseFirstIllnessName + '"}';
    $.ajaxSetup({
        contentType : 'application/json'
    });
    $.post('sys/illness/second',param,
        function(data) {
            if(data!=null ){
                var illnessSecondList = "";
                $.each(data.illnessListData,function(index,value){
                    var chooseSecondIllness = "'" + value.illnessSecondId +"','" + value.illnessSecondName + "'";
                    illnessSecondList = illnessSecondList + '<li class="border2"><a class="f3 c3" onclick="searchSecondIllness(' + chooseSecondIllness + ')">' + value.illnessSecondName + '</a> </li>';
                })
                $('.ill-right').html(illnessSecondList);
            }
        }, 'json');
}
var searchSecondIllness = function(illnessSecondId,illnessSecondName){
    window.location.href = window.location.href = "appoint#/searchList/" + illnessSecondId + ",searchDoctorByIllnessSecondId," + illnessSecondName;
}

/*时间列表*/
var chooseTime = function(){

    var param = '{"pageNo":"1","pageSize":"7","orderBy":"1"}';
    $.ajaxSetup({
        contentType : 'application/json'
    });
    $.post('register/user/time',param,
        function(data) {
            if(data!=null ){
                var timeList = "";
                $.each(data.timeData,function(index,value){
                    var searchDate = "'" + value.date + "'";
                    if(parseInt(index)<3){
                        timeList = timeList + '<li class="f5 c3 border2"  onclick="searchAppointmentByDate(' + searchDate + ')">'+ value.date + '&nbsp;&nbsp;'+ value.week +'<span class="c0 tc">'+value.desc+'</span></li>';
                    }
                    else{
                        timeList = timeList + '<li class="f5 c3 border2"  onclick="searchAppointmentByDate(' + searchDate + ')">' + value.date + '&nbsp;&nbsp;'+ value.week +'<i class="ion-ios-arrow-right"></i></li>';
                    }
                })
                $('#dateItem').html(timeList);
            }
        }, 'json');
}
var searchAppointmentByDate = function(searchDate){
    searchDate = searchDate.replace("/","%252F");
    window.location.href = "appoint#/searchList/" + searchDate + ",searchDoctorByDate," + searchDate;
}
/*专家团*/
var chooseDoctorGroup = function(){

    var param = '{"pageNo":"1","pageSize":"100","orderBy":"1"}';
    $.ajaxSetup({
        contentType : 'application/json'
    });
    $.post('sys/doctor/group',param,
        function(data) {
            if(data!=null ){
                var doctorGroupList = "";
                $.each(data.doctorGroupList,function(index,value){
                    var imgList = value.doctorIdList.split(";");
                    var imgVal = "";
                    $.each(imgList,function(index,val){
                        imgVal = imgVal + '<img class="doctor" width="20" height=20 src="http://xiaoerke-doctor-pic.oss-cn-' +
                            'beijing.aliyuncs.com/'+val+'">';
                    })

                    var expertiseList = value.expertise.split(";");
                    var expertiseVal = "";
                    $.each(expertiseList,function(index,val){
                        if(index==0){
                            expertiseVal = expertiseVal + '<div class="skilled"><span class="f3 c4">擅长：</span>';
                        }
                        if(val.length>5){
                            expertiseVal = expertiseVal + '<a class="bg1 tc f2"> ' + val.substr(0, 5)+'...'+'</a>';
                        }
                        else{
                            expertiseVal = expertiseVal + '<a class="bg1 tc f2"> ' + val+'</a>';
                        }
                        if(index==expertiseList.length-1){
                            expertiseVal = expertiseVal + '</div> ';
                        }
                    })

                    doctorGroupList = doctorGroupList +
                        '<li class="" onclick="chooseDoctorGroupList('+ value.doctorGroupId +')">' +
                        '<h3 class="f5 c3 fw0">' +
                        value.doctorGroupName + imgVal +
                        '</h3> ' + expertiseVal +
                        '<div class="introduce f2 c5">' +
                        value.description +
                        '</div> </li>';

                })
                $('#groupItem').html(doctorGroupList);
            }
        }, 'json');

    recordLogs("doctorGroupIndex");
}
var chooseDoctorGroupList = function(groupId){
    window.location.href = "appoint#/doctorGroupList/"+groupId;
}
var getDoctorGroupDescription = function(val){
    return val.replace(/(.{22})/g,'$1<br/>')
}

/*截取13位医院名称*/
var getLimitString = function(val){
    if(val.length>13) {
        return val.substring(0,13)+"...";
    }else{
        return val;
    }
}

/*顶部搜索*/
var seaHis = function() {
    $(".sea-his").show();
    $(".banner_visual").hide();
    if($('#searchObj').val()!=""){
        var searchData = "'"+$('#searchObj').val()+"'";
        searchItem = '<a class="ss" onclick="searchByName(' + searchData + ')">搜索</a>'
        $(".head .right").html(searchItem);
    }else{
        searchItem = '<a class="ss" onclick="cancelSearch()">取消</a>'
        $(".head .right").html(searchItem);
    }

    var patientList = ['湿疹','咳嗽','发烧','过敏','感冒','鼻炎','眼科','腹泻','呕吐'];

    var searchList = "";
    $.each(patientList,function(index,value){
        var hotValue = "'" + value + "'";
        searchList = searchList + '<li onclick="searchByName(' + hotValue + ')"><a>'+ value + '</a> </li>';
    })
    $('.hot-w').html(searchList);

    $('#searchObj').bind('input propertychange',function(){
        if($('#searchObj').val()!=""){
            var searchData = "'"+$('#searchObj').val()+"'";
            searchItem = '<a class="ss" onclick="searchByName(' + searchData + ')">搜索</a>'
            $(".head .right").html(searchItem);
        }else{
            searchItem = '<a class="ss" onclick="cancelSearch()">取消</a>'
            $(".head .right").html(searchItem);
        }
    });

    var param = '{}';
    $.ajaxSetup({
        contentType : 'application/json'
    });
    $.post('search/user/PatientSearchList',param,
        function(data) {
            if(data!=null ){
                var searchHistoryList = "";
                if(data.patientSearchList.length>0){
                    $("#searchHistoryItem").show();
                    $(".history").show();
                }
                $.each(data.patientSearchList,function(index,value){
                    var searchName = "'" +value.idurlname + "'";
                    searchHistoryList = searchHistoryList + '<li class="item" onclick="searchByName('+searchName+')">'+
                        value.idurlname +'<i class="ion-ios-arrow-right"></i> </li>';
                })
                $('#searchHistory').html(searchHistoryList);
            }
        }, 'json');
}

var searchByName = function(searchData){
    window.location.href = "appoint#/searchList/" + searchData + ",searchDoctorByOpenSearch," + searchData;
}
var cancelSearch = function(){
    $(".sea-his").hide();
    $(".banner_visual").show();
    var myItem = '<a class= my" href="appoint#/myselfFirst/,"><img src="http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/ap%2Fmy1.png" ' +
        'width="22" height="22"> </a>'
    $(".head .right").html(myItem);
}

var removeAllSearchHistory = function(){
    $("#searchHistoryItem").hide();
    $(".history").hide();
    var param = '{}';
    $.ajaxSetup({
        contentType : 'application/json'
    });
    $.post('search/user/RemoveAllSearchHistory',param,
        function(data) {
        }, 'json');
}

/*快速入口*/
var initLight = function(){
    var configStart = {latency: 100};
    var configStop = {latency: 500};
    $("#hospitalList")
        .on('scrollstart', configStart, function() {$(".fast-entry").hide();})
        .on('scrollstop', configStop, function() {$(".fast-entry").show();});
    $(".ill-right")
        .on('scrollstart', configStart, function() {$(".fast-entry").hide();})
        .on('scrollstop', configStop, function() {$(".fast-entry").show();});
    $("#timeList")
        .on('scrollstart', configStart, function() {$(".fast-entry").hide();})
        .on('scrollstop', configStop, function() {$(".fast-entry").show();});
    $("#groupList")
        .on('scrollstart', configStart, function() {$(".fast-entry").hide();})
        .on('scrollstop', configStop, function() {$(".fast-entry").show();});
}
var lightLock = function(){
    $("#lightLock").toggle(10);
}
var myAppointment = function(){
    recordLogs("快速入口-我的预约");
    window.location.href="appoint#/myAppointment";
}
var consultDoc = function(){
    recordLogs("快速入口-咨询医生");
    $.ajax({
        url:"wechatInfo/postInfoToWechat",// 跳转到 action
        async:true,
        type:'post',
        data:{},//得到需要分享页面的url
        cache:false,
        dataType:'json',
        success:function(data) {
            if(data=="true") {
                WeixinJSBridge.call('closeWindow');
            }else {
                init();
            }
        },
        error : function() {
        }
    });
}
var opinionFeedback = function(){
    window.location.href="baoFansCamp#/feedback";
}
function remindConsultClose(){
    $(".remind-consult-f").hide();
    $(".remind-consult").hide();
}
function remindConsultGet(){
    window.location.href = "";
}
function remindConsultNo(){
    $(".remind-consult-f").hide();
    $(".remind-consult").hide();
}