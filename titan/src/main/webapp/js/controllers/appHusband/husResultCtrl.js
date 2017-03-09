angular.module('controllers', ['ionic','ngDialog']).controller('husResultCtrl', [
    '$scope','$state','$stateParams','MyselfInfoAppointmentDetail',
    'OrderPayMemberServiceOperation','GetUserMemberService','$location','ngDialog',
    function ($scope,$state,$stateParams,MyselfInfoAppointmentDetail,OrderPayMemberServiceOperation,GetUserMemberService,$location,ngDialog) {

        $scope.status1=false;
        $scope.status2=false;
        $scope.status3=false;
        $scope.status4=false;
        if(1 == $stateParams.id){
            $scope.status1=true;
        }else if(2 == $stateParams.id){
            $scope.status2=true;
        }else if(3 == $stateParams.id){
            $scope.status3=true;
        }else if(4 == $stateParams.id){
            $scope.status4=true;
        }


    }])