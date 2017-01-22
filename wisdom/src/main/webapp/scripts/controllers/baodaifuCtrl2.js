angular.module('controllers2', [])
    .controller('indexCtrl',['$scope','$state','$stateParams','$http',
        function ($scope,$state,$stateParams,$http) {
            var bannerList = ["http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/pc2/bdf_banner1.png",
                "http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/pc2/bdf_banner3.png",
                "http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/pc2/bdf_banner2.png"];
            var num;
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

            num = 0;
            //banner轮播图
           /* angular.forEach(bannerList, function (value,index) {
                var li = '<li/>';
                $(".index_ban ul").append(li);
            });*/

            $(".index_ban ul li").eq(0).css("background",'url(http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/pc2/bdf_bandianxuanzhong.png) no-repeat center');
            /*var seti = setInterval(function () {
                num++;
                if(num==bannerList.length){
                    num = 0;
                }
                bannerImg();
            },8000);*/

            //点击banner左箭头
            $scope.goBannerLeft = function (event) {
                stopMao(event);
                if(num!=0){
                    num--;
                    bannerImg();
                }else{
                    //num = 0;
                    return;
                }
            }
            //点击banner右箭头
            $scope.goBannerright = function (event) {
                stopMao(event);
                if(num!=(bannerList.length-1)){
                    num++;
                    bannerImg();
                }else{
                    //num = bannerList.length-1;
                    return;
                }
            }


            //顶部关于我们
            $scope.goCallMine = function () {
                setLog("GW_TOP_GYWM");
                //clearInterval(seti);
                $state.go("callMine",{id:1});
            }

            //点击banner
            $scope.goBanner = function(){
                 if(num==(bannerList.length-1)){
                    //clearInterval(seti);
                     setLog("GW_BANNER_YHZ");
                    $state.go("doctorHelp");
                }
            }

            //关于我们
            $scope.goGuanYu = function (log) {
                //clearInterval(seti);
                setLog(log);
                $state.go("callMine",{id:1});

            }

            //联系我们
            $scope.goLianXi = function (log) {
                //clearInterval(seti);
                setLog(log);
                $state.go("callMine",{id:2});
            }

            //联系我们
            $scope.goLianXi2 = function (log) {
                //clearInterval(seti);
                setLog(log);
                $state.go("callMine",{id:5});
            }

            //服务协议
            $scope.goFuWu = function (log) {
               // clearInterval(seti);
                setLog(log);
                $state.go("callMine",{id:3});
            }

            //隐私保护
            $scope.goYinSi = function (log) {
              //  clearInterval(seti);
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

            function bannerImg(){
                $(".index_ban").css("background",'url("'+bannerList[num]+'") no-repeat center');
                $(".index_ban").css("background-size","cover");
                if(num==(bannerList.length-1)){
                    $(".index_ban").css("cursor","pointer");
                }else{
                    $(".index_ban").css("cursor","");
                }
                $(".index_ban ul li").eq(num).css("background",'url(http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/pc2/bdf_bandianxuanzhong.png) no-repeat center').siblings().css("background","");
            }

            function stopMao(event){
                var event = event || window.event;
                if (event && event.stopPropagation){
                    event.stopPropagation();
                }else {
                    event.cancelBubble = true;
                }
            }


        }])
    //联系我们
    .controller('callMineCtrl',['$scope','$state','$stateParams','$http',
        function ($scope,$state,$stateParams,$http) {
            var scrollTop;
            $scope.initial = function(){
                $(".curr").css("background-color","#22c4c6");
                $(".img_weixin").hide();
                setTimeout(function(){
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

                /*$('.mine_conright').scroll(function () {
                    var scr = $(this).scrollTop();
                    var scrollHeight = document.body.scrollHeight;
                    console.log("sc1",scr);
                    console.log("sc2",scrollHeight);

                });*/

            }

            if($stateParams.id==1){//首页顶部导航关于我们
                changeMine($("#guanyumine"),$("#lianximine"),$("#fuwuxieyi"),$("#yinsibaohu"),$("#GuanYu"),$("#LianXi"),$("#FuWu"),$("#YinSi"));
                $(window).scrollTop(0);
            }else if($stateParams.id==2){//医生、医院联系我们
                changeMine($("#lianximine"),$("#guanyumine"),$("#fuwuxieyi"),$("#yinsibaohu"),$("#LianXi"),$("#GuanYu"),$("#FuWu"),$("#YinSi"));
                $(window).scrollTop(1000);
            }else if($stateParams.id==3){
                changeMine($("#fuwuxieyi"),$("#guanyumine"),$("#lianximine"),$("#yinsibaohu"),$("#FuWu"),$("#GuanYu"),$("#LianXi"),$("#YinSi"));
                $(window).scrollTop(0);
            }else if($stateParams.id==4){
                changeMine($("#yinsibaohu"),$("#guanyumine"),$("#lianximine"),$("#fuwuxieyi"),$("#YinSi"),$("#GuanYu"),$("#LianXi"),$("#FuWu"));
                $(window).scrollTop(0);
            }else if($stateParams.id==5){
                changeMine($("#lianximine"),$("#guanyumine"),$("#fuwuxieyi"),$("#yinsibaohu"),$("#LianXi"),$("#GuanYu"),$("#FuWu"),$("#YinSi"));
                $(window).scrollTop(0);
            }

            //点击logo跳转到首页
            $scope.goLogo = function () {
                $state.go("index");
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
                $(window).scrollTop(360);
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


        }])
    //医互助
    .controller('doctorHelpCtrl',['$scope','$state','$stateParams','$http',
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

            }

            //点击logo跳转到首页
            $scope.goLogo = function () {
                $state.go("index");
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
            $scope.goLianXi2 = function (log) {
                setLog(log);
                $state.go("callMine",{id:5});
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