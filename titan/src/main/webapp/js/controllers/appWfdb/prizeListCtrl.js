angular.module('controllers', ['ionic']).controller('prizeListCtrl', [
    '$scope','$state','$stateParams','MyselfInfoAppointmentDetail',
    'OrderPayMemberServiceOperation','GetUserMemberService','$location','$http',
    function ($scope,$state,$stateParams,MyselfInfoAppointmentDetail,OrderPayMemberServiceOperation,GetUserMemberService,$location,$http) {
       /*查看代金券*/
        $scope.lookVoucher=function(x){
            console.log(x)
        }
    }])
