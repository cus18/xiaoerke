angular.module('controllers', [])
    .controller('storyListCtrl',
    ['$scope',
        function ($scope) {
            var imgList = [
                "http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/storyList%2FstoryList_01.jpg",
                "http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/storyList%2FstoryList_02.jpg",
                "http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/storyList%2FstoryList_03.jpg",
                "http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/storyList%2FstoryList_04.jpg",
                "http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/storyList%2FstoryList_05.jpg",
                "http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/storyList%2FstoryList_06.jpg"
            ];
            $scope.showFlag={
                magnifyImg : false
                };
            $scope.storyListInit = function () {

            };
            $scope.previewImage = function (index) {
                $scope.showFlag.magnifyImg = !$scope.showFlag.magnifyImg;
                $scope.imageSrc = imgList[index];
            };
            $scope.hideImg = function () {
                $scope.showFlag.magnifyImg = false;
            };
        }])