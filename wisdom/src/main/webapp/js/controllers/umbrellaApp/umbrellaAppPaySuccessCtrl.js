angular.module('controllers',['ionic']).controller('umbrellaAppPaySuccessCtrl',
    ['$scope',function ($scope){
        $scope.lgLock = false;


        $scope.$on('$ionicView.enter',function () {
            localStorage.flag = true;
        });

        $scope.goJoin = function () {
            $scope.lgLock = true;
        }

       $scope.cancelProtocol = function () {
            $scope.lgLock = false;
       }


}]);
