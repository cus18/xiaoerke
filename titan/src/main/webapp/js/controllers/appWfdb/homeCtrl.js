angular.module('controllers', ['ionic','ngDialog']).controller('homeCtrl', [
    '$scope','$state','$stateParams','MyselfInfoAppointmentDetail',
    'OrderPayMemberServiceOperation','GetUserMemberService','$location','ngDialog',
    function ($scope,$state,$stateParams,MyselfInfoAppointmentDetail,OrderPayMemberServiceOperation,GetUserMemberService,$location,ngDialog) {
        /*查看我的奖品*/
        $scope.lookPrize=function(){
            $state.go('prizeList')
        }
        $scope.lookMyCard=function(){
            $state.go('myCard')
        }
        $scope.lookRules=function(){
            ngDialog.open({
                template: 'rules',
                scope:$scope,//这样就可以景星传递参数
                controller: ['$scope', '$interval', function ($scope, $interval) {}],
                className: 'ngdialog-theme-default ngdialog-theme-custom',
                width:'11.797333rem',
                height:'15.317333rem'
            });
        }
    }])
