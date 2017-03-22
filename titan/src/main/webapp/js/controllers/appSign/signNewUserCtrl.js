angular.module('controllers', ['ionic','ngDialog']).controller('signNewUserCtrl', [
    '$scope','$state','$stateParams','MyselfInfoAppointmentDetail',
    'OrderPayMemberServiceOperation','GetUserMemberService','$location','ngDialog','MakeNewInviteCard',
    function ($scope,$state,$stateParams,MyselfInfoAppointmentDetail,OrderPayMemberServiceOperation,GetUserMemberService,$location,ngDialog,MakeNewInviteCard) {
        $scope.oldOpenId=$stateParams.oldOpenId;
        $scope.market=$stateParams.marketer;
        MakeNewInviteCard.save({oldOpenId:$scope.oldOpenId,market:$scope.market},function(res){

        })
    }])