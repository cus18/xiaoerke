angular.module('controllers', ['ionic','ngDialog']).controller('newUserCtrl', [
    '$scope','$state','$stateParams','MyselfInfoAppointmentDetail',
    'OrderPayMemberServiceOperation','GetUserMemberService','$location','ngDialog','CreateInviteCardInfo',
    function ($scope,$state,$stateParams,MyselfInfoAppointmentDetail,OrderPayMemberServiceOperation,GetUserMemberService,$location,ngDialog,CreateInviteCardInfo) {
        $scope.oldOpenId=$stateParams.oldOpenId;
        $scope.market=$stateParams.marketer;
        CreateInviteCardInfo.save({oldOpenId:$scope.oldOpenId,market:$scope.market},function(res){
            $scope.information=res;
        })
    }])
