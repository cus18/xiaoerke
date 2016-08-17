angular.module('controllers', ['ionic']).controller('umbrellaPublicizeCtrl', [
        '$scope','$state','$stateParams',
        function ($scope,$state,$stateParams) {
            $scope.title="宝大夫儿童家庭重疾互助计划";
            $scope.goFillInfo=function(){
                window.location.href = "http://s165.baodf.com/wisdom/umbrella#/umbrellaFillInfo/130300000/a";
            };
            $scope.goUmbrellaFirst=function(){
                window.location.href = "http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=umbrellaa_130300000";
            };


            $scope.$on('$ionicView.enter', function(){
                $.ajax({
                    type: 'POST',
                    url: "umbrella/firstPageDataCount",
                    contentType: "application/json; charset=utf-8",
                    success: function(result){
                        var count=result.count;
                        console.log("count",result.count);
                        $("#count").html(count);
                    },
                    dataType: "json"
                });
            });

            
    }]);