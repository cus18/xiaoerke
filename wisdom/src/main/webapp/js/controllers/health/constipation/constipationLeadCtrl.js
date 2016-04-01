angular.module('controllers', ['ionic']).controller('constipationLeadCtrl', [
    '$scope','$state','$stateParams','$timeout',
    function ($scope,$state,$stateParams,$timeout) {

        $scope.experienceImg="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Flead53_experience1.png";
        $scope.slideHasChanged=function(index){
            $scope.index= index;
            /*$scope.myActiveSlide=index;*/

        };
        $scope.onTouch=function(){
            $scope.experienceImg="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Flead53_experience2.png";
        };
        $scope.onRelease=function(){
            $scope.experienceImg="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/constipation%2Flead53_experience1.png";
        };


    }]);

