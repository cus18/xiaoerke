angular.module('controllers', ['ionic','ngDialog']).controller('husGuideCtrl', [
    '$scope','$state','$stateParams','MyselfInfoAppointmentDetail',
    'OrderPayMemberServiceOperation','GetUserMemberService','$location','ngDialog',
    function ($scope,$state,$stateParams,MyselfInfoAppointmentDetail,OrderPayMemberServiceOperation,GetUserMemberService,$location,ngDialog) {
        $scope.goTest=function(){
            recordLogs("YYHD_ZJLG_KSCS");
            $state.go('husTest');
        }
        var recordLogs = function(val){
            $.ajax({
                url:"util/recordLogs",// 跳转到 action
                async:true,
                type:'get',
                data:{logContent:encodeURI(val)},
                cache:false,
                dataType:'json',
                success:function(data) {
                },
                error : function() {
                }
            });
        }
        recordLogs("YYHD_ZJLG_SYCS");

    }])