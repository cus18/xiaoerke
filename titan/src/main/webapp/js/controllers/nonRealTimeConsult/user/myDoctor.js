angular.module('controllers', []).controller('myDoctor', [
    '$scope','$stateParams',
    function ($scope,$stateParams) {
        $scope.title='我的医生';
    }]);
