angular.module('controllers', ['ionic']).controller('babyGameFirstCtrl', [
        '$scope','$state','$stateParams','ConsultReconnection',
        function ($scope,$state,$stateParams,ConsultReconnection) {
            $scope.title = "连接通话";
            $scope.pageLoading =false;
            $scope.phone = $stateParams.phone
            $scope.doctorName = $stateParams.doctorName
            $scope.doctorId = $stateParams.doctorId
            $scope.phoneConsultaServiceId = $stateParams.phoneConsultaServiceId

            $scope.$on('$ionicView.enter', function(){
                ConsultReconnection.get({phoneConsultaServiceId:$stateParams.phoneConsultaServiceId},function(data){
                    if("000000" == data.statusCode){
                        wx.closeWindow();
                    }else{
                        alert("通话已过期")
                    }

                })
            });


    }])
