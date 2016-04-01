angular.module('controllers', ['ionic']).controller('antiDogUploadCtrl', [
    '$scope','$state','$stateParams',
    function ($scope,$state,$stateParams) {

        $scope.num = "";
        var img="http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/insurance%2FantiDog%2Fhospital_bill2.png";
        var mgList = [
            "http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/insurance%2FantiDog%2Fhospital_bill2.png",
            "http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/insurance%2FantiDog%2FIDCard2.png"
        ];


        /*预览图片*/
        $scope.previewPic = function(num){
            if(num==1){
                var img="http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/insurance%2FantiDog%2Fhospital_bill2.png";
                var mgList = [
                    "http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/insurance%2FantiDog%2Fhospital_bill2.png",
                    "http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/insurance%2FantiDog%2FIDCard2.png"
                ];
            }
            else{
                var img="http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/insurance%2FantiDog%2FIDCard2.png";
                mgList = [
                    "http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/insurance%2FantiDog%2FIDCard2.png",
                    "http://xiaoerke-remain-pic.oss-cn-beijing.aliyuncs.com/insurance%2FantiDog%2Fhospital_bill2.png"
                ];
            }
            wx.previewImage({
                current: img, // 当前显示图片的http链接
                urls:  mgList// 需要预览的图片http链接列表
            });
        };


        $scope.$on('$ionicView.enter', function(){

        })
    }]);

