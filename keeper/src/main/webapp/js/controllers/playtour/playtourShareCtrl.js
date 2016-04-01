angular.module('controllers', ['ionic']).controller('playtourShareCtrl', [
    '$scope','$state','$stateParams',
    function ($scope,$state,$stateParams) {

        var shareimgList= ["http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/playtour%2Fsharewubiaoti.png",//无心意评价分享
        "http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/playtour%2Fsharefuceng.png",//收到心意分享
        "http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/playtour%2Fsharefuceng_1.png"];//没有心意只有评价分享
        var imgType = 3;

        $scope.$on('$ionicView.enter', function(){
            $scope.sharetou = false;
            $scope.shareimg = false;//分享图片


        });

        //显示分享图层
        $scope.goShare = function () {
            $scope.sharetou = true;
            $scope.shareimg = true;
            if(imgType==1){
                $scope.shareImg = shareimgList[0];
            }else if(imgType==2){
                $scope.shareImg = shareimgList[1];
            }else{
                $scope.shareImg = shareimgList[2];
            }

        }

        //取消分享图层
        $scope.cancleShare = function(){
            $scope.sharetou = false;
            $scope.shareimg = false;//收到心意分享图片

        }

    }]);