angular.module('controllers', []).controller('olympicGameLevel3Ctrl', [
        '$scope','$state','$timeout',
        function ($scope,$state,$timeout) {
            $scope.title = "奥运宝贝-游戏第三关";
            $scope.num = 0;
            $scope.totalNum = 0;
            $scope.time = 15;
            $scope.lookResultFloat = false;
            /*$(document).ready(function(){
                console.log( $scope.time);
                $(".buttonImg").click(function(){
                    //$scope.num++;
                    var i = 0;
                    console.log( $scope.time);
                    var time = setInterval(function () {
                        i++;
                        var num = -100 - 350 * i;
                        $('.runningBoy').css('background-position',num+'px');
                        if(i == 8){
                            clearInterval(time);
                            i = 0;
                            $('.runningBoy').css('background-position',-100+'px');
                        }
                        console.log(i);
                    },100);
                    $scope.totalNum = $scope.num;
                });
            });*/
            $scope.olympicGameLevel3 = function () {
                startTime();
                $('#buttonImg').click = function () {
                    console.log( $scope.time);
                    $scope.num++;
                    var i = 0;
                    var time = setInterval(function () {
                        i++;
                        var num = -100 - 350 * i;
                        $('.runningBoy').css('background-position',num+'px');
                        if(i == 8){
                            clearInterval(time);
                            i = 0;
                            $('.runningBoy').css('background-position',-100+'px');
                        }
                    },100);
                    $scope.totalNum = $scope.num;
                };
            };
            //定时器
            var startTime = function () {
                console.log('startTime',$scope.time);
               var time = setInterval(function () {
                   $scope.time--;
                   $scope.time =  $scope.time;
                   if($scope.time == 0){
                       clearInterval(time);
                       $scope.time = 15;
                   }
               },100);
                console.log( $scope.time);

            };
            //取消浮层
            $scope.cancelFloat = function () {
                $scope.lookResultFloat = false;
            };
            //$(".buttonImg").click(function(){var i = 0;var time = setInterval(function () {i++;var num = -100 - 350 * i;('.runningBoy').css('background-position',num+'px');if(i == 8){clearInterval(time);i = 0;$('.runningBoy').css('background-position',-100+'px');}console.log(i);},100);});
    }])
