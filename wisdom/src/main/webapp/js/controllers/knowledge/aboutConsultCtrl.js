angular.module('controllers', ['ionic']).controller('aboutConsultCtrl', [
    '$scope',
    function ($scope) {
        $scope.editLock="false";
        $scope.Author = "false";
        $scope.$on('$ionicView.enter', function() {
        });
    }]);