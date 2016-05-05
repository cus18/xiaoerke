angular.module('controllers', ['ionic']).controller('AppointmentConfirmCtrl', [
    '$scope','$state','$stateParams','DoctorAppointmentInfoDetail',
    'DoctorDetail','$rootScope','GetCheckOrder',
    'OrderAppointOperation','OrderFreePayOperation','GetMemberServiceStatus','$location',
    'getLastOrderBabyInfo','getBabyinfoList','saveBabyInfo','$filter','GetUserLoginStatus',
    function ($scope,$state,$stateParams,DoctorAppointmentInfoDetail,
              DoctorDetail,$rootScope,GetCheckOrder,OrderAppointOperation,OrderFreePayOperation,GetMemberServiceStatus,$location
        ,getLastOrderBabyInfo,getBabyinfoList,saveBabyInfo,$filter,GetUserLoginStatus) {
        $scope.title = "订单填写";
        $scope.title0 = "宝大夫（400-623-7120）";
        $scope.info = {};
        $scope.doctorId = "";
        $scope.select = "0";
        $scope.userPhoneError = "手机号格式错误";
        $scope. phoneErrorShow = false
        $scope.info.disabled = false
        $scope.parseInt = parseInt;
        $scope.haveOrder = false;
        $scope.serviceTipsLock = false;
        $scope.bookRemindLock = false;
        $scope.readDealLock = true;
        $scope.selectBabyLock = false;
        $scope.selectBabyInfoLock = false;
        $scope.info.checkUserBind = false;//检测用户是否绑定 用户设定手机号是否为readonly
        $scope.babyInfo	= [];
        $scope.name="";

        $scope.blackPhoneList =
            "13241773528,13263189543,15201462822,18511964919,15901492649,15010235519,13366250606,17744597981,13264570669," +
            "15010933341,15210995263,17090432533,13718063961,13699170036,17710217277,18519595514,13552613090,15247730725," +
            "18101200345,15110105640,15810606156,13021117987,18801202667,15001139555,18701034414,15711411099,13717580536,"+
            "13051447423,13120389727,13263189543,13321187297,13391561827,13511083148,13521040932,186181122629,17839258891,"+
            "13522131680,13522527881,13910439411,15110293295,15300080332,15810606156,15300248123,17744597981,18612072774,"+
            "17093885059,18101200345,18310584735,15900000358,13691213765,13703921686,18610104169,13120042069,13051397635," +
            "15110192017,18610104169,13691582976,13146869358,13661182966,15863547327,15811494155,13260190729,15110293359,13910029785";


        $scope.skip = function(a){
            if(a=="case"){
                console.log($("#birthday").val()+"输入病情时获取的生日信息");
                console.log($scope.birthdayLock);
            }
            /*   $("html,body").animate({ scrollTop: $("#"+a).offset().top }, 0);*/

        }

        //选择宝宝
        $scope.selectBaby = function(){
            $scope.selectBabyLock = true;
        }
        //取消选择宝宝
        $scope.cancelSelectBaby = function(){
            $scope.selectBabyLock = false;
        }
        //是否阅读用户协议
        $scope.readDeal = function(){
            if( $scope.readDealLock){
                $scope.readDealLock = false;
            }
            else{
                $scope.readDealLock = true;
            }
        }
        $scope.serviceTips = function(){
            $scope.serviceTipsLock = true;
        }
        $scope.cancelRemind=function(){
            $scope.bookRemindLock = false;
        }
        $scope.serviceTipsCancel = function(){
            $scope.serviceTipsLock = false;
        }

        $scope.selectInfo=function(){
            if( $scope.select =="0") {
                $scope.select = "1"
            }
            else{
                $scope.select = "0"
            }
        }

        $scope.checkPhone= function(){
            var phoneNumber = $scope.info.userPhone+"";
            if ( phoneNumber.match(/^1[3578]\d{9}$/)){
                $scope.info.show = true
                setTimeout(function() {
                    $scope.info.show = false
                }, 2000);
                $scope. phoneErrorShow = false;
                return;
            }
            $scope. phoneErrorShow = true
        }

        $scope.pageLoading = true;
        DoctorAppointmentInfoDetail.save({"doctorId":$stateParams.doctorId,"register_service_id":$stateParams.register_service_id,
            "date":$stateParams.date,"begin_time":$stateParams.begin_time,"end_time":$stateParams.end_time},function(data){
            $scope.pageLoading = false;
            $scope.date = $stateParams.date;
            $scope.begin_time = $stateParams.begin_time;
            $scope.end_time = $stateParams.end_time;
            $scope.begin_time_fee =  $scope.begin_time;
            $scope.begin_time_feeHour = parseInt($scope.begin_time.substr(0, 2))-5;
            $scope.begin_time_feeMin =   $scope.begin_time.substr(3, 2);

            $scope.needPay = $stateParams.needPay;

            $scope.week = moment($scope.date).format("E");
            if($scope.week==1){$scope.week="星期一"}
            else if($scope.week==2){$scope.week="星期二"}
            else if($scope.week==3){$scope.week="星期三"}
            else if($scope.week==4){$scope.week="星期四"}
            else if($scope.week==5){$scope.week="星期五"}
            else if($scope.week==6){$scope.week="星期六"}
            else if($scope.week==7){$scope.week="星期日"}
            //注意在此处考虑一个场景，用户在点击进入加号的是，此号被别人抢走，根据反馈，跳回到之前的医生详情页面
            $scope.info.userPhone = data.phone== null?"":parseInt(data.phone)
            if(data.phone>0){
                $scope.info.checkUserBind = true;
            }
            $scope.appointmentDetail = data;
            $scope.pageLoading = true;
            DoctorDetail.save({"doctorId":$stateParams.doctorId,
                "register_service_id":$stateParams.register_service_id},function(data){
                $scope.pageLoading = false;
                $scope.doctorDetail = data;
                $scope.doctorId = data.doctorId;
            })
        })

        $scope.appointment = function(){
            if($scope.blackPhoneList.indexOf($scope.info.userPhone)!=-1)
            {
                alert("您的手机号码预约过于频繁，暂时无法预约。\n如有疑问请拨打客服电话 4006237120");
                return;
            }
            $scope.pageLoading = true;
            GetCheckOrder.save({"phone":$scope.info.userPhone,"babyName":$scope.info.babyName,
                "reid":$stateParams.register_service_id},function(data){
                $scope.pageLoading = false;
                if(data.status=="78888"){
                    $scope.bookRemindLock = true;
                }else if (data.status=="1"){
                    alert("每天同一个手机号不能超过2个号，" +
                        "可在“我的订单”里查看订单详情；如有疑问，请联系客服：400-623-7120。");
                    history.back();
                }else if(data.status=="2"){
                    alert("每月同一个手机号不能超过5个号，" +
                        "可在“我的订单”里查看订单详情；如有疑问，请联系客服：400-623-7120。");
                    history.back();
                }else if(data.status=="3"){
                    var val="很抱歉，由于您今日已预约了"+
                        $scope.doctorDetail.doctorName + "医生，导致无法继续预约此医生，可在“我的订单”里查" +
                        "看订单详情；如有疑问，请联系客服：400-623-7120。"
                    alert(val);
                    history.back();
                }else if(data.status=="4"){
                    alert("很抱歉，由于您选择的就诊时间与已预约就诊时间间隔较短，导致无法预约，" +
                        "可在“我的订单”里查看订单详情；如有疑问，请联系客服：400-623-7120。");
                    history.back();
                }
                else {
                    /**
                     * 添加宝宝
                     */
                    var babyid = $scope.info.babyId;
                    if (typeof(babyid) == "undefined" || $scope.name!=$scope.info.babyName) {
                        var names = $scope.info.babyName;
                        var birthday = $("#birthday").val();
                        var sex = 1;
                        saveBabyInfo.get({"sex": sex, "name": encodeURI(names), "birthDay": birthday}, function (data) {
                            if (data.resultCode == '1') {
                                babyid = data.autoId;
                                $scope.info.disabled = true
                                $scope.pageLoading = true;
                                OrderAppointOperation.save({
                                    "sys_register_service_id": $stateParams.register_service_id,
                                    "action": {
                                        "status": "0",
                                        "babyName": $scope.info.babyName,
                                        "userPhone": $scope.info.userPhone + "",
                                        "illnessDesc": $scope.info.illness,
                                        "birthday": $('#birthday').val(),
                                        "babyId": babyid
                                    }
                                }, function (data) {
                                    $scope.pageLoading = false;
                                    var patient_register_service_id = data.patient_register_service_id;
                                    if (data.status == "0") {
                                        alert("亲，此号刚被人预约走，敬请谅解，还请另约其他号，谢谢");
                                        $state.go('appointmentFirst');
                                    } else {
                                        $rootScope.unBindUserPhoneNum = $scope.info.userPhone
                                        if ($stateParams.needPay == "true") {
                                            $scope.pageLoading = false;
                                            if ($scope.status == "4" || $scope.status == "8") {
                                                $state.go("appointmentUseMember", {
                                                    patient_register_service_id: patient_register_service_id,
                                                    date: $scope.date,
                                                    begin_time: $scope.begin_time,
                                                    end_time: $scope.end_time
                                                })
                                            }
                                            else if ($scope.status == "9" || $scope.status == "10") {
                                                alert("您好，您的会员服务和免费预约券已过期，宝大夫免费赠送您周会员服务。");
                                                $state.go("appointmentUseMember", {
                                                    patient_register_service_id: patient_register_service_id,
                                                    date: $scope.date,
                                                    begin_time: $scope.begin_time,
                                                    end_time: $scope.end_time
                                                })
                                            }
                                            else if ($scope.status == "11") {
                                                $state.go("appointmentUseMember", {
                                                    patient_register_service_id: patient_register_service_id,
                                                    date: $scope.date,
                                                    begin_time: $scope.begin_time,
                                                    end_time: $scope.end_time
                                                })
                                            }
                                            else if ($scope.status == "1" || $scope.status == "2" || $scope.status == "3" ||
                                                $scope.status == "5" || $scope.status == "6" || $scope.status == "7" || $scope.status == "cattle" || $scope.status == "22") {
                                                alert("您好，您的免费预约机会已用完，如需继续预约，请再次购买会员服务。");
                                                window.location.href = "/titan/pay/patientPay.do?patient_register_service_id="
                                                    + patient_register_service_id + "&chargePrice=15000";
                                            }
                                        }
                                        else if ($stateParams.needPay == "false") {
                                            $scope.pageLoading = true;
                                            //此处为用户扫医生码关注，不需要购买会员
                                            OrderFreePayOperation.get({"patient_register_service_id": patient_register_service_id}, function (data) {
                                                $state.go('appointmentSuccess', {"patient_register_service_id": patient_register_service_id});
                                            });
                                        }
                                        else if ($stateParams.needPay == "tehui") {
                                            if ($scope.status == "1" || $scope.status == "2" || $scope.status == "5"
                                                || $scope.status == "6" || $scope.status == "9" || $scope.status == "10") {
                                                alert("您好，您的会员服务已过期，如需继续预约，请再次购买会员服务。");
                                                window.location.href = "/titan/pay/patientPay.do?patient_register_service_id="
                                                    + patient_register_service_id + "&chargePrice=15000";
                                            }
                                            else {
                                                $scope.pageLoading = true;
                                                //此处为用户扫医生码关注，不需要购买会员
                                                OrderFreePayOperation.get({"patient_register_service_id": patient_register_service_id}, function (data) {
                                                    $state.go('appointmentSuccess', {"patient_register_service_id": patient_register_service_id});
                                                });
                                            }
                                        }
                                    }
                                })
                            }
                        });
                    } else {
                        $scope.info.disabled = true
                        $scope.pageLoading = true;

                        OrderAppointOperation.save({
                            "sys_register_service_id": $stateParams.register_service_id,
                            "action": {
                                "status": "0",
                                "babyName": $scope.info.babyName,
                                "userPhone": $scope.info.userPhone + "",
                                "illnessDesc": $scope.info.illness,
                                "birthday": $('#birthday').val(),
                                "babyId": babyid
                            }
                        }, function (data) {
                            $scope.pageLoading = false;
                            var patient_register_service_id = data.patient_register_service_id;
                            if (data.status == "0") {
                                alert("亲，此号刚被人预约走，敬请谅解，还请另约其他号，谢谢");
                                $state.go('firstPage/appointment');
                            } else {
                                $rootScope.unBindUserPhoneNum = $scope.info.userPhone
                                if ($stateParams.needPay == "true") {
                                    $scope.pageLoading = false;
                                    if ($scope.status == "4" || $scope.status == "8") {
                                        $state.go("appointmentUseMember", {
                                            patient_register_service_id: patient_register_service_id,
                                            date: $scope.date,
                                            begin_time: $scope.begin_time,
                                            end_time: $scope.end_time
                                        })
                                    }
                                    else if ($scope.status == "9" || $scope.status == "10") {
                                        alert("您好，您的会员服务和免费预约券已过期，宝大夫免费赠送您周会员服务。");
                                        $state.go("appointmentUseMember", {
                                            patient_register_service_id: patient_register_service_id,
                                            date: $scope.date,
                                            begin_time: $scope.begin_time,
                                            end_time: $scope.end_time
                                        })
                                    }
                                    else if ($scope.status == "11") {
                                        $state.go("appointmentUseMember", {
                                            patient_register_service_id: patient_register_service_id,
                                            date: $scope.date,
                                            begin_time: $scope.begin_time,
                                            end_time: $scope.end_time
                                        })
                                    }
                                    else if ($scope.status == "1" || $scope.status == "2" || $scope.status == "3" ||
                                        $scope.status == "5" || $scope.status == "6" || $scope.status == "7" || $scope.status == "cattle" || $scope.status == "22") {
                                        alert("您好，您的免费预约机会已用完，如需继续预约，请再次购买会员服务。");
                                        window.location.href = "/titan/pay/patientPay.do?patient_register_service_id="
                                            + patient_register_service_id + "&chargePrice=15000";
                                    }
                                }
                                else if ($stateParams.needPay == "false") {
                                    $scope.pageLoading = true;
                                    //此处为用户扫医生码关注，不需要购买会员
                                    OrderFreePayOperation.get({"patient_register_service_id": patient_register_service_id}, function (data) {
                                        $state.go('appointmentSuccess', {"patient_register_service_id": patient_register_service_id});
                                    });
                                }
                                else if ($stateParams.needPay == "tehui") {
                                    if ($scope.status == "1" || $scope.status == "2" || $scope.status == "5"
                                        || $scope.status == "6" || $scope.status == "9" || $scope.status == "10") {
                                        alert("您好，您的会员服务已过期，如需继续预约，请再次购买会员服务。");
                                        window.location.href = "/titan/pay/patientPay.do?patient_register_service_id="
                                            + patient_register_service_id + "&chargePrice=15000";
                                    }
                                    else {
                                        $scope.pageLoading = true;
                                        //此处为用户扫医生码关注，不需要购买会员
                                        OrderFreePayOperation.get({"patient_register_service_id": patient_register_service_id}, function (data) {
                                            $state.go('appointmentSuccess', {"patient_register_service_id": patient_register_service_id});
                                        });
                                    }
                                }
                            }
                        })
                    }
                }
            })
        }

        $scope.goToMemberService = function(){
            $state.go("memberService",{memberType:"week",action:"showInfo",register_service_id:""});
        }

        $scope.selectedBaby = function(index){
            var baby=$scope.babyInfo[index];
            //alert(baby.name);
            $scope.info.babyName=baby.name;
            $scope.name=baby.name;
            $scope.info.babyId=baby.id;
            $scope.info.birthday=$filter('date')(baby.birthday,"yyyy-MM-dd");
            if( $scope.info.birthday=="" || $scope.info.birthday==undefined){
                $scope.birthdayLock = false;
            }
            else{
                $scope.birthdayLock = true;
            }

            $scope.cancelSelectBaby();
        }
        Date.prototype.Format = function (fmt) { //author: meizz
            var o = {
                "M+": this.getMonth() + 1, //月份
                "d+": this.getDate(), //日
                "h+": this.getHours(), //小时
                "m+": this.getMinutes(), //分
                "s+": this.getSeconds(), //秒
                "q+": Math.floor((this.getMonth() + 3) / 3), //季度
                "S": this.getMilliseconds() //毫秒
            };
            if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
            for (var k in o)
                if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
            return fmt;
        }

        function loadDate(){
            var date = new Date(+new Date()+8*3600*1000).toISOString().replace(/T/g,' ').replace(/\.[\d]{3}Z/,'');
            $("#birthday").mobiscroll().date();
            //初始化日期控件
            var opt = {
                preset: 'date', //日期，可选：date\datetime\time\tree_list\image_text\select
                theme: 'default', //皮肤样式，可选：default\android\android-ics light\android-ics\ios\jqm\sense-ui\wp light\wp
                display: 'modal', //显示方式 ，可选：modal\inline\bubble\top\bottom
                mode: 'scroller', //日期选择模式，可选：scroller\clickpick\mixed
                lang:'zh',
                dateFormat: 'yyyy-mm-dd', // 日期格式
                setText: '确定', //确认按钮名称
                cancelText: '取消',//取消按钮名籍我
                dateOrder: 'yyyymmdd', //面板中日期排列格式
                dayText: '日', monthText: '月', yearText: '年', //面板中年月日文字
                showNow: false,
                nowText: "今",
                // startYear:1980, //开始年份
                // endYear:currYear //结束年份
                minDate: new Date(1980,0,1),
                maxDate: new Date(date.substring(0,4), date.substring(5,7)-1, date.substring(8,10)),
                onSelect: function (valueText) {
                    /* console.log("value",valueText);*/
                    $scope.birthdayLock = true; // 判断是否有有生日信息
                },
                onCancel: function (valueText, inst) {
                    /*  console.log("value",valueText);*/
                    if(valueText==null || valueText=="undefined"){
                        $scope.birthdayLock = false; // 判断是否有有生日信息
                    }
                    else{
                        $scope.birthdayLock = true; // 判断是否有有生日信息
                    }
                }
            };
            $("#birthday").mobiscroll(opt);
        }

        $scope.$on('$ionicView.enter',function() {
            loadDate();
            // 判断是否有宝宝信息
              if($("#birthday").val()==null || $("#birthday").val()==" " || typeof($("#birthday").val())=="undefined"){
                 $scope.birthdayLock = false; // 判断是否有有生日信息
             }
             else{
                 $scope.birthdayLock = true; // 判断是否有有生日信息
             }

            $scope.pageLoading = true;
            var routePath = "/appointBBBBBB" + $location.path();
            GetUserLoginStatus.save({routePath:routePath},function(data){
                $scope.pageLoading = false;
                if(data.status=="9") {
                    window.location.href = data.redirectURL;
                }else if(data.status=="8"){
                    window.location.href = data.redirectURL+"?targeturl="+routePath;
                }else{
                    GetMemberServiceStatus.save(function(data){
                        $scope.status = data.status;
                        if($scope.status=="11") {
                            alert("为感谢您对宝大夫的关注，现赠送短期会员服务体验卡；您可在会员期内享受1次免费名医预约，" +
                                "点击“个人中心”查看更多服务详情。");
                        }
                    });
                    getLastOrderBabyInfo.get(function(data){
                        console.log("dd",data);
                        if(data.lastOrderBabyInfo!=""){
                            $scope.info.babyName = data.lastOrderBabyInfo.name;
                            $scope.info.babyId=data.lastOrderBabyInfo.id;
                            $("#birthday").val($filter('date')(data.lastOrderBabyInfo.birthday,"yyyy-MM-dd"));
                            loadDate();
                           /* if(typeof(data.lastOrderBabyInfo.birthday)=="undefined"){
                                $scope.birthdayLock = false; // 判断是否有有生日信息
                            }
                            else{
                                $scope.birthdayLock = true; // 判断是否有有生日信息
                                console.log($("#birthday").val()+"后台获取有生日")
                            }*/
                        }
                        else{
                            /*$("#name").attr("readonly","readonly");*/
                            $scope.birthdayLock = false;
                        }
                    });
                    getBabyinfoList.save({"openId":""}, function (data){
                        if(data!=""){
                            var baby=data.babyInfoList;
                            var name=$scope.info.babyName;
                            if(baby.length==1&&typeof(name) == "undefined"){
                                $scope.selectBabyLock = false;
                                $scope.info.babyName=baby[0].name;
                                $scope.name=baby[0].name;
                                $scope.info.babyId=baby[0].id;
                                $("#birthday").val($filter('date')(baby[0].birthday,"yyyy-MM-dd"));
                                //$("#name").attr("readonly","readonly");
                            }else if(baby.length>=2){
                                $scope.babyInfo	= baby;
                                $scope.selectBabyInfoLock = true;
                                //$("#name").attr("readonly","readonly");
                            }else{
                                loadDate();
                            }

                        }
                    });
                }
            })
        });
    }])
