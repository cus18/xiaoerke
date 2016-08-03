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
            $scope.olympicBabyFirstInit = function(){

            };
            function ScrollImgLeft() {
                var speed = 50;
                var scroll_begin = document.getElementById("scroll_begin");
                var scroll_end = document.getElementById("scroll_end");
                var scroll_div = document.getElementById("scroll_div");
                scroll_end.innerHTML = scroll_begin.innerHTML;

                function Marquee() {
                    if (scroll_end.offsetWidth - scroll_div.scrollLeft <= 0) {
                        scroll_div.scrollLeft -= scroll_begin.offsetWidth;
                    } else {
                        scroll_div.scrollLeft++;
                    }
                }
                var MyMar = setInterval(Marquee, speed);
            };
            $scope.load= function(){
                ScrollImgLeft();
                $('#ruleBtn').bind('click', function() {
                    $('#ruleBox').show();
                });
                $('#ruleBox').bind('click', function(e) {
                    if (e.target.className != 'ruleBoxContent') {
                        $('#ruleBox').hide();
                    }
                });
            }
    }])
