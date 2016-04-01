angular.module('controllers', ['ionic']).controller('sheOnlineDetailCtrl', [
        '$scope','$state','$stateParams','GetArticleDetail','ArticleZanRecord','ArticleShareRecord','$sce',
        function ($scope,$state,$stateParams,GetArticleDetail,ArticleZanRecord,ArticleShareRecord,$sce) {
            $scope.info={};

            /**
             *点赞方法
             */
            $scope.zan = function(){
                if($scope.lock1=="0"){
                    $scope.zanImg = 'http://xiaoerke-knowledge-pic.oss-cn-beijing.aliyuncs.com/sheOnline%2Fonline_zan_yes.png';
                    $scope.lock1= "1";
                    ArticleZanRecord.save({"contentId":$scope.titleId,"action":"confirm"},function(data){
                        $scope.num = data[0];
                    })
                }
                else{
                    $scope.zanImg = 'http://xiaoerke-knowledge-pic.oss-cn-beijing.aliyuncs.com/sheOnline%2Fonline_zan_no.png';
                    $scope.lock1= "0";
                    ArticleZanRecord.save({"contentId":$scope.titleId,"action":"cancel"},function(data){
                        $scope.num = data[0];
                    })
                }
            }

            /**
             * 点击相关文章方法
             */
            $scope.article = function(id,title){
                GetArticleDetail.save({"id":id},function(data){
                    $state.go('articleDetail',{contentId:id,contentTitle:title,
                        contentHits:data.articleReadCount,contentShare:data.articleShareCount});
                });
            }

            $scope.doRefresh = function(){
                var webname="http://www.baodf.com";
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
                                    title: $scope.contentTitle+'-'+'【郑玉巧在线】', // 分享标题
                                    link: window.location.href.replace("true","false"), // 分享链接
                                    imgUrl: webname+'http://xiaoerke-common-pic.oss-cn-beijing.aliyuncs.com/common%2Fbaodf_logo.jpg', // 分享图标
                                    success: function (res) {

                                        //记录用户分享文章
                                        ArticleShareRecord.save({"contentId":"郑玉巧在线:"+$stateParams.contentId},function(data){

                                        })
                                    },
                                    fail: function (res) {
                                    }
                                });

                                wx.onMenuShareAppMessage({
                                    title: $scope.contentTitle+'-'+'【郑玉巧在线】', // 分享标题
                                    desc: $scope.description, // 分享描述
                                    link: window.location.href.replace("true","false"), // 分享链接
                                    imgUrl: webname+'http://xiaoerke-common-pic.oss-cn-beijing.aliyuncs.com/common%2Fbaodf_logo.jpg', // 分享图标
                                    success: function (res) {
                                        //记录用户分享文章
                                        ArticleShareRecord.save({"contentId":"郑玉巧在线:"+$stateParams.contentId},function(data){

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

            $scope.$on('$ionicView.enter', function() {
                $scope.titleId = $stateParams.contentId;
                $scope.lock1 ="0";
                $scope.zanImg = 'http://xiaoerke-knowledge-pic.oss-cn-beijing.aliyuncs.com/sheOnline%2Fonline_zan_no.png';
                $scope.articleNum="false";

                $scope.pageLoading=true;
                ArticleZanRecord.save({"contentId":$scope.titleId,"action":"get"},function(data){
                    $scope.num = data[0];
                })

                GetArticleDetail.save({"id":$scope.titleId},function(data){
                    $scope.pageLoading=false;
                    $scope.info.articleContent = $sce.trustAsHtml(angular.copy(data.articleDetail.content));
                    $scope.description = data.article.description;
                    $scope.time = data.article.author;
                    $scope.title=data.article.title;
                    $scope.hits=data.article.hits;
                    $scope.share=data.article.share;
                    $scope.relationList = data.relation;
                    if($scope.relationList.length!=0){
                        $scope.articleNum="true";
                    }
                    $scope.doRefresh();
                });
            });
        }])