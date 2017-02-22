angular.module('controllers', ['ionic']).controller('myDoctor', [
        '$scope','$state','MyselfInfo','$ionicPopup','GetUserLoginStatus',
    '$rootScope','$location','resolveUserLoginStatus','$stateParams','RecordLogs','BabyCoinInit',
        function ($scope,$state,MyselfInfo,$ionicPopup,GetUserLoginStatus,$rootScope,$location,resolveUserLoginStatus,$stateParams,RecordLogs,BabyCoinInit) {
            $scope.title='我的医生';
            $scope.name='我是医生列表界面';
    }])
