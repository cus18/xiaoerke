angular.module('controllers', ['ionic']).controller('insuranceOrderDetailCtrl', [
    '$scope','$state','$stateParams','$http','getInsuranceRegisterServiceById','$location','GetUserLoginStatus',
    function ($scope,$state,$stateParams,$http,getInsuranceRegisterServiceById,$location,GetUserLoginStatus) {

        $scope.num = "";
        $scope.service = "current";
        $scope.insurance={};
        $scope.parRemindLock =false;

        //日志
        var setLog = function (item) {
            var pData = {logContent:encodeURI(item)};
            $http({method:'post',url:'util/recordLogs',params:pData});
        }

        $scope.$on('$ionicView.enter', function(){
            var routePath = "/insuranceBBBBBB" + $location.path();
            GetUserLoginStatus.save({routePath:routePath},function(data){
                if(data.status=="9") {
                    window.location.href = data.redirectURL;
                } else if(data.status=="8"){
                    window.location.href = ata.redirectURL+"?targeturl="+routePath;
                }else {
                    getInsuranceRegisterServiceById.save({"id":$stateParams.id}, function (data){
                        $scope.insurance=data.InsuranceRegisterService;
                        if( $scope.insurance.insuranceType==3){
                            setLog("FYB_DDXQ");
                        }

                    });
                }
            });
        });

        // 我要领补贴提醒
        $scope.receiveMoney=function(){
            $scope.parRemindLock =true;
            var pData = {logContent:encodeURI("SZKB_DDXQ_LBT")};
            $http({method:'post',url:'util/recordLogs',params:pData});
        }

        //取消 补贴 提醒
        $scope.cancelPayRemind = function(){
            $scope.parRemindLock =false;
        };

    }]);


