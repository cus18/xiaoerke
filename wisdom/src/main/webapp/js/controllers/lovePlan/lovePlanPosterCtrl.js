angular.module('controllers', ['ionic']).controller('lovePlanPosterCtrl', [
    '$scope','$state','$stateParams',
    function ($scope,$state,$stateParams) {

        $scope.title ="宝妈爱心接力";

        $scope.$on('$ionicView.enter', function(){
            $.ajax({
                url:"umbrella/getOpenid",// 跳转到 action
                async:true,
                type:'post',
                cache:false,
                dataType:'json',
                success:function(data) {
                    if(data.openid=="none"){
                        window.location.href = "http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=32";
                    }
                },
                error : function() {
                }
            });
        })
    }]);

