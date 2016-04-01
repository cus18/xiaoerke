angular.module('controllers', ['ionic']).controller('settlementCtrl', [
    '$scope','$state','$stateParams','SettlementInfo','GetDoctorInfo','CheckBind','$location','GetUserLoginStatus',
    function ($scope,$state,$stateParams,SettlementInfo,GetDoctorInfo,CheckBind,$location,GetUserLoginStatus) {
        if($stateParams.date=="")
        {
            $scope.date = moment().format('YYYY-MM-DD');
        }
        else
        {
            $scope.date = $stateParams.date;
        }

        $scope.$on('$ionicView.enter', function(){
            $scope.pageLoading = true;
            var routePath = "/ap/doctorBBBBBB" + $location.path();
            GetUserLoginStatus.save({routePath:routePath},function(data){
                $scope.pageLoading = false;
                if(data.status=="9") {
                    window.location.href = data.redirectURL;
                }else if(data.status=="8"){
                    window.location.href = data.redirectURL+"?targeturl="+routePath;
                }else{
                    $scope.pageLoading = true;
                    GetDoctorInfo.save({},function(data){
                        $scope.pageLoading = false;
                        if(data.doctorId!=""){
                            SettlementInfo.save({"doctorId":data.doctorId,"type":"appointment",
                                "date":$scope.date},function(data){
                                $scope.settlementAppointment = data.doctorSettlementAppointment;
                                $scope.totalAppPrice = data.totalAppPrice;
                                $scope.appNum = data.appNum;
                            })
                        }
                    });
                }
            })
        });

    }]);
