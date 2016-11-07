angular.module('controllers', ['ionic']).controller('MyselfFirstCtrl', [
    '$scope','$state','MyselfInfo','$ionicPopup','GetUserLoginStatus',
    '$rootScope','$location','resolveUserLoginStatus','$stateParams',
        function ($scope,$state,MyselfInfo,$ionicPopup,GetUserLoginStatus,$rootScope,$location,resolveUserLoginStatus,$stateParams) {

            $scope.pendPayment = function(){
                resolveUserLoginStatus.events("pendPayment","","","","notGo");
            }
            $scope.accountBalance = function(){
                resolveUserLoginStatus.events("accountBalance","","","","notGo");
            }
            $scope.toBeTreated = function(){
                resolveUserLoginStatus.events("toBeTreated","","","","notGo");
            }
            $scope.toBeEvaluated = function(){
                resolveUserLoginStatus.events("toBeEvaluated","","","","notGo");
            }
            $scope.myAppointment = function(){
                resolveUserLoginStatus.events("myAppointment","","","","notGo");
            }
            $scope.healthRecord = function(){
                resolveUserLoginStatus.events("healthRecordIndex","","","","notGo");
            }
            $scope.myService = function(){
                resolveUserLoginStatus.events("myService","","","","notGo");
            }
            $scope.myAttention = function(){
                resolveUserLoginStatus.events("myAttention","","","","notGo");
            }
            $scope.toBeShared = function(){
                resolveUserLoginStatus.events("toBeShared","","","","notGo");
            }
            // $scope.bindUserPhone = function(){
            //     resolveUserLoginStatus.events("myselfFirst",",",{userPhoneNum:$stateParams.userPhoneNum},"","go");
            // }


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


            $scope.$on('$ionicView.enter', function(){
                $scope.title = "个人中心"
                $scope.title0 = "宝大夫（400-623-7120）";
                $scope.myselfInfo = {};
                $scope.patientId = "";
                $scope.accountFund = "0";
                $scope.myslefBindStatus="false";

                $scope.pageLoading = true;
                var routePath = "/appointBBBBBB" + $location.path();
                GetUserLoginStatus.save({routePath:routePath},function(data){
                    $scope.pageLoading = false;
                    if(data.status=="9") {
                        $scope.myslefBindStatus="false";
                    }else{
                        $scope.userName = data.userName;
                        $scope.userPhoneNum = data.userPhone;
                        // 个人订单基本信息
                        $scope.pageLoading = true;
                        var unBindUserPhoneNum = $rootScope.unBindUserPhoneNum;
                        if($scope.userPhoneNum!=undefined){
                            unBindUserPhoneNum = $scope.userPhoneNum;
                            $scope.myslefBindStatus="true";
                        }else{
                            $scope.myslefBindStatus="false";
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
                    if(data.waitPay==null||data.waitPay == '0'){
                        var obj = document.getElementById("itemList");
                        obj.className = "item wait-item wait-item-noPay";
                    }else{
                        var obj = document.getElementById("itemList");
                        obj.className = "item wait-item";
                    }
                });
            });
    }])
