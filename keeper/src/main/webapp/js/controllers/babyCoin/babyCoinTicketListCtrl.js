angular.module('controllers', ['ionic']).controller('babyCoinTicketListCtrl', [
    '$scope','$state','$stateParams','sendMindCouponList',
    function ($scope,$state,$stateParams,sendMindCouponList) {

        $scope.commitLock=true;

        sendMindCouponList.save({},function (data) {
            $scope.couponList=data.mindCoupon;
        })


        //点击 立即兑换
        $scope.goExchange = function (id) {
            $state.go("babyCoinTicketPay",{'id':id});
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
