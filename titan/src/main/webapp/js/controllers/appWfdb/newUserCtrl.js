angular.module('controllers', ['ionic','ngDialog']).controller('newUserCtrl', [
    '$scope','$state','$stateParams','MyselfInfoAppointmentDetail',
    'OrderPayMemberServiceOperation','GetUserMemberService','$location','ngDialog','CreateInviteCardInfo',
    function ($scope,$state,$stateParams,MyselfInfoAppointmentDetail,OrderPayMemberServiceOperation,GetUserMemberService,$location,ngDialog,CreateInviteCardInfo) {
        $scope.oldOpenId=$stateParams.oldFriendInfo.oldOpenId;
        $scope.market=$stateParams.oldFriendInfo.marketer;
        CreateInviteCardInfo.save({oldOpenId:$scope.oldOpenId,market:$scope.market},function(res){
            $scope.information=res;
        })
    }])
