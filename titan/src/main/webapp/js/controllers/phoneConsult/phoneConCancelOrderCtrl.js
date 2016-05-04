angular.module('controllers', ['ionic']).controller('phoneConCancelOrderCtrl', [
        '$scope','$state','$stateParams','OrderCancel',
        function ($scope,$state,$stateParams,OrderCancel) {
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
            $scope.cancelContent = "预约有误，重新预约";
            //取消原因
            $scope.select = function (item) {
                $("dd img").attr("src","http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/phoneConsult%2Fcancel.png");
                $("dd img").eq(item).attr("src","http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/phoneConsult%2Fcancel_1.png");
                $scope.cancelContent = $scope.cancelList[item].content;
                if(item==2){
                    $scope.lock = true;
                    $scope.cancelContent = $scope.info.reason;
                }
                else{
                    $scope.lock = false;
                }
            };

            $scope.cancel = function () {
                $scope.info.buttonLock = true;
                $scope.pageLoading = true;
                OrderCancel.save({phoneConsultaServiceId:$stateParams.consultPhone_register_service_id,
                    cancelReason:$scope.cancelContent},function(data){
                    if(data.status=="20")
                    {
                        alert("很抱歉，接听前5分钟无法取消预约；\n如需帮助，请联系客服：400-623-7120。");
                        $scope.pageLoading = false;
                        history.back();
                        return;
                    }
                    $scope.pageLoading = false;
                    $scope.info.buttonLock = false;
                    $state.go('phoneConCancelSuccess',{"price":data.resultState});
                })
            };
            $scope.$on('$ionicView.enter', function(){

            });
            
    }])
