angular.module('controllers', ['ionic']).controller('EvaluateDoctorSuccessCtrl', [
        '$scope','$stateParams','resolveUserLoginStatus','$location',
        function ($scope,$stateParams,resolveUserLoginStatus,$location) {
            $scope.title = "评价医生成功";
            $scope.title0 = "宝大夫（400-623-7120）";
            $scope.use= "说明建档的好处（比如免费咨询一次；二次就诊申请加号；基于电子档案的诊后服务等等）"
            $scope.file = "立即建档"
            $scope.info = "您获得一个新的邀请码"
            $scope.tips = "温馨提示"
            $scope.tips1 = "1.此邀请码可用于自己预约或转借他人使用。"
            $scope.tips2 = "2.邀请码只能使用一次。"
            $scope.share = "立即分享"
            $scope.know = "我知道了"
            $scope.patientId = $stateParams.patientId;

            $scope.$on('$ionicView.enter', function(){
                resolveUserLoginStatus.events($location.path(),"","","","notGo");
            });
 }])