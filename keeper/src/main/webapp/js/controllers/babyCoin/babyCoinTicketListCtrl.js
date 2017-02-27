angular.module('controllers', ['ionic']).controller('babyCoinTicketListCtrl', [
    '$scope','$state','$stateParams','sendMindCouponList','BabyCoinInit',
    function ($scope,$state,$stateParams,sendMindCouponList,BabyCoinInit) {

        $scope.commitLock=true;
        BabyCoinInit.save({},function(data){
            $scope.babyMoney = data.babyCoinVo.cash;
        })

        //点击 赚取宝宝币
        $scope.earnBabyCoin = function () {
            $state.go("babyCoinInvitePage");
        };
        sendMindCouponList.save({},function (data) {
            $scope.ticketData=data.mindCoupon;
        })


        //点击 立即兑换
        $scope.goExchange = function (id,name) {
            recordLogs("WSC_YHQ"+id);
            $state.go("babyCoinTicketPay",{'id':id,"name":name});
        };
        $scope.$on('$ionicView.enter', function(){


        });

        $scope.goToPayPage = function () {
            window.location.href= "http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=35";
        }

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
