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
                        slider.curWidth = 225;
                        slider.regWidth = 110;
                        slider.$curPanel.find('.doc_name').hide();
                        slider.$curPanel.find('.doc_name_info').css("font-size","20px");
                        slider.$curPanel.find('.doc_hosp').css("font-size","18px");

                },
                    initChange: function(e, slider, tar){
                        slider.curWidth = 225;
                        slider.regWidth = 110;
                        slider.$curPanel.find('.doc_name').show();
                        slider.$curPanel.find('.doc_name_info').css("font-size","0px");
                        slider.$curPanel.find('.doc_hosp').css("font-size","0px");
                    },
                    completed: function(e, slider){
                        console.log("1",e);
                        console.log("2",slider);
                        slider.curWidth = 225;
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
                    },
                    initChange: function(e, slider, tar){
                        slider.curWidth = 250;
                        slider.regWidth = 125;
                    },
                    completed: function(e, slider){
                        slider.curWidth = 250;
                        slider.regWidth = 125;
                        slider.$curPanel.find('.doc_name').css("margin-top","0px");
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
                $state.go("callMine");
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



            });

            //首页
            $scope.goIndex = function () {
                 $state.go("index");
            }


            //底部微信二维码显示
            $(".img_1").mouseenter(function () {
                $(".img_weixin").show();

            });
            $(".img_1").mouseout(function () {
                $(".img_weixin").hide();

            });
        }]);
