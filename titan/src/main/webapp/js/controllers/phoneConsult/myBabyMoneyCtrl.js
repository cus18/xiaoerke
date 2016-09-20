angular.module('controllers', ['ionic']).controller('myBabyMoneyCtrl', [
    '$scope','$state','$stateParams','$http','$ionicPopup','$location','BabyCoinInit',
    function ($scope,$state,$stateParams,$http,$ionicPopup,$location,BabyCoinInit) {
        $scope.bobyMoneyInit = function(){
            BabyCoinInit.save({},function(data){
                $scope.babyMoney = data.babyCoinVo.cash;
                $scope.babyCoinRecordVos = data.babyCoinRecordVos;
            })
            recordLogs("ZXYQ_GRZX_BBB");
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
        };

    }])
