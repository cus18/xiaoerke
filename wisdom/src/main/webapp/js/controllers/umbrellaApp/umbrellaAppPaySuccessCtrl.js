angular.module('controllers',['ionic']).controller('umbrellaAppPaySuccessCtrl',
    ['$scope',function ($scope){
        $scope.lgLock = false;


        $scope.goJoin = function () {
            $scope.lgLock = true;
        }

       $scope.cancelProtocol = function () {
            $scope.lgLock = false;
       }


}]);
