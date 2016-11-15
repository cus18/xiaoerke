angular.module('controllers', ['ionic']).controller('picSpreadResultCtrl', [
    '$scope','$state','$stateParams','PicSpread',
    function ($scope,$state,$stateParams,PicSpread) {
        $scope.title ="图片传播";
        $scope.pageLoading =true;// 页面加载是否完成
        $scope.picUrl = "";

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
        recordLogs('TPCN_SHTP');

       //后台请求图片地址
        PicSpread.save({babyName:$stateParams.name},function (data) {
            console.log(data)
            $scope.picUrl = data.picImg;
            $scope.pageLoading =false;// 页面加载是否完成
        });
        $scope.tryAgain = function(){
            //后台请求图片地址
            recordLogs('TPCN_ZSYC');
            $scope.pageLoading =true;// 页面加载是否完成
            PicSpread.save({babyName:$stateParams.name},function (data) {
                console.log(data)
                $scope.picUrl = data.picImg;
                $scope.pageLoading =false;// 页面加载是否完成
            });
        };

        $scope.$on('$ionicView.enter', function() {


        });
    }]);

