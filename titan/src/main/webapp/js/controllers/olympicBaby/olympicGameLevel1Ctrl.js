angular.module('controllers', ['ionic']).controller('olympicGameLevel1Ctrl', [
        '$scope','$state','$timeout',
        function ($scope,$state,$timeout) {
            $scope.title = "奥运宝贝游戏第一关";
            $scope.btnLock =false;
            $scope.num =0;
            $scope.totalNum=0;
            $scope.countdown =3;
            var myTime = setInterval(function() {
                $scope.countdown--;
                $scope.$digest();
            }, 1000);
            $scope.startSwim = function(num){
                $scope.num=num;
                $scope.totalNum+= $scope.num;
                $scope.num++;
                console.log("num "+num);
                console.log(" $scope.totalNum "+ $scope.totalNum);
                $("#swimmer").animate({bottom:$scope.totalNum*2+"px"},0);
//$("#swimmer").css("bottom",$scope.num*5+"px");
                $timeout(function() {
                    $scope.btnLock =true;
                    alert("游戏时间结束");
                }, 10000);
            };


            $scope.$on('$ionicView.enter', function(){

            })
    }])
