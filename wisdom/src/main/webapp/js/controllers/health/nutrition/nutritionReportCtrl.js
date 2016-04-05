angular.module('controllers', ['ionic']).controller('nutritionReportCtrl', [
    '$scope','$state','$stateParams','GetBabyInfo','UpdateBabyInfo','UpdateSendWechatMessage','GetUserLoginStatus','$location',
    function ($scope,$state,$stateParams,GetBabyInfo,UpdateBabyInfo,UpdateSendWechatMessage,GetUserLoginStatus,$location) {

        $scope.footerNum=2;
        $scope.homeImg="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Ficon_footer1.png";
        $scope.assessImg="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Ficon_footer2.png";
        $scope.necessaryImg="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Ficon_footer3.png";
        $scope.reportImg="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Ficon_footer4_select.png";

        $scope.sexBoy="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Fboy_select.png";
        $scope.sexGirl="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Fgirl.png";
        $scope.sexLock=false;
        $scope.heightLock=false;
        $scope.weightLock=false;
        $scope.info={ };//修改的身高值 修改的体重值
        $scope.updateSexValue = "男";//修改的体重值
        $scope.info.sex = "boy";
        var KuapExplain =["宝宝身材肥胖哦，平时是不是特别爱吃奶、肉肉和零食呢？要注意饮食均衡哦，否则长大后患上慢性疾病的风险会大大增加哦，快来学习科学营养喂养，调整宝宝的饮食结构，助宝宝健康成长吧!",
            "宝宝身材棒棒哒，要继续保持良好的饮食习惯哦，宝宝从小养成均衡合理的饮食习惯会让宝宝受益终身哦，一起来学习如何更科学的营养喂养宝宝吧，让宝宝长更高、更健康！",
            "宝宝身材消瘦哦，平时是不是吃的太少了或者宝宝挑食偏食导致营养不均衡呢？快来测一测宝宝平时容易缺乏哪些营养素，会有哪些危害呢？一起来学习如何科学喂养宝宝吧！"];



        //修改性别
        $scope.updateSex =function(){
            $scope.sexLock=true;
        };
        //修改身高
        $scope.updateHeight =function(){
            $scope.heightLock=true;
        };
        //修改体重
        $scope.updateWeight =function(){
            $scope.weightLock=true;
        };
        // 取消 更新操作
        $scope.cancelUpdate =function(){
            $scope.weightLock=false;
            $scope.heightLock=false;
            $scope.sexLock=false;
        };

        //完成修改身高
        $scope.finishUpdateHeight =function(){
            if($scope.info.updateHeightValue==undefined||$scope.info.updateHeightValue==""){
                alert("信息不能为空！");
            }else{
                $scope.heightLock=false;
                updateInfoHeight();
            }
        };
        //完成修改体重
        $scope.finishUpdateWeight =function(){
            if($scope.info.updateWeightValue==undefined||$scope.info.updateWeightValue==""){
                alert("信息不能为空！");
            }else{
                $scope.weightLock=false;
                updateInfoWeight();
            }
        };
        // 完成修改性别
        $scope.finishUpdateSex =function(){
            $scope.sexLock=false;
            updateInfoSex();
        };

        /**
         * 选择性别
         * @param sex
         */
        $scope.selectSex =function(sex){
            if(sex=="girl"){
                $scope.sexGirl="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Fgirl_select.png";
                $scope.sexBoy="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Fboy.png";
                $scope.info.sex = "girl";
                $scope.updateSexValue= "女";
            }
            else{
                $scope.sexGirl="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Fgirl.png";
                $scope.sexBoy="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Fboy_select.png";
                $scope.updateSexValue = "男";
                $scope.info.sex = "boy";
            }
        };

        var bir = "";
        var planId = "";

        $scope.$on('$ionicView.enter', function(){
            var routePath = "/ntrBBBBBB" + $location.path();
            GetUserLoginStatus.save({routePath:routePath},function(data){
                $scope.pageLoading = false;
                if(data.status=="9") {
                    window.location.href = data.redirectURL;
                }else if(data.status=="8"){
                    window.location.href = data.redirectURL+"?targeturl="+routePath;
                }else{
                    $scope.commentImg = "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Fdianjiqian_png_03.png";
                    var babyInfo = "";
                    $stateParams.type=="first"?$scope.sethide="true":$scope.sethide="false";
                    GetBabyInfo.save({}, function (data){
                        if(data.babyInfo==undefined){
                            window.location.href = "ntr?value=251336#/nutritionBabyInfo";
                        }else{
                            planId = data.id;
                            babyInfo = data.babyInfo.split(";");
                            if(babyInfo[0]=="boy"){
                                $scope.sex = "男";
                            }else{
                                $scope.sex = "女";
                            }
                            $scope.birthday = dateDiff(babyInfo[1]);
                            $scope.height = babyInfo[2];
                            $scope.weight = babyInfo[3];
                            getKuap();//调用计算kuap值方法
                            if(data.switch!=""){
                                if(data.switch=="yes"){
                                    $scope.setMe = true;
                                }else{
                                    $scope.setMe = false;
                                }
                            }else{
                                $scope.setMe = true;
                            }
                        }
                    });
                }
            }
            );
            var date = new Date(+new Date()+8*3600*1000).toISOString().replace(/T/g,' ').replace(/\.[\d]{3}Z/,'');
            var myYear=date.substring(0,4);
            var myMonth=date.substring(5,7);
            var myDate=date.substring(8,10);
            var myYear2 = "";
            var myMonth2 = "";
            /*if(myMonth>6){
                myYear2=parseInt(myYear)-1;
                //myMonth2=parseInt(myMonth);
            }
            else{
                myYear2=parseInt(myYear)-2;
                //myMonth2=parseInt(myMonth)+6;
            }*/

            $("#birthday").mobiscroll().date();
            //初始化日期控件
            var opt = {
                preset: 'date', //日期，可选：date\datetime\time\tree_list\image_text\select
                theme: 'default', //皮肤样式，可选：default\android\android-ics light\android-ics\ios\jqm\sense-ui\wp light\wp
                display: 'bottom', //显示方式 ，可选：modal\inline\bubble\top\bottom
                mode: 'scroller', //日期选择模式，可选：scroller\clickpick\mixed
                lang:'zh',
                dateFormat: 'yyyy-mm-dd', // 日期格式
                setText: '确定', //确认按钮名称
                cancelText: '取消',//取消按钮名籍我
                dateOrder: 'yyyymmdd', //面板中日期排列格式
                dayText: '日', monthText: '月', yearText: '年', //面板中年月日文字
                showNow: false,
                nowText: "今",
                // startYear:1980, //开始年份
                // endYear:currYear //结束年份
                minDate: new Date(myYear-3,myMonth-1,myDate),
                maxDate: new Date(myYear-1, myMonth-1, myDate),
                onSelect: function (valueText) {
                    console.log("value",valueText);
                    if(dateDiff(valueText)=="no"){
                        $scope.birthday = "";
                        alert("宝宝年龄只能在1岁至3岁之间，请重新选择，谢谢！");

                    }else{
                        bir = valueText;
                        updateInfoBir(valueText);
                    }
                }
            };
            $("#birthday").mobiscroll(opt);

        });

        /**
         * 关闭消息提醒
         */
        $scope.setMessage = function () {
            var flag = "";
            $scope.setMe==true?$scope.setMe=false:$scope.setMe=true;
            $scope.setMe==true?flag="yes":flag="no";
            UpdateSendWechatMessage.get({"flag":flag}, function (data) {

            })

        }

        $scope.goIndex =function(){
            $state.go("nutritionIndex");
        };

        /* 底部菜单选择*/
        $scope.menuSelect =function(index){
            $scope.index=index;
            if(index==0){
                $state.go("nutritionIndex");
            }
            else if(index==1){
                $state.go("nutritionAssess");
            }
            else if(index==2){
                $state.go("nutritionReport",{type:"second"});
            }
            else if(index==3){
                $state.go("nutritionNecessary");
            }else if(index==4){
                $scope.commentImg = "http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Fdianjihou_png_03.png";
                $state.go("nutritionAsk");
            }
        };

        function updateInfoHeight(){
            $scope.pageLoading = true;
            UpdateBabyInfo.save({"id":planId,"height":String($scope.info.updateHeightValue)}, function (data) {
                $scope.pageLoading = false;
                if(data.resultMsg=="OK"){
                    $scope.height=$scope.info.updateHeightValue;
                    getKuap();
                }else{
                    alert("修改失败！");
                }
            });
        }

        function updateInfoWeight(){
            $scope.pageLoading = true;
            UpdateBabyInfo.save({"id":planId,"weight":String($scope.info.updateWeightValue)}, function (data) {
                $scope.pageLoading = false;
                if(data.resultMsg=="OK"){
                    $scope.weight=$scope.info.updateWeightValue;
                    getKuap();
                }else{
                    alert("修改失败！");
                }
            });
        }

        function updateInfoSex(){
            $scope.pageLoading = true;
            UpdateBabyInfo.save({"id":planId,"gender":$scope.info.sex}, function (data) {
                $scope.pageLoading = false;
                if(data.resultMsg=="OK"){
                    $scope.sex=$scope.updateSexValue;
                }else{
                    alert("修改失败！");
                }
            });
        }

        function updateInfoBir(item){
            $scope.pageLoading = true;
            UpdateBabyInfo.save({"id":planId,"birthday":item}, function (data) {
                $scope.pageLoading = false;
                if(data.resultMsg=="OK"){
                    $scope.birthday = dateDiff(item);
                }else{
                    alert("修改失败！");
                }
            });
        }

        function getKuap(){
            $scope.kuap = parseFloat($scope.weight/Math.pow($scope.height,2)*Math.pow(10,4)).toFixed(1);
            var kuapRotate = (21-$scope.kuap)*10;
            $(".kaup-wheel .kuap-circle").css("transform","rotate("+kuapRotate+"deg)");
            console.log("kuap",$scope.kuap);
            if($scope.sethide=="true"){
                if($scope.kuap>22){//肥胖
                    $scope.getKuapExplain = KuapExplain[0];
                }
                if($scope.kuap>=15&&$scope.kuap<=22){//正好
                    $scope.getKuapExplain = KuapExplain[1];
                }
                if($scope.kuap<15){//消瘦
                    $scope.getKuapExplain = KuapExplain[2];
                }
            }
        }

        function dateDiff(bir){
            var date = new Date(+new Date()+8*3600*1000).toISOString().replace(/T/g,' ').replace(/\.[\d]{3}Z/,'');
            var birthday=bir;
            var now=date.substring(0,10);
            var day=compareDate(birthday,now);
            console.log("days",day);
            var year=parseInt(day/365);
            var month=parseInt((day-365*year)/30);
            var week=parseInt((day-365*year)%30/7);

            if(day<365||day>1095){

                return "no";
            }
            var age = "";
            if(year!=0){
                age+=year+"岁";
            }
            if(month!=0){
                age+=month+"月";
            }
            if(week!=0){
                age+=week+"周";
            }

            return age;

        }

        function compareDate(start,end){

            var arr=start.split("-");
            var starttime=new Date(arr[0],parseInt(arr[1]-1),arr[2]);
            var starttimes=starttime.getTime();

            var arrs=end.split("-");
            var endtime=new Date(arrs[0],parseInt(arrs[1]-1),arrs[2]);
            var endtimes=endtime.getTime();

            var intervalTime = endtimes-starttimes;//两个日期相差的毫秒数 一天86400000毫秒
            var Inter_Days = ((intervalTime).toFixed(2)/86400000)+1;//加1，是让同一天的两个日期返回一天

            return Inter_Days;
        }


    }]);

