angular.module('controllers', ['ionic']).controller('selfCenterCtrl', [
        '$scope','$state','MyselfInfo','$ionicPopup','GetUserLoginStatus',
    '$rootScope','$location','resolveUserLoginStatus','$stateParams','RecordLogs','BabyCoinInit',
        function ($scope,$state,MyselfInfo,$ionicPopup,GetUserLoginStatus,$rootScope,$location,resolveUserLoginStatus,$stateParams,RecordLogs,BabyCoinInit) {
            $scope.title = "我的";
            $scope.pageLoading =false;
            $scope.myselfBindStatus=false;
           /*去绑定*/
            $scope.bindUserPhone = function(){
                //resolveUserLoginStatus.events("selfCenter",",",{userPhoneNum:$stateParams.userPhoneNum},"","notGo");
                var routePath = "/phoneConsultBBBBBB" + $location.path();
                GetUserLoginStatus.save({routePath:routePath},function(data){
                    if(data.status=="9") {
                        window.location.href = data.redirectURL;
                    } else if(data.status=="8"){
                        window.location.href = data.redirectURL+"?targeturl="+routePath;
                    }else {

                    }
                })
            }

            /*我的资料*/
            $scope.myInfo = function(){
                window.location.href="appoint#/myInfo/"+$scope.myselfInfo.userPhoneNum;
               /* window.location.href = "/titan/appoint#/healthRecordSelectBaby/"+index+",1,"+$stateParams.conid;*/
            };
            /*我的预约券*/
            $scope.myService = function(){
                RecordLogs.get({logContent:encodeURI("DHZX_HYZX_HYFW")},function(){})
                window.location.href="appoint#/myService";
                /* window.location.href = "/titan/appoint#/healthRecordSelectBaby/"+index+",1,"+$stateParams.conid;*/
            };
            /*宝贝保*/
            $scope.myInsurance = function(){
                // RecordLogs.get({logContent:encodeURI("DHZX_HYZX_HYFW")},function(){})
                window.location.href="http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=29";
            };
            /* 当前订单*/
            $scope.currentOrder = function(){
                RecordLogs.get({logContent:encodeURI("DHZX_HYZX_DQDD")},function(){})
                resolveUserLoginStatus.events("currentOrderList","","","","notGo");
            };
            /* 我的医生*/
            $scope.myDoctor = function(){
               $state.go('myDoctor');
            };
            /* 预约挂号订单*/
            $scope.appointOrder = function(){
                RecordLogs.get({logContent:encodeURI("DHZX_HYZX_YYGH")},function(){})
                resolveUserLoginStatus.events("appointOrder","","","","notGo");
            };
            /* 电话咨询订单*/
            $scope.phoneConsultOrder = function(){
                RecordLogs.get({logContent:encodeURI("FSS_YHD_GRZX_TWZX")},function(){})
                location.href="http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=umbrella";
                //resolveUserLoginStatus.events("phoneConsultOrder","","","","notGo");
            };

           /* 我的资产*/
            $scope.accountBalance = function(){
                resolveUserLoginStatus.events("accountBalance","","","appoint#/accountBalance","notGo");
            };
           /* 健康档案*/
            $scope.healthRecord = function(){
                resolveUserLoginStatus.events("healthRecordIndex","0","","appoint#/healthRecordIndex/","notGo");
            };

            var routePath = "/phoneConsultBBBBBB" + $location.path();
            GetUserLoginStatus.save({routePath:routePath},function(loginData){
                if(loginData.status=="20") {
                    MyselfInfo.save({unBindUserPhoneNum:$rootScope.unBindUserPhoneNum}, function(data) {
                        $scope.pageLoading = false;
                        $scope.myselfInfo = data;
                        $scope.patientId = data.patientId;
                        $scope.userName = data.userName;
                        $scope.accountFund = data.accountFund!=null?data.accountFund+"元":"";
                        $scope.bondSwitch = data.bondSwitch;
                        $scope.switchStatus = data.switchStatus;
                        $scope.memberNum = data.memberNum;
                        $scope.userPhoneNum = data.userPhone;
                        if($scope.userPhoneNum!=''){
                            $scope.myselfBindStatus=true;
                        }else{
                            $scope.myselfBindStatus=false;
                        }
                    });
                }else{
                    $scope.memberNum = "0";
                    $scope.accountFund = "0元";
                }
            })
            BabyCoinInit.save({},function(data){
               if(data.babyCoinVo==undefined){

               }else{
                   $scope.babyMoney = data.babyCoinVo.cash;
               }
            })

            $scope.$on('$ionicView.enter', function(){


            })
    }])
