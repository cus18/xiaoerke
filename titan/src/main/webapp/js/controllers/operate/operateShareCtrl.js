angular.module('controllers', ['ionic']).controller('operateShareCtrl', [
        '$scope','$state','$stateParams',
        function ($scope,$state,$stateParams) {

            $scope.$on('$ionicView.enter', function() {
                if ($stateParams.url != "") {
                    $scope.url = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+$stateParams.url;
                } else {
                    $scope.url = "http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/operate%2Fbaodf_share_code.jpg";
                }
            });
        }]);