angular.module('controllers', ['ionic','ngDialog']).controller('newUserCtrl', [
    '$scope','$state','$stateParams','MyselfInfoAppointmentDetail',
    'OrderPayMemberServiceOperation','GetUserMemberService','$location','ngDialog','CreateInviteCardInfo',
    function ($scope,$state,$stateParams,MyselfInfoAppointmentDetail,OrderPayMemberServiceOperation,GetUserMemberService,$location,ngDialog,CreateInviteCardInfo) {
        CreateInviteCardInfo.save({},function(res){
            console.log(res)
        })
    }])
