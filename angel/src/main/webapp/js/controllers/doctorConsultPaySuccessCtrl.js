angular.module('controllers', ['luegg.directives','ngFileUpload'])
    .controller('doctorConsultPaySuccessCtrl', ['$scope','$stateParams',
        function ($scope,$stateParams) {
            $scope.consultTime = $stateParams.consultTime;
            $scope.thirdName = $stateParams.consultTime;
            $scope.doctorConsultPaySuccess = function () {
                if($scope.consultTime == "1") {
                    $scope.consultTime = "30分钟";
                }else if ($scope.consultTime == "2") {
                    $scope.consultTime = "24小时";
                }else {
                    $scope.consultTime = "一个月";
                }


            };
            $scope.closeDoctorConsultPaySuccess = function () {
                if( $scope.thirdName=="GuoWei"){
                   //window.location.href= "http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=77177,h5PayThird-GuoWei";
                   window.location.href= "http://s201.xiaork.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s201.xiaork.com/keeper/wechatInfo/getUserWechatMenId?url=77177,h5PayThird-GuoWei";
                }
                else{
                    wx.closeWindow();
                }

            };
            $scope.$on('$ionicView.enter', function(){
                $scope.thirdName=$stateParams.thirdName;
            });

        }])

