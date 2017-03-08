angular.module('controllers', ['ionic','ngDialog']).controller('husResultCtrl', [
    '$scope','$state','$stateParams','MyselfInfoAppointmentDetail',
    'OrderPayMemberServiceOperation','GetUserMemberService','$location','ngDialog',
    function ($scope,$state,$stateParams,MyselfInfoAppointmentDetail,OrderPayMemberServiceOperation,GetUserMemberService,$location,ngDialog) {
        $scope.status1=true;
        $scope.status2=false;
        $scope.status3=false;
        $scope.status4=false;
    }])