angular.module('controllers', ['ionic']).controller('babyCoinTicketPayCtrl', [
    '$scope','$state','$stateParams',
    function ($scope,$state,$stateParams) {

        $scope.surePayLock=true; // 是否点击使用


        //点击 赚取宝宝币
        $scope.earnBabyCoin = function () {


        };
        //点击 使用
        $scope.selectUse = function () {
            $scope.surePayLock=!$scope.surePayLock;

        };
        //点击 确认兑换
        $scope.sureExchange = function () {

        };

        $scope.$on('$ionicView.enter', function(){

        });

        function recordLogs(val){
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


    }]);
