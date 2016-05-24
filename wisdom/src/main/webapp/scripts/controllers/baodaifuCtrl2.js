angular.module('controllers2', [])
    .controller('indexCtrl',['$scope','$state','$stateParams',
        function ($scope,$state,$stateParams) {

            $(function(){
                $(".img_weixin").hide();
                $('#mov_doctor').movingBoxes({
                    width: 1000,
                    reducedSize : 0.5,
                    startPanel : 2,
                    currentPanel : 'svccurrent',
                    fixedHeight:false,
                    hashTags: false,
                    wrap: false,
                    initialized: function(e, slider, tar){
                        //console.log("1",e);
                        //console.log("2",slider);
                        //console.log("3",tar);
                        slider.curWidth = 210;
                        slider.regWidth = 110;
                        slider.$curPanel.find('.doc_name').hide();
                        slider.$curPanel.find('.doc_name_info').css("font-size","20px");
                        slider.$curPanel.find('.doc_hosp').css("font-size","18px");

                },
                    initChange: function(e, slider, tar){
                        slider.curWidth = 210;
                        slider.regWidth = 110;
                        slider.$curPanel.find('.doc_name').show();
                        slider.$curPanel.find('.doc_name_info').css("font-size","0px");
                        slider.$curPanel.find('.doc_hosp').css("font-size","0px");
                    },
                    completed: function(e, slider){
                        console.log("1",e);
                        console.log("2",slider);
                        slider.curWidth = 210;
                        slider.regWidth = 110;
                        slider.$curPanel.find('.doc_name').hide();
                        slider.$curPanel.find('.doc_name_info').css("font-size","20px");
                        slider.$curPanel.find('.doc_hosp').css("font-size","18px");
                    }

                });
                $('#mov_hosp').movingBoxes({
                    width: 1000,
                    reducedSize : 0.5,
                    startPanel : 2,
                    currentPanel : 'svccurrent',
                    hashTags: false,
                    fixedHeight:false,
                    wrap: false,
                    initialized: function(e, slider, tar){
                        //console.log("1",e);
                        //console.log("2",slider);
                        //console.log("3",tar);
                        slider.curWidth = 250;
                        slider.regWidth = 125;
                        slider.$curPanel.find('.doc_name').css("margin-top","0px");
                        slider.$curPanel.find('.doc_name').css("font-size","18px");
                    },
                    initChange: function(e, slider, tar){
                        slider.curWidth = 250;
                        slider.regWidth = 125;
                        slider.$curPanel.find('.doc_name').css("margin-top","0px");
                        slider.$curPanel.find('.doc_name').css("font-size","15px");
                    },
                    completed: function(e, slider){
                        slider.curWidth = 250;
                        slider.regWidth = 125;
                        slider.$curPanel.find('.doc_name').css("margin-top","0px");
                        slider.$curPanel.find('.doc_name').css("font-size","18px");
                    }

                });
            })

            //底部微信二维码显示
            $(".img_1").mouseenter(function () {
                $(".img_weixin").show();

            });
            $(".img_1").mouseout(function () {
                $(".img_weixin").hide();

            });

            //联系我们
            $scope.goCallMine = function () {
                $state.go("callMine",{id:1});
            }


            //关于我们
            $scope.goGuanYu = function () {
                $state.go("callMine",{id:1});

            }

            //联系我们
            $scope.goLianXi = function () {
                $state.go("callMine",{id:2});
            }

            //服务协议
            $scope.goFuWu = function () {
                $state.go("callMine",{id:3});
            }

            //隐私保护
            $scope.goYinSi = function () {
                $state.go("callMine",{id:4});
            }





        }])
    //联系我们
    .controller('callMineCtrl',['$scope','$state','$stateParams',
        function ($scope,$state,$stateParams) {

            $(function () {
                $(".img_weixin").hide();
                //顶部菜单效果
                var $headBar = $('.index_title'), initTop = 0, isScroll = true;
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

                //$('#content_scroll').perfectScrollbar();
                // with vanilla JS!
                $('.mine_con').css({'height': '1000px'});
                Ps.initialize(document.getElementById('content_scroll'));

                $("#guanyumine").show();
                $("#lianximine").hide();
                $("#fuwuxieyi").hide();
                $("#yinsibaohu").hide();
                $("#GuanYu").css("color","#22c4c6");
                $("#LianXi").css("color","#333");
                $("#FuWu").css("color","#333");
                $("#YinSi").css("color","#333");
                //changeMine($("#guanyumine"),$("#lianximine"),$("#fuwuxieyi"),$("#yinsibaohu"),$("#GuanYu"),$("#LianXi"),$("#FuWu"),$("#YinSi"));


            });


            $scope.$on('$ionicView.enter', function() {
                if($stateParams.id==1){
                    $scope.goGuanYu();
                }else if($stateParams.id==2){
                    $scope.goLianXi();
                }else if($stateParams.id==3){
                    $scope.goFuWu();
                }else if($stateParams.id==4){
                    $scope.goYinSi();
                }

            });


            //首页
            $scope.goIndex = function () {
                 $state.go("index");
            }

            //关于我们
            $scope.goGuanYu = function () {

                changeMine($("#guanyumine"),$("#lianximine"),$("#fuwuxieyi"),$("#yinsibaohu"),$("#GuanYu"),$("#LianXi"),$("#FuWu"),$("#YinSi"));
            }

            //联系我们
            $scope.goLianXi = function () {

                changeMine($("#lianximine"),$("#guanyumine"),$("#fuwuxieyi"),$("#yinsibaohu"),$("#LianXi"),$("#GuanYu"),$("#FuWu"),$("#YinSi"));
            }

            //服务协议
            $scope.goFuWu = function () {
                changeMine($("#fuwuxieyi"),$("#guanyumine"),$("#lianximine"),$("#yinsibaohu"),$("#FuWu"),$("#GuanYu"),$("#LianXi"),$("#YinSi"));
            }

            //隐私保护
            $scope.goYinSi = function () {
                changeMine($("#yinsibaohu"),$("#guanyumine"),$("#lianximine"),$("#fuwuxieyi"),$("#YinSi"),$("#GuanYu"),$("#LianXi"),$("#FuWu"));
            }
            
            var changeMine = function (id1,id2,id3,id4,css1,css2,css3,css4) {
                id1.show();
                id2.hide();
                id3.hide();
                id4.hide();
                css1.css("color","#22c4c6");
                css2.css("color","#333");
                css3.css("color","#333");
                css4.css("color","#333");

            }
            
            

            //底部微信二维码显示
            $(".img_1").mouseenter(function () {
                $(".img_weixin").show();

            });
            $(".img_1").mouseout(function () {
                $(".img_weixin").hide();

            });
        }]);
