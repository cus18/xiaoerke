angular.module('controllers', ['ionic']).controller('handfootmouthPaySuccessCtrl', [
    '$scope','$state','$stateParams','$http','getInsuranceRegisterServiceById','$location','GetUserLoginStatus',
    function ($scope,$state,$stateParams,$http,getInsuranceRegisterServiceById,$location,GetUserLoginStatus) {

        $scope.insurance={};

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
                    });
                }
            });
        });

        $scope.goFirstPage = function (){
            var pData = {logContent:encodeURI("SZKB_GMCC_FHSY")};
            $http({method:'post',url:'util/recordLogs',params:pData});
            //$state.go('handfootmouthIndex');
            window.location.href = "firstPage/insurance";
        };

    }]);


