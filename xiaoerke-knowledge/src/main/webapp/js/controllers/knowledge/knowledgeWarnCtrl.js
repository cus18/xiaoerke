angular.module('controllers', ['ionic']).controller('knowledgeWarnCtrl', [
        '$scope','$state','$stateParams','dailyRemind','$ionicSlideBoxDelegate',
        function ($scope,$state,$stateParams,dailyRemind,$ionicSlideBoxDelegate) {
            $scope.info = {};
            $scope.lock='false';
            $scope.myActiveSlide = 0;

            $scope.$on('$ionicView.enter', function(){
                //郑玉巧每日提醒
                $scope.pageLoading=true;
                dailyRemind.get({"birthDay":$stateParams.birthday},function(data){
                    $scope.pageLoading=false;
                    if($stateParams.index==0){
                        $ionicSlideBoxDelegate.slide(0);
                    }else{
                        $ionicSlideBoxDelegate.slide(1);
                    }
                    $scope.dailytodayTip = data.dailyRemind_todayTip;
                    $scope.dailyRemind_game = data.dailyRemind_game;
                });
            });
    }]);