angular.module('controllers', ['ionic']).controller('lovePlanListCtrl', [
    '$scope','$state','$stateParams',
    function ($scope,$state,$stateParams) {

        $scope.title ="宝妈爱心接力";
        // 初始化假数据
        $scope.user=[
            {
                img:"http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/common%2Fbaodf_logo.jpg",
                name:"某女子",
                time:"7:00",
                price:5,
                words:"一起帮助这个可怜的孩子吧，大家一起行动起来"
            },
            {
                img:"http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/common%2Fbaodf_logo.jpg",
                name:"大白",
                time:"一天前",
                price:12,
                words:"心疼极了"
            },
            {
                img:"http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/common%2Fbaodf_logo.jpg",
                name:"某某",
                time:"一周前",
                price:8,
                words:"保佑他保佑他保佑他保佑他保佑他"
            },
            {
                img:"http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/common%2Fbaodf_logo.jpg",
                name:"王某某",
                time:"一个月前",
                price:88,
                words:""
            },
            {
                img:"http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/common%2Fbaodf_logo.jpg",
                name:"宝护伞",
                time:"一个月前",
                price:0,
                words:"好可怜好可怜好可怜好可怜好可怜好可怜好可怜好可怜好可怜好可怜好可怜好可怜好可怜好可怜"
            }
        ];
        $scope.goContribute = function () {
            window.location.href="http://localhost:8080/keeper/wxPay/patientPay.do?serviceType=lovePlanPay"
        };

        $scope.$on('$ionicView.enter', function(){

        })
    }]);

