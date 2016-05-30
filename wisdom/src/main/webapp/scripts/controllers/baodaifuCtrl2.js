angular.module('controllers2', [])
    .controller('indexCtrl',['$scope','$state','$stateParams','$http',
        function ($scope,$state,$stateParams,$http) {

            $scope.initial = function(){
                $(".img_weixin").hide();
                //底部微信二维码显示
                $(".img_1").mouseenter(function () {
                    $(".img_weixin").show();

                });
                $(".img_1").mouseout(function () {
                    $(".img_weixin").hide();
                });
                $(".curr").css("background-color","#22c4c6");
                setLog("GW_PV");
                var $headBar = $('.bdf_title'), initTop = 0, isScroll = true;
                $(window).on('scroll', function(e) {
                    var scrollY = $(this).scrollTop();
                    if(scrollY > 126){
                        if(scrollY > initTop){
                            $headBar.addClass('active');
                        }else{
                            $headBar.removeClass('active');
                        }
                    } else{
                        $headBar.removeClass('active');
                    }
                    initTop = scrollY;
                });

                setTimeout(function(){

                    $('#mov_doctor').movingBoxes({
                        width: 1000,
                        reducedSize : 0.7,
                        startPanel : 3,
                        currentPanel : 'svccurrent',
                        fixedHeight:false,
                        hashTags: false,
                        wrap: false,
                        initialized: function(e, slider, tar){
                            slider.$curPanel.find('.doc_name').hide();
                            slider.$curPanel.find('.doc_name_info').css("font-size","20px");
                            slider.$curPanel.find('.doc_name_info').css("margin-top","10px");
                            slider.$curPanel.find('.doc_hosp').css("font-size","18px");
                            slider.$curPanel.find('.doc_hosp').css("margin-top","6px");
                            slider.$curPanel.find('.index_com').css("font-size","16px");

                        },
                        initChange: function(e, slider, tar){
                            slider.$curPanel.find('.doc_name').show();
                            slider.$curPanel.find('.doc_name').css("margin-top","12px");
                            slider.$curPanel.find('.doc_name_info').css("font-size","0px");
                            slider.$curPanel.find('.doc_hosp').css("font-size","0px");
                            slider.$curPanel.find('.index_com').css("font-size","0px");
                        },
                        completed: function(e, slider){
                            slider.$curPanel.find('.doc_name').hide();
                            slider.$curPanel.find('.doc_name_info').css("font-size","20px");
                            slider.$curPanel.find('.doc_name_info').css("margin-top","10px");
                            slider.$curPanel.find('.doc_hosp').css("font-size","18px");
                            slider.$curPanel.find('.doc_hosp').css("margin-top","6px");
                            slider.$curPanel.find('.index_com').css("font-size","16px");
                        }

                    });
                    $('#mov_hosp').movingBoxes({
                        width: 1000,
                        reducedSize : 0.6,
                        startPanel : 3,
                        currentPanel : 'svccurrent',
                        hashTags: false,
                        fixedHeight:false,
                        wrap: false,
                        initialized: function(e, slider, tar){
                            //console.log("1",e);
                            //console.log("2",slider);
                            //console.log("3",tar);
                            //slider.curWidth = 250;
                            //slider.regWidth = 125;
                            slider.$curPanel.find('.doc_name').css("margin-top","24px");
                            slider.$curPanel.find('.doc_name').css("font-size","20px");
                        },
                        initChange: function(e, slider, tar){
                            //slider.curWidth = 250;
                            //slider.regWidth = 125;
                            slider.$curPanel.find('.doc_name').css("margin-top","24px");
                            slider.$curPanel.find('.doc_name').css("font-size","15px");
                        },
                        completed: function(e, slider){
                            //slider.curWidth = 250;
                            //slider.regWidth = 125;
                            slider.$curPanel.find('.doc_name').css("margin-top","24px");
                            slider.$curPanel.find('.doc_name').css("font-size","20px");
                        }

                    });
                },2000);
            }


            //顶部关于我们
            $scope.goCallMine = function () {
                setLog("GW_TOP_GYWM");
                $state.go("callMine",{id:1});
            }


            //关于我们
            $scope.goGuanYu = function (log) {
                setLog(log);
                $state.go("callMine",{id:1});

            }

            //联系我们
            $scope.goLianXi = function (log) {
                setLog(log);
                $state.go("callMine",{id:2});
            }

            //服务协议
            $scope.goFuWu = function (log) {
                setLog(log);
                $state.go("callMine",{id:3});
            }

            //隐私保护
            $scope.goYinSi = function (log) {
                setLog(log);
                $state.go("callMine",{id:4});
            }

            //微博
            $scope.goWeiBo = function (log) {
                setLog(log);
            }

            //记录日志
            function setLog(log){
                var pData = {logContent:encodeURI(log)};
                $http({method:'post',url:'util/recordLogs',params:pData});
            }

        }])
    //联系我们
    .controller('callMineCtrl',['$scope','$state','$stateParams','$http',
        function ($scope,$state,$stateParams,$http) {
            $scope.initial = function(){
                $(".curr").css("background-color","#22c4c6");
                setTimeout(function(){
                    $(".img_weixin").hide();
                    //顶部菜单效果
                    var $headBar = $('.bdf_title'), initTop = 0, isScroll = true;
                    $(window).on('scroll', function(e) {
                        var scrollY = $(this).scrollTop();
                        if(scrollY > 126){
                            if(scrollY > initTop){
                                $headBar.addClass('active');
                            }else{
                                $headBar.removeClass('active');
                            }
                        } else{
                            $headBar.removeClass('active');
                        }
                        initTop = scrollY;
                        //if(scrollY >= (3000 - $(window).height())){
                        //    $('.sy-connect .left').animate({'margin-left': '0'}, '1000');
                        //    $('.sy-connect .right').animate({'margin-right': '0'}, '1000');
                        //}
                    });

                    // with vanilla JS!
                    $('.mine_con').css({'height': '1000px'});
                    Ps.initialize(document.getElementById('content_scroll'));
                },300);

                /*$("#content_scroll").scroll(function(e){
                    var scrollY = $(this).scrollTop();
                    console.log("scd",scrollY);
                });*/

            }

            if($stateParams.id==1){
                changeMine($("#guanyumine"),$("#lianximine"),$("#fuwuxieyi"),$("#yinsibaohu"),$("#GuanYu"),$("#LianXi"),$("#FuWu"),$("#YinSi"));
            }else if($stateParams.id==2){
                changeMine($("#lianximine"),$("#guanyumine"),$("#fuwuxieyi"),$("#yinsibaohu"),$("#LianXi"),$("#GuanYu"),$("#FuWu"),$("#YinSi"));
                $(window).scrollTop(600);
            }else if($stateParams.id==3){
                changeMine($("#fuwuxieyi"),$("#guanyumine"),$("#lianximine"),$("#yinsibaohu"),$("#FuWu"),$("#GuanYu"),$("#LianXi"),$("#YinSi"));
            }else if($stateParams.id==4){
                changeMine($("#yinsibaohu"),$("#guanyumine"),$("#lianximine"),$("#fuwuxieyi"),$("#YinSi"),$("#GuanYu"),$("#LianXi"),$("#FuWu"));
            }


            //首页
            $scope.goIndex = function () {
                 $state.go("index");
            }

            //关于我们
            $scope.goGuanYu = function (log) {
                setLog(log);
                changeMine($("#guanyumine"),$("#lianximine"),$("#fuwuxieyi"),$("#yinsibaohu"),$("#GuanYu"),$("#LianXi"),$("#FuWu"),$("#YinSi"));
            }

            //联系我们
            $scope.goLianXi = function (log) {
                setLog(log);
                changeMine($("#lianximine"),$("#guanyumine"),$("#fuwuxieyi"),$("#yinsibaohu"),$("#LianXi"),$("#GuanYu"),$("#FuWu"),$("#YinSi"));
                $(window).scrollTop(600);
            }

            //服务协议
            $scope.goFuWu = function (log) {
                setLog(log);
                changeMine($("#fuwuxieyi"),$("#guanyumine"),$("#lianximine"),$("#yinsibaohu"),$("#FuWu"),$("#GuanYu"),$("#LianXi"),$("#YinSi"));
            }

            //隐私保护
            $scope.goYinSi = function (log) {
                setLog(log);
                changeMine($("#yinsibaohu"),$("#guanyumine"),$("#lianximine"),$("#fuwuxieyi"),$("#YinSi"),$("#GuanYu"),$("#LianXi"),$("#FuWu"));
            }

            //微博
            $scope.goWeiBo = function (log) {
                setLog(log);
            }


            function  changeMine(id1,id2,id3,id4,css1,css2,css3,css4) {
                id1.show();
                id2.hide();
                id3.hide();
                id4.hide();

                css1.css("color","");
                css2.css("color","");
                css3.css("color","");
                css4.css("color","");
                css1.css("color","#22c4c6");

                $(".mine_conright").animate({scrollTop:0},1000);
                $(window).scrollTop(0);
            }

            //function getQueryString(name) {
            //
            //    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
            //
            //    var r = window.location.search.substr(1).match(reg);
            //
            //    if (r != null) {
            //
            //        return unescape(r[2]);
            //
            //    }
            //
            //    return null;
            //
            //}

            //底部微信二维码显示
            $(".img_1").mouseenter(function () {
                $(".img_weixin").show();

            });
            $(".img_1").mouseout(function () {
                $(".img_weixin").hide();

            });

            //记录日志
            function setLog(log){
                var pData = {logContent:encodeURI(log)};
                $http({method:'post',url:'util/recordLogs',params:pData});
            }


        }]);
