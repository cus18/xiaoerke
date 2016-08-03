angular.module('controllers', []).controller('olympicBabyFirstCtrl', [
        '$scope','$state','$timeout',
        function ($scope,$state,$timeout) {
            $scope.title = "奥运宝贝-首页";
            //获取openId
            /*
            * $.ajax({
             url:"umbrella/getOpenid",// 跳转到 action
             async:true,
             type:'post',
             cache:false,
             dataType:'json',
             success:function(data) {
             if(data.openid=="none"){
             window.location.href = "http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=37";
             }
             },
             error : function() {
             }
             });*/

            
    }])
