angular.module('controllers', ['ionic','ngDialog']).controller('husGuideCtrl', [
    '$scope','$state','$stateParams','MyselfInfoAppointmentDetail',
    'OrderPayMemberServiceOperation','GetUserMemberService','$location','ngDialog',
    function ($scope,$state,$stateParams,MyselfInfoAppointmentDetail,OrderPayMemberServiceOperation,GetUserMemberService,$location,ngDialog) {
        $scope.goTest=function(){
            $state.go('husTest');
            alert(111)
        }
    }])