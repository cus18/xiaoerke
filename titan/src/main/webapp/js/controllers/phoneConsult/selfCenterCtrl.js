angular.module('controllers', ['ionic']).controller('selfCenterCtrl', [
        '$scope','$state','MyselfInfo','$ionicPopup','GetUserLoginStatus',
    '$rootScope','$location','resolveUserLoginStatus','$stateParams',
        function ($scope,$state,MyselfInfo,$ionicPopup,GetUserLoginStatus,$rootScope,$location,resolveUserLoginStatus,$stateParams) {
            $scope.title = "我的";
            $scope.pageLoading =false;
            $scope.myselfBindStatus=false;
           /*去绑定*/
            $scope.bindUserPhone = function(){
                resolveUserLoginStatus.events("selfCenter",",",{userPhoneNum:$stateParams.userPhoneNum},"","go");
            };
            /*我的资料*/
            $scope.myInfo = function(){
                window.location.href="appoint#/myInfo/"+$scope.myselfInfo.userPhoneNum;
               /* window.location.href = "/titan/appoint#/healthRecordSelectBaby/"+index+",1,"+$stateParams.conid;*/
            };
            /* 当前订单*/
            $scope.currentOrder = function(){
                resolveUserLoginStatus.events("currentOrderList","","","","notGo");
            };
            /* 预约挂号订单*/
            $scope.appointOrder = function(){
                resolveUserLoginStatus.events("appointOrder","","","","notGo");
            };
            /* 电话咨询订单*/
            $scope.phoneConsultOrder = function(){
                resolveUserLoginStatus.events("phoneConsultOrder","","","","notGo");
            };

           /* 我的资产*/
            $scope.accountBalance = function(){
                resolveUserLoginStatus.events("accountBalance","","","appoint#/accountBalance","notGo");
            };
           /* 健康档案*/
            $scope.healthRecord = function(){
                resolveUserLoginStatus.events("healthRecordIndex","","","appoint#/healthRecordIndex/","notGo");
            };
            var routePath = "/appointBBBBBB" + $location.path();
            GetUserLoginStatus.save({routePath:routePath},function(data){
                $scope.pageLoading = false;
                if(data.status=="9") {
                    $scope.myselfBindStatus=false;
                }else{
                    $scope.userName = data.userName;
                    $scope.userPhoneNum = data.userPhone;
                    // 个人订单基本信息
                    $scope.pageLoading = true;
                    var unBindUserPhoneNum = $rootScope.unBindUserPhoneNum;
                    if($scope.userPhoneNum!=undefined){
                        unBindUserPhoneNum = $scope.userPhoneNum;
                        $scope.myselfBindStatus=true;
                    }else{
                        $scope.myselfBindStatus=false;
                    }
                    MyselfInfo.save({unBindUserPhoneNum:unBindUserPhoneNum}, function(data) {
                        $scope.pageLoading = false;
                        $scope.myselfInfo = data;
                        $scope.patientId = data.patientId;
                        $scope.userName = data.userName;
                        $scope.accountFund = data.accountFund!=null?data.accountFund+"元":"";
                        $scope.bondSwitch = data.bondSwitch;
                        $scope.switchStatus = data.switchStatus;
                        $scope.memberNum = data.memberNum;
                    });
                }
            });
            $scope.$on('$ionicView.enter', function(){


            })
    }])
