﻿angular.module('controllers', []).controller('olympicGameLevel2Ctrl', [
        '$scope','$state','$timeout','GetUserOpenId','SaveGameScore',
        function ($scope,$state,$timeout,GetUserOpenId,SaveGameScore) {
            $scope.title = "奥运宝贝-游戏第二关";
            $scope.olympicGameLevel2Init = function(){
                (function(){
                    var i = 1,
                        y = 1,
                        time1,
                        time2,
                        time3,
                        time4,
                        randomNub = 0,
                        bingo = false,
                        score = 0,
                        total = 20,
                        countDown = -1;
                    $('#score').html(score*5);
                    $('#surplus').html('还剩'+total+'个栏');
                    time4 = setInterval(function(){
                        countDown++;
                        if(countDown == '4'){
                            clearInterval(time4);
                            movingBackgroundRun();
                            $('#startAnimat').hide();
                        }
                        $('#startAnimat>img').hide();
                        $('#startAnimat>img').eq(countDown).show();
                    },1000)
                    function barrierRun() {
                        if(total == '0'){
                            clearTime();
                            $('#startAnimat').show()
                            $('#resultImg').show()
                            $('#spielergebnis').html(score*5);
                            if(!submitScore()){
                                $('#challengeAgain').remove();
                            }
                        }
                        var barrierLeft = (parseInt($('#barrier').css('left')) / parseInt($('#hurdlesBox').css('width')) * 100);
                        if (barrierLeft > 5 && barrierLeft < 25) {
                            if (bingo == false) {
                                clearTime();
                                $('#startAnimat').show()
                                $('#resultImg').show()
                                $('#spielergebnis').html(score*5);
                                if(!submitScore()){
                                    $('#challengeAgain').remove();
                                }
                            }
                        };
                        if (barrierLeft < -25 || isNaN(barrierLeft)) {
                            $('#barrier').remove();
                            if (randomNub != '0') {
                                if (randomNub == 1) {
                                    $("#athlete").before('<div class="barrier" id="barrier"></div>');
                                    total--;
                                    score++;
                                    $('#score').html(score*5);
                                    $('#surplus').html('还剩'+total+'个栏')
                                    randomNub = 0;
                                } else {
                                    randomNub--;
                                }
                            } else {
                                randomNub = random() + 1;
                            };
                        } else {
                            $('#barrier').css('left', barrierLeft - 3 + '%');
                        }
                    }

                    function movingBackgroundRun() {
                        time1 = setInterval(athleteRun, 150);
                        time2 = setInterval(function() {
                            $('#movingBackground').css('background-position', (y -= 10) + 'px ' + (0) + 'px')
                        }, 42);
                        time3 = setInterval(barrierRun, 42)
                    }

                    function random() {
                        return Math.floor(Math.random() * 50);
                    }

                    function athleteRun() {
                        $('#athlete').css('background-position', (169.5 * i) + 'px ' + (0) + 'px');
                        $('#scottpilgrim').css('background-position', (-108 * i) + 'px ' + (0) + 'px');
                        i++;
                        i == 9 ? i = 1 : i = i;
                    };

                    function clearTime() {
                        clearInterval(time1);
                        clearInterval(time2);
                        clearInterval(time3);
                        $("#jumpBtn").unbind();
                    };

                    function jump() {
                        bingo = true;
                        clearInterval(time1);
                        $("#jumpBtn").unbind();
                        $('#athlete').css('background-position', (169.5 * 7) + 'px ' + (0) + 'px');
                        $("#athlete").animate({
                            top: '-=200px',
                        }, 400);
                        $("#athlete").animate({
                            top: '+=200px',
                        }, 600);
                        setTimeout(function() {
                            time1 = setInterval(athleteRun, 150);
                            bingo = false;
                            $("#jumpBtn").bind("click", jump);
                        }, 850)
                    };

                    function submitScore() {
                        SaveGameScore.save({'openid':''+$scope.openid,'gameLevel':'2','gameScore':''+score*5},function(data){
                            if(data.gamePlayingTimes>=3){
                                return false
                            }else{
                                return true
                            }
                        });
                    };

                    var recordLogs = function(val){
                        $.ajax({
                            url:"util/recordLogs",// 跳转到 action
                            async:true,
                            type:'get',
                            data:{logContent:encodeURI(val)},
                            cache:false,
                            dataType:'json',
                            success:function(data) {
                            },
                            error : function() {
                            }
                        });
                    };
                    recordLogs('action_olympic_baby_tiwce_visit');
                    recordLogs('action_olympic_baby_tiwce_share');
                    $("#jumpBtn").bind("click", jump);
                    $('#challengeAgain').bind('click',function(){
                        location.reload();
                    });
                    $('#challengeMore').bind('click',function(){
                        window.location.href="olympicBaby#/olympicBabyFirst";
                    });
                    GetUserOpenId.get({},function (data) {
                        if(data.openid=="none"){
                            //window.location.href = "http://s251.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s251.baodf.com/keeper/wechatInfo/getUserWechatMenId?url=37";
                        }else{
                            $scope.openid = data.openid;
                        }
                    });
                }());
            };
    }])
