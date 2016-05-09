angular.module('controllers', ['ionic']).controller('knowledgeArticleContentCtrl', [
    '$scope','$state','$stateParams','$timeout','GetArticleDetail','ArticleShareRecord','ArticleZanRecord',
    'SendWechatMessageToUser','$ionicScrollDelegate','$location','$anchorScroll','$sce','$ionicBackdrop',
    'listComment','saveComment',
    function ($scope,$state,$stateParams,$timeout,GetArticleDetail,ArticleShareRecord,ArticleZanRecord,
              SendWechatMessageToUser,$ionicScrollDelegate,$location,$anchorScroll,$sce,$ionicBackdrop,
              listComment,saveComment) {


        $scope.info={};
        $scope.attentionLock="false";
        $scope.editLock="false";
        $scope.Author = "false";
        var openId="";

        $scope.$on('$ionicView.enter', function() {

            $scope.contentId=$stateParams.contentId;//获取文档id
            $scope.generalize = $stateParams.generalize;
            $scope.zanImg = 'http://xiaoerke-knowledge-pic.oss-cn-beijing.aliyuncs.com/knowledge%2Farticle_zan_no.png';//点赞初始图片
            $scope.num=0;//点赞初始值
            $scope.lock1= "0";//点赞初始锁值
            $scope.bdf="false";//宝大夫扫描二维码提示
            $scope.lockzixun="false";
            $scope.lockyuyue="false";
            $scope.description="";
            $scope.articleNum="false";
            $scope.comm = "false";
            $scope.init($scope.contentId);

        });


        /**
         * 获取文章数据
         */
        $scope.init = function(id){
            $scope.pageLoading=true;
            GetArticleDetail.save({"id":id,"generalize":$stateParams.generalize},function(data){
                $scope.pageLoading=false;
                if(data.article.author!=""){
                    $scope.Author = "true";
                    $scope.author=data.article.author;
                }
                $scope.info.articleContent = $sce.trustAsHtml(angular.copy(data.articleDetail.content));
                $scope.description=data.article.description;
                $scope.contentTitle=data.article.title;
                $scope.contentHits=data.article.hits;
                $scope.contentShare=data.article.share;
                $scope.contentId=data.article.id;
                $scope.num = data.article.praise;
                $scope.comment = data.article.comment;
                openId = data.openIdFlag;
                $scope.relationList = data.relation;
                if($scope.relationList.length!=0){
                    $scope.articleNum="true";
                }
                $scope.Refresh();
            });


            listComment.get({"articleId":id,"pageNo":1,"pageSize":2},function(data){
                $scope.commentList = data.commentList;
                if($scope.commentList.length!=0){
                    $scope.comm = "true";
                }
            });
        }

        /**
         *点赞方法
         */
        $scope.zan = function(){
            if($scope.lock1=="0"){
                $scope.zanImg = 'http://xiaoerke-knowledge-pic.oss-cn-beijing.aliyuncs.com/knowledge%2Farticle_zan_yes.png';
                $scope.lock1= "1";
                $scope.num = $scope.num+1;
                ArticleZanRecord.save({"articleId":$scope.contentId,"PraiseNumber":$scope.num},function(data){
                })
            }
            else{
                $scope.zanImg = 'http://xiaoerke-knowledge-pic.oss-cn-beijing.aliyuncs.com/knowledge%2Farticle_zan_no.png';
                $scope.lock1= "0";
                $scope.num = $scope.num-1;
                ArticleZanRecord.save({"articleId":$scope.contentId,"PraiseNumber":$scope.num},function(data){
                })
            }
        }

        /**
         * 点击输入评论
         */
        $scope.editComment = function(){
            $scope.editLock="true";
        }

        /**
         * 点击宝大夫
         */
        $scope.attentionBdf = function(){
            judgeWX();
        }

        $scope.cancel = function(){
            $scope.attentionLock="false";
            $scope.editLock="false";
            $scope.lockzixun="false";
            $scope.lockyuyue="false";

        }

        /**
         * 判断是否是微信浏览器
         */
        var judgeWX = function(){
            var ua = window.navigator.userAgent.toLowerCase();
            if(ua.match(/MicroMessenger/i) == 'micromessenger'){
                alert("扫描下方二维码关注宝大夫！");
            }else{
                $scope.attentionLock="true";
            }
        }

        /**
         * 咨询大夫
         */
        $scope.zixun = function () {
            if(openId=="yes"){
                SendWechatMessageToUser.save({},function(data){

                });
                WeixinJSBridge.call('closeWindow');
            }else{
                $state.go('aboutConsult');
            }
        }

        /**
         * 预约大夫
         */
        $scope.yuyue = function(){
            if(openId=="yes"){
                $state.go('appointmentFirst');
            }else{
                $scope.lockzixun="false";
                $scope.lockyuyue="true";
                $scope.attentionLock="true";
            }
        }

        /**
         * 保存
         */
        $scope.save = function(){
            $scope.editLock = "false";
            if($scope.info.content!=undefined){
                $scope.pageLoading=true;
                saveComment.save({"articleId":$scope.contentId,"commentContent":$scope.info.content},function(data){
                    if(data.resultMsg==undefined){
                        $scope.info.content="";
                        window.location.reload();
                    }else{
                        alert("评论提交失败！");
                    }
                });
            }else{
                alert("评论内容不能为空！");
            }
        }

        /**
         * 相关文章
         */
        $scope.article = function(id){
            $scope.zanImg = 'http://xiaoerke-knowledge-pic.oss-cn-beijing.aliyuncs.com/knowledge%2Farticle_zan_no.png';//点赞初始图片
            $scope.num=0;//点赞初始值
            $scope.lock1= "0";//点赞初始锁值
            $scope.bdf="false";//宝大夫扫描二维码提示
            $scope.lockzixun="false";
            $scope.lockyuyue="false";
            $scope.description="";
            $scope.articleNum="false";
            $scope.attentionLock="false";
            $scope.editLock="false";
            $scope.comm = "false";
            $scope.attentionLock="false";
            $scope.Author = "false";
            $ionicScrollDelegate.scrollTop();
            $location.hash('top');
            $anchorScroll();
            $scope.init(id);
        }

        $scope.Refresh = function(){
            var shareUrl ='http://baodf.com/wisdom/wechatInfo/' +
                'fieldwork/wechat/author?url=http://baodf.com/wisdom/' +
                'wechatInfo/getZhengArticle?'+"id="+$scope.contentId;
            var timestamp;//时间戳
            var nonceStr;//随机字符串
            var signature;//得到的签名
            var appid;//得到的签名
            $.ajax({
                url:"wechatInfo/getConfig",// 跳转到 action
                async:true,
                type:'get',
                data:{url:location.href.split('#')[0]},//得到需要分享页面的url
                cache:false,
                dataType:'json',
                success:function(data) {
                    if(data!=null ){
                        timestamp=data.timestamp;//得到时间戳
                        nonceStr=data.nonceStr;//得到随机字符串
                        signature=data.signature;//得到签名
                        appid=data.appid;//appid
                        //微信配置
                        wx.config({
                            debug: false,
                            appId: appid,
                            timestamp:timestamp,
                            nonceStr: nonceStr,
                            signature: signature,
                            jsApiList: [
                                'onMenuShareTimeline',
                                'onMenuShareAppMessage'
                            ] // 功能列表
                        });
                        wx.ready(function () {
                            // 2.2 监听“分享到朋友圈”按钮点击、自定义分享内容及分享结果接口
                            wx.onMenuShareTimeline({
                                title: $scope.contentTitle+'-'+'宝大夫', // 分享标题
                                link: window.location.href.replace("true","false"), // 分享链接
                                imgUrl: 'http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Findex_baby_info.png', // 分享图标
                                success: function (res) {
                                    //记录用户分享文章
                                    ArticleShareRecord.save({"title":$scope.contentTitle,"contentId":$scope.contentId,"contentShare":$scope.contentShare},function(data){

                                    })
                                },
                                fail: function (res) {
                                }
                            });

                            wx.onMenuShareAppMessage({
                                title: $scope.contentTitle+'-'+'宝大夫', // 分享标题
                                desc: $scope.description, // 分享描述
                                link:window.location.href.replace("true","false"), // 分享链接
                                imgUrl: 'http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Findex_baby_info.png', // 分享图标
                                success: function (res) {
                                    //记录用户分享文章
                                    ArticleShareRecord.save({"title":$scope.contentTitle,"contentId":$scope.contentId,"contentShare":$scope.contentShare},function(data){

                                    })
                                },
                                fail: function (res) {
                                }
                            });

                        })
                    }else{
                    }
                },
                error : function() {
                }
            });

        }
    }]);