angular.module('controllers', ['ionic']).controller('babyCoinTicketListCtrl', [
    '$scope','$state','$stateParams','sendMindCouponList',
    function ($scope,$state,$stateParams,sendMindCouponList) {

        $scope.commitLock=true;
        $scope.ticketData=[
            {
                name:"咨询5折券",
                coin:"50",
                pic:"http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/babyCoin/ticket5_1.png"
            },
            {
                name:"5元代金券",
                coin:"50",
                pic:"http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/babyCoin/ticket5_2.png"
            },
            {
                name:"10元代金券",
                coin:"100",
                pic:"http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/babyCoin/ticket10.png"
            },
            {
                name:"20元代金券",
                coin:"200",
                pic:"http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/babyCoin/ticket20.png"
            },
            {
                name:"30元代金券",
                coin:"300",
                pic:"http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/babyCoin/ticket30.png"
            },
            {
                name:"40元代金券",
                coin:"400",
                pic:"http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/babyCoin/ticket40.png"
            }
        ];
        //点击 赚取宝宝币
        $scope.earnBabyCoin = function () {
            $state.go("babyCoinInvitePage");
        };
        sendMindCouponList.save({},function (data) {
            $scope.couponList=data.mindCoupon;
        })


        //点击 立即兑换
        $scope.goExchange = function (id,name) {
            recordLogs("WSC_YHQ"+id);
            $state.go("babyCoinTicketPay",{'id':id,"name":name});
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
