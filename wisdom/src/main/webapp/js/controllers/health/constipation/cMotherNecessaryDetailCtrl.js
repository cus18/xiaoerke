angular.module('controllers', ['ionic']).controller('cMotherNecessaryDetailCtrl', [
    '$scope','$state','$stateParams','SaveShopping','$http',
    function ($scope,$state,$stateParams,SaveShopping,$http) {

        $scope.$on('$ionicView.enter', function() {
            $scope.necessaryNum=$stateParams.necessaryNum;
            if($scope.necessaryNum==0){

                var pData = {logContent:encodeURI("BMGL_32")};
                $http({method:'post',url:'util/recordLogs',params:pData});

                $scope.necessaryText= [
                    "宝宝便秘突然严重的时候",
                    "一些应急药就会派上用场",
                    "妈妈不用手忙搅乱也避免去医院或药店的麻烦",
                    "您至少要为宝宝准备一种哦~",
                ];
                $scope.necessaryDetailList=[
                    {
                        name:"乳果糖",
                        pic:"http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/plan%2Fpic%2Fdrug%2F1.1.jpg",
                        href:"http://item.yhd.com/item/44250923?tc=3.0.5.44250923.1&tp=51.%E4%B9%B3%E6%9E%9C%E7%B3%96%E5%8F%A3%E6%9C%8D%E6%B6%B2.124.1.1.L7aSd0S-10-Cj9ef&abtest=1.8_565_83&ti=SCBS"
                    },
                    {
                        name:"乳果糖",
                        pic:"http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/plan%2Fpic%2Fdrug%2F1.2.jpg",
                        href:"http://item.yhd.com/item/45236408?tc=3.0.5.45236408.2&tp=51.乳果糖口服液.124.2.1.L7aSd0S-10-Cj9ef&abtest=1.8_565_83&ti=CEVS"
                    },
                    {
                        name:"开塞露",
                        pic:"http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/plan%2Fpic%2Fdrug%2F2.1.jpg",
                        href:"http://item.yhd.com/item/48136305?tc=3.0.5.48136305.3&tp=51.开塞露儿童.124.3.3.L7aYnno-10-Cj9ef&abtest=1.8_565_83&ti=F7VN"
                    },
                    {
                        name:"开塞露",
                        pic:"http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/plan%2Fpic%2Fdrug%2F2.2.jpg",
                        href:"http://item.yhd.com/item/48644531?tc=3.0.5.48644531.7&tp=51.开塞露儿童.124.7.1.L7aYnno-10-Cj9ef&abtest=1.8_565_83&ti=GGWU"
                    },
                    {
                        name:"开塞露",
                        pic:"http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/plan%2Fpic%2Fdrug%2F2.3.jpg",
                        href:"http://item.yhd.com/item/47789596?tc=3.0.5.47789596.1&tp=51.开塞露儿童.124.1.1.L7aYnno-10-Cj9ef&abtest=1.8_565_83&ti=G23G"
                    },
                    {
                        name:"红霉素",
                        pic:"http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/plan%2Fpic%2Fdrug%2F3.1.jpg",
                        href:"http://item.yhd.com/item/58292273?tc=3.0.5.58292273.3&tp=51.红霉素眼膏.124.3.1.L8ANi7Y-10-ApfQO&ti=Z3KY"
                    },
                    {
                        name:"红霉素",
                        pic:"http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/plan%2Fpic%2Fdrug%2F3.2.jpg",
                        href:"http://item.yhd.com/item/55895854?tc=3.0.5.55895854.7&tp=51.红霉素眼膏.124.7.1.L8ANi7Y-10-ApfQO&ti=QEF5"
                    }
                ];

            }
            if($scope.necessaryNum==1){

                var pData = {logContent:encodeURI("BMGL_33")};
                $http({method:'post',url:'util/recordLogs',params:pData});

                $scope.necessaryText=[
                    "给宝宝做饭的这些食材是必须的",
                    "家里没有了可以随时买",
                    "您至少要为宝宝准备一种哦！"
                ];
                $scope.necessaryDetailList=[
                    {
                        name:"西兰花",
                        pic:"http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/西兰花",
                        href:"http://www.sfbest.com/html/products/23/1800022444.html#trackref=sfbest_s_￨ﾥ﾿￥ﾅﾰ￨ﾊﾱ_itemlist_0-saleNum:desc-1-1"
                    },
                    {
                        name:"油麦菜",
                        pic:"http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/油麦菜",
                        href:"http://www.sfbest.com/html/products/23/1800022464.html#trackref=sfbest_s_油麦菜_itemlist_0-saleNum:desc-1-1"
                    },
                    {
                        name:"小白菜",
                        pic:"http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/小白菜",
                        href:"http://www.sfbest.com/html/products/23/1800022461.html#trackref=sfbest_s_￥ﾰﾏ￧ﾙﾽ￨ﾏﾜ_itemlist_0-saleNum:desc-1-1"
                    },
                    {
                        name:"小米",
                        pic:"http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/小米",
                        href:"http://www.sfbest.com/html/products/31/1300030241.html#trackref=sfbest_s_￥ﾰﾏ￧ﾱﾳ_itemlist_0-saleNum:desc-1-1"
                    },
                    {
                        name:"柚子",
                        pic:"http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/柚子",
                        href:"http://www.sfbest.com/html/products/174/1800173675.html#trackref=sfbest_s_￯﾿ﾦ￯ﾾﾟ￯ﾾﾚ￯﾿ﾥ￯ﾾﾭ￯ﾾﾐ_itemlist_0-saleNum:desc-1-2"
                    },
                    {
                        name:"火龙果",
                        pic:"http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/火龙果",
                        href:"http://www.sfbest.com/html/products/58/1800057935.html#trackref=sfbest_s_￯﾿ﾧ￯ﾾﾁ￯ﾾﾫ￯﾿ﾩ￯ﾾﾾ￯ﾾﾙ￯﾿ﾦ￯ﾾﾞ￯ﾾﾜ_itemlist_0-saleNum:desc-1-3"
                    },
                    {
                        name:"燕麦片",
                        pic:"http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/燕麦片",
                        href:"http://www.sfbest.com/html/products/31/1300030242.html#trackref=sfbest_s_￧ﾇﾕ￩ﾺﾦ_itemlist_0-saleNum:desc-1-2"
                    },
                    {
                        name:"油菜",
                        pic:"http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/油菜",
                        href:"http://www.sfbest.com/html/products/23/1800022460.html#trackref=sfbest_s_￦ﾲﾹ￨ﾏﾜ_itemlist_0-saleNum:desc-1-1"
                    },
                    {
                        name:"红薯",
                        pic:"http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/红薯",
                        href:"http://www.sfbest.com/html/products/23/1800022442.html#trackref=sfbest_s_￧ﾺﾢ￨ﾖﾯ_itemlist_0-saleNum:desc-1-1"
                    },
                    {
                        name:"胡萝卜",
                        pic:"http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/胡萝卜",
                        href:"http://www.sfbest.com/html/products/23/1800022476.html#trackref=sfbest_s_￯﾿ﾨ￯ﾾﾃ￯ﾾﾡ￯﾿ﾨ￯ﾾﾐ￯ﾾﾝ￯﾿ﾥ￯ﾾﾍ￯ﾾﾜ_itemlist_0-saleNum:desc-1-2"
                    }
                ];

            }
            if($scope.necessaryNum==2){

                var pData = {logContent:encodeURI("BMGL_34")};
                $http({method:'post',url:'util/recordLogs',params:pData});

                $scope.necessaryText= [
                    "便盆要选宝宝专用的",
                    "不会误伤宝宝哦",
                    "宝宝喜欢的便盆会给宝宝增加去排便的乐趣",
                    "您至少要为宝宝准备一种哦",
                ];
                $scope.necessaryDetailList=[
                    {
                        name:"德国hape儿童门球宝宝益智创意木质",
                        pic:"http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/plan%2Fpic%2Fbedpan%2F2.jpg",
                        href:"http://item.jd.com/1099888.html"
                    } ,
                    {
                        name:"贝比特 儿童 坐便器 婴儿 马桶 宝宝 座便器",
                        pic:"http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/plan%2Fpic%2Fbedpan%2F3.jpg",
                        href:"http://item.jd.com/1801089708.html"
                    } ,
                    {
                        name:"意大利进口 OKBABY瑞莱斯 婴儿 儿童坐便器 宝宝小马桶 ",
                        pic:"http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/plan%2Fpic%2Fbedpan%2F4.jpg",
                        href:"http://item.jd.com/1481703068.html"
                    } ,
                    {
                        name:"世纪宝贝巴蒂多功能婴儿座便器宝宝坐便器儿童坐便器小孩马桶便盆",
                        pic:"http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/plan%2Fpic%2Fbedpan%2F5.jpg",
                        href:"http://item.jd.com/1667311321.html"
                    } ,
                    {
                        name:"宝贝时代 婴儿童坐便器 宝宝坐便器马桶圈 塑料尿便盆 环保大号座便器 ",
                        pic:"http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/plan%2Fpic%2Fbedpan%2F1.jpg",
                        href:"http://item.jd.com/1731443066.html"
                    } ,
                    {
                        name:"宝贝时代儿童坐便器 婴儿辅助马桶垫圈 ",
                        pic:"http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/plan%2Fpic%2Fbedpan%2F6.jpg",
                        href:"http://item.jd.com/1741380864.html"
                    } ,

                ];

            }
            if($scope.necessaryNum==3){

                var pData = {logContent:encodeURI("BMGL_35")};
                $http({method:'post',url:'util/recordLogs',params:pData});

                $scope.necessaryText=[
                    "宝宝通过看这些图书",
                    "会更好的理解排便的好处",
                    "更好的帮助宝宝主动排便",
                    "您至少要为宝宝准备一本哦",
                ];
                $scope.necessaryDetailList=[
                    {
                        name:"宝宝更健康便便绘本（套装全3册[2-6岁]",
                        pic:"http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/plan%2Fpic%2Fbook%2F1.jpg",
                        href:"http://item.jd.com/11786991.html"
                    },
                    {
                        name:"《你的便便在哪里？》",
                        pic:"http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/plan%2Fpic%2Fbook%2F2.jpg",
                        href:"http://item.jd.com/1705485639.html"
                    },
                    {
                        name:"宝宝便秘腹泻怎么办",
                        pic:"http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/plan%2Fpic%2Fbook%2F3.jpg",
                        href:"http://item.jd.com/1028359009.html"
                    },
                    {
                        name:"儿童情绪管理与性格培养绘本--拉便便好疼:给便秘孩子的健康指导",
                        pic:"http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/plan%2Fpic%2Fbook%2F4.jpg",
                        href:"http://product.dangdang.com/22789454.html"
                    },
                    {
                        name:"【乐乐趣绘本馆】便便大象",
                        pic:"http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/plan%2Fpic%2Fbook%2F5.jpg",
                        href:"http://item.jd.com/1767131903.html"
                    },
                    {
                        name:"咕噜噜，拉便便",
                        pic:"http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/plan%2Fpic%2Fbook%2F6.jpg",
                        href:"http://item.jd.com/11059086.html"
                    },
                    {
                        name:"臭臭王国的便便大战-小毛毛无字故事书",
                        pic:"http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/plan%2Fpic%2Fbook%2F7.jpg",
                        href:"http://item.jd.com/1318904257.html"
                    },
                    {
                        name:"兰兰坐便盆",
                        pic:"http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/plan%2Fpic%2Fbook%2F8.jpg",
                        href:"http://item.jd.com/1453339286.html"

                    },
                    {
                        name:"我要便便 ",
                        pic:"http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/plan%2Fpic%2Fbook%2F9.jpg",
                        href:"http://item.jd.com/1671572587.html"
                    },
                    {
                        name:"糊糊 臭臭 便便 球球",
                        pic:"http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/plan%2Fpic%2Fbook%2F10.jpg",
                        href:"http://item.jd.com/10025988095.html"
                    },
                    {
                        name:"便便超人",
                        pic:"http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/plan%2Fpic%2Fbook%2F11.jpg",
                        href:"http://item.jd.com/10023872651.html"
                    },
                    {
                        name:"如厕训练: 和小动物一起拉粑",
                        pic:"http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/plan%2Fpic%2Fbook%2F13.jpg",
                        href:"http://www.amazon.cn/如厕训练-和小动物一起拉粑/dp/B00CX9KUG0/ref=sr_1_5?ie=UTF8&qid=1450338621&sr=8-5&keywords=书宝宝便便"
                    }

                ];


            }
        });


        $scope.saveShopping = function(href){
            var pData = {logContent:encodeURI("BMGL_53")};
            $http({method:'post',url:'util/recordLogs',params:pData});
            SaveShopping.save({"type":$stateParams.necessaryNum,"href":href},function(data){

            })
        }

    }]);

