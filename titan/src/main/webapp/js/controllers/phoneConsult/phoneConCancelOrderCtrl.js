angular.module('controllers', ['ionic']).controller('phoneConCancelOrderCtrl', [
        '$scope','$state',
        function ($scope,$state) {
            $scope.title = "取消预约";
            $scope.lock = false;
            $scope.info = {};
            $scope.cancelList = [{
                img:"http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/phoneConsult%2Fcancel_1.png",
                content:"预约有误，重新预约"
            },{
                img:"http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/phoneConsult%2Fcancel.png",
                content:"临时有事，无法接听电话"
            },{
                img:"http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/phoneConsult%2Fcancel.png",
                content:"其他"
            }];
            var cancelContent = "预约有误，重新预约";
            //取消原因
            $scope.select = function (item) {
                $("dd img").attr("src","http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/phoneConsult%2Fcancel.png");
                $("dd img").eq(item).attr("src","http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/phoneConsult%2Fcancel_1.png");
                cancelContent = $scope.cancelList[item].content;
                if(item==2){
                    $scope.lock = true;
                    cancelContent = $scope.info.reason;
                }
                else{
                    $scope.lock = false;
                }
            };

            $scope.Cancel = function () {
                $state.go("phoneConCancelSuccess");
            };
            $scope.$on('$ionicView.enter', function(){

            });
            
    }])
