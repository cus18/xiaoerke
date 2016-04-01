angular.module('controllers', ['ionic']).controller('nutritionPyramidCtrl', [
    '$scope','$state','$stateParams',
    function ($scope,$state,$stateParams) {

        $scope.scrollNum=0;
        $scope.num=0;
        //膳食宝塔
        $scope.pyramidList=[
            {
                img:"http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2FpyramidFood1.png",
                classify:"油盐类"
            },
            {
                img:"http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2FpyramidFood2.png",
                classify:"奶类"
            },
            {
                img:"http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2FpyramidFood3.png",
                classify:"鱼禽肉蛋类"
            },
            {
                img:"http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2FpyramidFood4.png",
                classify:"蔬菜类"
            },
            {
                img:"http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2FpyramidFood5.png",
                classify:"水果类"
            },
            {
                img:"http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2FpyramidFood6.png",
                classify:"谷薯类"
            },
            {
                img:"http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2FpyramidFood7.png",
                classify:"水"
            }
        ];


        $scope.nutritionIndex =function(){
            $state.go("nutritionIndex");
        };


        $scope.$on('$ionicView.enter', function(){
            $(".box2").animate({left:"10px",right:"10px",top:"20px",bottom:"20px"},2000,function(){
            });

        });


    }]);

