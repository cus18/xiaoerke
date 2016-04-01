angular.module('controllers', [])
    .controller('baodaifuIndexCtrl',['$scope','$state','$stateParams','getArticleList',
        function ($scope,$state,$stateParams,getArticleList) {
            getArticleList.save({"id":"6edb1015e89d4d7a88515347bc3d7dd7","pageNo":1,"pageSize":2},function(data){
                if(data.articleList.length==0){
                    $scope.lockScroll="false";
                    alert("妈妈莫着急，部分内容正在拼命建设中！");
                }else{
                    $scope.articleList1 = data.articleList;
                }
                $scope.goContent1 = function(id){
                    $state.go("remenArticle",{id:id});
                }
            });

            getArticleList.save({"id":"a32998ad5ff64247a49cb2b4c7f5ac96","pageNo":1,"pageSize":3},function(data){
               if(data.articleList.length==0){
                    $scope.lockScroll="false";
                    alert("妈妈莫着急，部分内容正在拼命建设中！");
                }else{
                    $scope.articleList = data.articleList;
                }
                $scope.goContent = function(id){
                    $state.go("wendaArticle",{id:id});
                }
            });
        }])
    .controller('zixundaifuCtrl',['$scope','$sce',
        function ($scope,$sce) {
            $scope.zixundaifuList = [
                {"title":"怎样开始在线咨询？",
                    "content":"在微信中打开“宝大夫”服务号，点击左下角“小键盘”输入文字或语音，系统将自动转接在线医生，开启在线咨询对话。"+"<img src='http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/pc%2Fimg%2Faaa.png'/>"},
                {"title":"在线咨询的医生是怎样的资质？",
                    "content":"宝大夫在线咨询的签约医生全部为有临床5年以上的儿科医生，并承诺1分钟内回复您的问题。"},
                {"title":"对医生的回复不满意怎么办？",
                    "content":"当您对咨询回复的内容不满意时，可在会话结束后的评价中说出您的感受，我们会及时与您取得联系，并尽快帮您解决问题。"},
            ];
            $scope.zixundaifuClick = function(index){
                if(index==0){
                    $scope.zixundaifuIndex = 0;
                    $scope.content = $sce.trustAsHtml($scope.zixundaifuList[0].content);
                }
                if(index==1){
                    $scope.zixundaifuIndex = 1;
                    $scope.content = $sce.trustAsHtml($scope.zixundaifuList[1].content);
                }
                if(index==2){
                    $scope.zixundaifuIndex = 2;
                    $scope.content = $sce.trustAsHtml($scope.zixundaifuList[2].content);
                }
                console.log($scope.content);
            }

        }])
    //预约大夫
    .controller('yuyuedaifuCtrl',['$scope','$sce',
        function ($scope,$sce) {
            $scope.yuyuedaifuList = [
                {"title":"如何在宝大夫上“预约挂号”？",
                    "content":"第一步  进入宝大夫服务号，点击“预约大夫”菜单。"+"<img src='http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/pc%2Fimg%2Fbbb.png'/>"+"第二步  搜索或根据实际情况选择对应的医院、疾病、时间筛选目标医生。"+"<img src='http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/pc%2Fimg%2Fccc.png'/>"+"第三步  选择要挂号的医生，查看相关医院、医生的信息。"+"<img src='http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/pc%2Fimg%2Fddd.png'/>"
                +"第四步  根据医生的具体信息，选择自己的就诊时间，填写预约单，点击“立即预约”即可完成挂号。"+"<img src='http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/pc%2Fimg%2Feee.png'/>"},
                {"title":"挂号后，现场如何就医操作？",
                    "content":"挂号成功后，应在预约时间半小时前到达就诊地点，按照“订单详情页”页的“见医生流程”就诊，即可成功完成就诊。"},
                {"title":"如何取消预约挂号？",
                    "content":"取消预约挂号，需先进入“预约大夫”，点击右上角的图标（如图），进入我的预约："+"<img src='http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/pc%2Fimg%2Ffff.png'/>"+"在我的账户中查看“我的预约”，即可看到当前订单，按提示即可完成取消预约的操作。"
                    +"<img src='http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/pc%2Fimg%2Fggg.png'/>"},
                {"title":"就诊后如何进行评价？",
                    "content":"就诊后，需先进入“预约大夫”，点击右上角的图标（如图），进入我的预约："+"<img src='http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/pc%2Fimg%2Fhhh.png'/>"+"在我的账户中查看“待评价”，即可看到待评价订单，按提示即可完成评价的操作"+"<img src='http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/pc%2Fimg%2Fiii.png'/>"},
                {"title":"预约挂号服务是否收费？",
                    "content":"目前，宝大夫提供的“预约大夫”服务为免费预约服务，具体挂号收费以医生就诊挂号费用为准，即与医生当日实际出诊挂号费用一致。"},
            ];
            $scope.yuyuedaifuClick = function(index){
                if(index==0){
                    $scope.yuyuedaifuIndex = 0;
                    $scope.content = $sce.trustAsHtml($scope.yuyuedaifuList[0].content);
                }
                if(index==1){
                    $scope.yuyuedaifuIndex = 1;
                    $scope.content = $sce.trustAsHtml($scope.yuyuedaifuList[1].content);
                }
                if(index==2){
                    $scope.yuyuedaifuIndex = 2;
                    $scope.content = $sce.trustAsHtml($scope.yuyuedaifuList[2].content);
                }
                if(index==3){
                    $scope.yuyuedaifuIndex = 3;
                    $scope.content = $sce.trustAsHtml($scope.yuyuedaifuList[3].content);
                }
                if(index==4){
                    $scope.yuyuedaifuIndex = 4;
                    $scope.content = $sce.trustAsHtml($scope.yuyuedaifuList[4].content);
                }
            }

        }])
    //健康管理
    .controller('shuoCtrl',['$scope','$sce',
        function ($scope,$sce) {
            $scope.shuoList = [
                {"title":"如何开启健康管理服务？",
                    "content":"<p>第一步  进入宝大夫公众号</p>"+"<p>第二步  打开“宝粉之家”，点击进入“健康管理”</p>"+"<p>第三步  点击“便秘管理”或“营养管理”之后参加管理便可开启</p>"},
                {"title":"健康管理适用的年龄段？",
                    "content":"<p>便秘管理暂时适用于1.5—3岁的宝宝</p>"+"<p>营养管理暂时适用于1—3岁的宝宝</p>"+"<p>（适用的年龄段范围只是暂时阶段，平台正在丰富其余各个年龄段的服务）</p>"},
                {"title":"营养管理的提问消息在哪里可以看到回复？",
                    "content":"您在提问页面提交了问题后，宝大夫营养师和营养保健医生会在24小时之内，通过“宝大夫公众号”直接给您回复。所以为了更快更准的解决您的问题，请您一定要关注宝大夫公众号。"},
            ];
            $scope.shuoClick = function(index){
                if(index==0){
                    $scope.shuoIndex = 0;
                    $scope.content = $sce.trustAsHtml($scope.shuoList[0].content);
                }
                if(index==1){
                    $scope.shuoIndex = 1;
                    $scope.content = $sce.trustAsHtml($scope.shuoList[1].content);
                }
                if(index==2){
                    $scope.shuoIndex = 2;
                    $scope.content = $sce.trustAsHtml($scope.shuoList[2].content);
                }
            };
        }])
    //热门知识
    .controller('remenCtrl',['$scope','$state','getArticleList',
        function ($scope,$state,getArticleList) {
            getArticleList.save({"id":"6edb1015e89d4d7a88515347bc3d7dd7","pageNo":1,"pageSize":10},function(data){
                if(data.articleList.length==0){
                    $scope.lockScroll="false";
                    alert("妈妈莫着急，部分内容正在拼命建设中！");
                }else{
                    $scope.articleList = data.articleList;
                }
                $scope.goContent = function(id){
                    $state.go("remenArticle",{id:id});
                }
            });
        }])

    .controller('remenArticleCtrl',['$scope','$stateParams','GetArticleDetail','$sce','getArticleList','$state',
        function ($scope,$stateParams,GetArticleDetail,$sce,getArticleList,$state) {
            $scope.pageLoading=true;
            GetArticleDetail.save({"id":$stateParams.id,"generalize":"GW"},function(data){
                $scope.articleContent =$sce.trustAsHtml(data.articleDetail.content);
                $scope.description=data.article.description;
                $scope.contentTitle=data.article.title;
                $scope.contentAuthor=data.article.author;
                $scope.contentHits=data.article.hits;
                $scope.contentShare=data.article.share;
                $scope.contentId=data.article.id;
                $scope.comment = data.article.comment;
                $scope.articleReadCount = data.articleReadCount;
            });
            getArticleList.save({"id":"6edb1015e89d4d7a88515347bc3d7dd7","pageNo":1,"pageSize":4},function(data){
                if(data.articleList.length==0){
                    $scope.lockScroll="false";
                    alert("妈妈莫着急，部分内容正在拼命建设中！");
                }else{
                    $scope.articleList = data.articleList;
                }
                $scope.goContent1 = function(id){
                    $state.go("remenArticle",{id:id});
                }
            });

        }])
    //精彩问答
    .controller('wendaCtrl',['$scope','$state','getArticleList',
        function ($scope,$state,getArticleList) {
            getArticleList.save({"id":"a32998ad5ff64247a49cb2b4c7f5ac96","pageNo":1,"pageSize":10},function(data){
                if(data.articleList.length==0){
                    $scope.lockScroll="false";
                    alert("妈妈莫着急，部分内容正在拼命建设中！");
                }else{
                    $scope.articleList = data.articleList;
                }
                $scope.goContent = function(id){
                    $state.go("wendaArticle",{id:id});
                }
            });
        }])
    //精彩回答
    .controller('wendaArticleCtrl',['$scope','$stateParams','GetArticleDetail','$sce',
        function ($scope,$stateParams,GetArticleDetail,$sce) {
            $scope.pageLoading=true;
            GetArticleDetail.save({"id":$stateParams.id,"generalize":"GW"},function(data){
                $scope.articleContent =$sce.trustAsHtml(data.articleDetail.content);
                $scope.description=data.article.description;
                $scope.contentTitle=data.article.title;
                $scope.contentAuthor=data.article.author;
                $scope.contentHits=data.article.hits;
                $scope.contentShare=data.article.share;
                $scope.articleReadCount=data.articleReadCount;
                $scope.contentId=data.article.id;
                $scope.comment = data.article.comment;
            });
        }])
    //资讯列表
    .controller('zixunCtrl',['$scope','$state','getArticleList',
        function ($scope,$state,getArticleList) {
            getArticleList.save({"id":"06c294a772fe4c509647ab5ea298cb9d","pageNo":1,"pageSize":3},function(data){
                if(data.articleList.length==0){
                 $scope.lockScroll="false";
                 alert("妈妈莫着急，部分内容正在拼命建设中！");
                 }else{
                 $scope.articleList = data.articleList;
                 }
                $scope.goContent = function(id){
                    $state.go("content",{id:id});
                }
            });
            getArticleList.save({"id":"7a5addb3141848018e26ac53fedef3a0","pageNo":1,"pageSize":10},function(data){
                if(data.articleList.length==0){
                 $scope.lockScroll="false";
                 alert("妈妈莫着急，部分内容正在拼命建设中！");
                 }else{
                 $scope.articleList1 = data.articleList;
                 }
                $scope.goContent1 = function(id){
                    $state.go("hotthing",{id:id});
                }
            });


        }])
    //资讯文章
    .controller('contentCtrl',['$scope','$stateParams','GetArticleDetail','$sce',
        function ($scope,$stateParams,GetArticleDetail,$sce) {
            $scope.pageLoading=true;
            GetArticleDetail.save({"id":$stateParams.id,"generalize":"GW"},function(data){
                $scope.articleContent =$sce.trustAsHtml(data.articleDetail.content);
                $scope.description=data.article.description;
                $scope.contentTitle=data.article.title;
                $scope.contentAuthor=data.article.author;
                $scope.contentHits=data.article.hits;
                $scope.contentShare=data.article.share;
                $scope.contentId=data.article.id;
                $scope.articleReadCount=data.articleReadCount;
                $scope.comment = data.article.comment;
            });
        }])
    //实时热点
    .controller('hotthingCtrl',['$scope','$stateParams','GetArticleDetail','$sce',
        function ($scope,$stateParams,GetArticleDetail,$sce) {
            $scope.pageLoading=true;
            GetArticleDetail.save({"id":$stateParams.id,"generalize":"GW"},function(data){
                $scope.articleContent =$sce.trustAsHtml(data.articleDetail.content);
                $scope.description=data.article.description;
                $scope.contentTitle=data.article.title;
                $scope.contentAuthor=data.article.author;
                $scope.contentHits=data.article.hits;
                $scope.contentShare=data.article.share;
                $scope.contentId=data.article.id;
                $scope.articleReadCount=data.articleReadCount;
                $scope.comment = data.article.comment;
            });
        }])

    //联系我们
    .controller('contactUsCtrl',
        function ($scope) {
        })
    //产品信息
    .controller('productIntroductionCtrl',
        function ($scope) {
        })
    //隐私保护
    .controller('yinsibaohuCtrl',
        function ($scope) {
        })
    //服务条款
    .controller('fuwutiaokuanCtrl',
        function ($scope) {
        })
    //关于我们
    .controller('aboutCtrl',
    function ($scope) {
    })