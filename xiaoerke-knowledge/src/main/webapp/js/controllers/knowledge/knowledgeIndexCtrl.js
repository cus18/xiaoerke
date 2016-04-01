angular.module('controllers', ['ionic']).controller('knowledgeIndexCtrl', [
        '$scope','$state','$stateParams','$filter','dailyRemind','standardFigure','GetCategoryList','GetArticleList',
    'gettodaySelectAndReadArticleList','getBabyEmrList','updateBabyPic','updateBabyBirthday','ArticleShareRecord','$ionicScrollDelegate',
        function ($scope,$state,$stateParams,$filter,dailyRemind,standardFigure,GetCategoryList,GetArticleList,
                  gettodaySelectAndReadArticleList,getBabyEmrList,updateBabyPic,updateBabyBirthday,ArticleShareRecord,$ionicScrollDelegate) {
            $scope.info = {};
            $scope.lock='false';
            $scope.showSearchAllLock="false";
            $scope.showSearchLock="true";
            $scope.photoLock="true";
            $scope.myScrollLock=false;
            $scope.jing = "true";
            $scope.jing2 = "false";
            var num = 10;
            $scope.todayChoiceMoreItem='Newborn';
            $scope.babyClassifyImg = [
                "http://xiaoerke-knowledge-pic.oss-cn-beijing.aliyuncs.com/knowledge%2Fclassify_icon1.png" ,
                "http://xiaoerke-knowledge-pic.oss-cn-beijing.aliyuncs.com/knowledge%2Fclassify_icon2.png",
                "http://xiaoerke-knowledge-pic.oss-cn-beijing.aliyuncs.com/knowledge%2Fclassify_icon3.png",
                "http://xiaoerke-knowledge-pic.oss-cn-beijing.aliyuncs.com/knowledge%2Fclassify_icon4.png",
                "http://xiaoerke-knowledge-pic.oss-cn-beijing.aliyuncs.com/knowledge%2Fclassify_icon5.png" ,
                "http://xiaoerke-knowledge-pic.oss-cn-beijing.aliyuncs.com/knowledge%2Fclassify_icon6.png" ,
                "http://xiaoerke-knowledge-pic.oss-cn-beijing.aliyuncs.com/knowledge%2Fclassify_icon7.png" ,
                "http://xiaoerke-knowledge-pic.oss-cn-beijing.aliyuncs.com/knowledge%2Fclassify_icon8.png"
            ];
            var bir = "";
            var img = document.getElementById('babyPhoto');

            /**
             * 判断是否录入宝宝信息
             */
            $scope.pageLoading=true;
            getBabyEmrList.get({},function(data){
                $scope.pageLoading=false;
                if(data.name!=undefined){
                    $scope.babyname = data.name;
                    bir = data.birthday;
                    if(data.status=="0"){
                        img.src ="http://xiaoerke-knowledge-pic.oss-cn-beijing.aliyuncs.com/knowledge%2Fbaby_photo_default.png";
                    }else{
                        img.src ="http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/"+data.id;
                    }
                }else{
                    window.location.href ="ap/knowledge?value=251333#/knowledgeLogin";
                }
            });

            $scope.$on('$ionicView.enter', function(){
                img = document.getElementById('babyPhoto');

                $scope.syndromeList = [];
                getBirthDayTime();
                getBirthDayContent();
                getCategoryContent();
            });

            var getBirthDayTime = function(){
                $("#birthday").mobiscroll().date();
                var currYear = (new Date()).getFullYear();
                var opt=(
                {
                    preset: 'date', //日期，可选：date\datetime\time\tree_list\image_text\select
                    theme: 'android-ics light', //皮肤样式，可选：default\android\android-ics light\android-ics\ios\jqm\sense-ui\wp light\wp
                    display: 'modal', //显示方式 ，可选：modal\inline\bubble\top\bottom
                    mode: 'scroller', //日期选择模式，可选：scroller\clickpick\mixed
                    lang:'zh',
                    dateFormat: 'yyyy-mm-dd', // 日期格式
                    setText: '确定', //确认按钮名称
                    cancelText: '取消',//取消按钮名籍我
                    dateOrder: 'yyyymmdd', //面板中日期排列格式
                    dayText: '日', monthText: '月', yearText: '年', //面板中年月日文字
                    showNow: false,
                    nowText: "今",
                    startYear:2006, //开始年份
                    endYear:currYear, //结束年份
                    onSelect: function (valueText, inst) {
                        updateBabyBirthday.save({"birthday":valueText},function(data){
                            bir = valueText;
                            getBirthDayContent();
                        }) ;
                    }
                }
                );
                $("#birthday").mobiscroll(opt);
            }

            /**
             * 根据生日不同获取相应内容
             */
            var getBirthDayContent = function(){

                //身高体重
                standardFigure.get({},function(data){
                    if($filter('limitTo')(data.age, 1)<3){
                        $scope.jing = "true";
                        $scope.jing2 = "false";
                        var standardFigure = data.standardFigure;
                        $scope.standard = standardFigure.split(";");
                        $scope.info.bir =data.age;

                        //郑玉巧每日提醒
                        dailyRemind.get({"birthDay":bir},function(data){
                            $scope.dailyList = data.dailyRemind_tip.split("dailyRemind");
                        });

                        //获取今日精选和今日必读文章
                        $scope.pageLoading=true;
                        gettodaySelectAndReadArticleList.save({},function(data){
                            $scope.pageLoading=false;
                            console.log(data);
                            $scope.todayReadArticleList = data.todayReadArticleList;//每日必读
                            $scope.todaySelectArticleList = data.todaySelectArticleList;//每日精选
                        });

                    }else{
                            $scope.jing = "false";
                            $scope.jing2 = "true";
                            $scope.info.bir =data.age;
                            $scope.pageLoading=true;
                            GetArticleList.save({"id":$scope.jingId},function(data){
                                $scope.pageLoading=false;
                                $scope.jingList = data.articleList;
                            });

                    }
                });
            }


            /**
             * 获取固定的栏目列表
             */
            var getCategoryContent = function(){
                //查询栏目列表接口
                $scope.pageLoading=true;
                GetCategoryList.save({},function(data){
                    $scope.pageLoading=false;
                    $scope.babyClassify = data.categoryList[4].secondMenuData;//获取育儿宝典内容
                    $scope.syndromeClassify = data.categoryList[7].secondMenuData;//常见疾病
                    $scope.jingId = data.categoryList[5].firstMenuId;
                    for (var i = 2; i < $scope.syndromeClassify.length; i++) {
                        $scope.syndromeList.push($scope.syndromeClassify[i]);//其它疾病
                    }
                });
            }

            /**
             * 搜索文章
             */
            $scope.searchTitle = function(){
                if($scope.info.search!=undefined){
                    $state.go('knowledgeSearch',{content:$scope.info.search,generalize:"WX"});
                }else{
                    alert("请输入搜索内容！");
                }
            }

            /**
             * 修改宝宝图像
             */
            $scope.chooseImage = function(){
                $scope.photoLock="false";
                wx.chooseImage({
                    count: 1, // 默认9
                    sizeType: ['original','compressed'], // 可以指定是原图还是压缩图，默认二者都有
                    sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
                    success: function (res) {
                        img.src = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
                        var imgId = res.localIds[0];
                        //上传图片接口
                        wx.uploadImage({
                            localId: imgId, // 需要上传的图片的本地ID，由chooseImage接口获得
                            isShowProgressTips: 1, // 默认为1，显示进度提示
                            success: function (res) {
                                updateBabyPic.get({"mediaId":res.serverId},function(data){
                                 });
                            }
                         });
                    }
                });
            }

            /**
             * 今日精选跳转
             */
            $scope.lookMore = function(id,name){
                if(name=="新生儿"){
                    $state.go('todayChoiceNewborn',{id:id,title:name});
                }
                else if(name=="婴幼儿"){
                    $state.go('todayChoiceNursling',{id:id,title:name});
                }
                else {
                    $state.go('otherDisease',{id:id,title:name});
                }
            }
            /**
             * 显示搜索框
             */
            $scope.showSearch=function(){
                $ionicScrollDelegate.scrollTop();
                $scope.showSearchLock="false";
                $scope.showSearchAllLock="true";
            }
            $scope.onSwipeUp=function(){

                $scope.showSearchAllLock="false";
                $scope.showSearchLock="true";
            }
            $scope.onSwipeDown=function(){

                $scope.showSearchAllLock="false";
                $scope.showSearchLock="true";
                var img = document.getElementById('babyPhoto');
            }
            $scope.onTouch=function(){
                $scope.showSearchAllLock="false";
                $scope.showSearchLock="true";
            }

            /**
             * 超过3岁以上加载更多精选
             */
            $scope.jingMore = function(){
                num=num+10;
                $scope.pageLoading=true;
                GetArticleList.save({"id":$scope.jingId,"pageNo":1,"pageSize":num},function(data){
                    $scope.pageLoading=false;
                    if($scope.jingList.length == data.articleList.length){
                        $scope.jing2 = "false";
                        alert("文章已全部加载完成！");
                    }else{
                        $scope.jingList = data.articleList;
                    }
                });
            }

            /**
             * 更多郑老师提醒
             */
            $scope.getWarn = function(index){
                $state.go('knowledgeWarn',{birthday:bir,index:index});
            }

            /**
             * 今日必读临时显示
             */
            $scope.linshi = function(name,id){
                if(name=="精选"){
                    $state.go('todayChoiceNurslingList',{zhutitle:name,houtitle:"文章",id:id});
                }else{
                    $state.go('todayReadMore',{secondMenuId:id,title:name});
                }
            }

            $scope.doRefresh = function(){
                var shareUrl ='http://baodf.com/xiaoerke-knowledge/ap/wechatInfo/fieldwork/wechat/' +
                    'author?url=http://baodf.com/xiaoerke-knowledge/ap/wechatInfo/getZhengIndex';
                var timestamp;//时间戳
                var nonceStr;//随机字符串
                var signature;//得到的签名
                var appid;//得到的签名
                $.ajax({
                    url:"ap/wechatInfo/getConfig",// 跳转到 action
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
                                    'onMenuShareAppMessage',
                                    'onMenuShareWeibo',
                                    'chooseImage',
                                    'uploadImage',
                                    'downloadImage'
                                ] // 功能列表
                            });
                            wx.ready(function () {
                                //分享到朋友圈
                                wx.onMenuShareTimeline({
                                    title: '【郑玉巧育儿经】-宝大夫', // 分享标题
                                    link: shareUrl, // 分享链接
                                    imgUrl: 'http://xiaoerke-common-pic.oss-cn-beijing.aliyuncs.com/common%2Fbaodf_logo.jpg', // 分享图标
                                    success: function () {
                                        //记录用户分享文章
                                        ArticleShareRecord.save({"contentId":"郑玉巧育儿经首页"},function(data){
                                        });
                                    },
                                    cancel: function () {
                                        // 用户取消分享后执行的回调函数
                                    }
                                });
                                //分享给朋友
                                wx.onMenuShareAppMessage({
                                    title: '【郑玉巧育儿经】-宝大夫', // 分享标题
                                    desc: '智能匹配月龄，获取针对性一对一育儿指导，建立宝宝专属健康档案，一路呵护，茁壮成长！', // 分享描述
                                    link: shareUrl, // 分享链接
                                    imgUrl: 'http://xiaoerke-common-pic.oss-cn-beijing.aliyuncs.com/common%2Fbaodf_logo.jpg', // 分享图标
                                    success: function () {
                                        //记录用户分享文章
                                        ArticleShareRecord.save({"contentId":"郑玉巧育儿经首页"},function(data){

                                        });
                                    },
                                    cancel: function () {
                                        // 用户取消分享后执行的回调函数
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