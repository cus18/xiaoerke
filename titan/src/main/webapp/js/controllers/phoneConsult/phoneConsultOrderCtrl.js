angular.module('controllers', ['ionic']).controller('phoneConsultOrderCtrl', [
        '$scope','$state',
        function ($scope,$state) {
            $scope.title = "电话咨询订单";
            $scope.pageLoading =false;
            $scope.classifyItem =0;
            $scope.classifyItemList =["全部","待支付","待接听","待评价","待分享","已完成","已取消"];

           /* 伪数据*/
            $scope.orderInfo=[
                {
                    doctorName:"刘晓雁",
                    position:"主任医师、教授",
                    hospital:"首都儿科研究所",
                    date:"03/24",
                    time:"09:20-09:40",
                    status:"待就诊",
                    classify:"phone"
                },
                {
                    doctorName:"刘雁",
                    position:"主任医师",
                    hospital:"北京中医医院",
                    date:"03/24",
                    time:"09:20-09:40",
                    status:"待支付",
                    classify:"ap"
                },
                {
                    doctorName:"李建",
                    position:"副主任医师",
                    hospital:"北京儿童医院",
                    date:"03/24",
                    time:"09:20-09:40",
                    status:"待评价",
                    classify:"phone"
                },
                {
                    doctorName:"刘晓雁",
                    position:"主任医师、副教授",
                    hospital:"北京中日友好医院",
                    date:"03/24",
                    time:"09:20-09:40",
                    status:"待分享",
                    classify:"ap"
                },
                {
                    doctorName:"夏光明",
                    position:"教授",
                    hospital:"北京中医药大学附属东方医院",
                    date:"03/24",
                    time:"09:20-09:40",
                    state:"已取消",
                    classify:"ap"
                }
            ];
           /* 选择订单分类*/
            $scope.selectClassify = function(item){
                $scope.classifyItem =item;
            };
            $scope.orderDerail = function(item){
                $state.go("orderDetail")
            };


            $scope.$on('$ionicView.enter', function(){


            })
    }])
