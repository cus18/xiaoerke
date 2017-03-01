angular.module('controllers', ['ionic','ngDialog']).controller('oldUserCtrl', [
    '$scope','$state','$stateParams','MyselfInfoAppointmentDetail',
    'OrderPayMemberServiceOperation','GetUserMemberService','$location','ngDialog',
    function ($scope,$state,$stateParams,MyselfInfoAppointmentDetail,OrderPayMemberServiceOperation,GetUserMemberService,$location,ngDialog) {
        $scope.goHome=function(){
            $state.go('home');
        }
    }])
