angular.module('controllers', ['ionic']).controller('nutritionBabyInfoCtrl', [
    '$scope','$state','$stateParams','$location','GetUserLoginStatus','SaveBabyInfonutrition','$http',
    function ($scope,$state,$stateParams,$location,GetUserLoginStatus,SaveBabyInfonutrition,$http) {

        $scope.title="基本信息";
        $scope.sexGirl="基本信息";
        $scope.info = {};
        $scope.sexBoy="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Fboy_select.png";
        $scope.sexGirl="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Fgirl.png";
        $scope.info.sex = "boy";
        var bir = "";

        $scope.$on('$ionicView.enter', function(){
            $scope.pageLoading = true;
            var routePath = "/ntrBBBBBB" + $location.path();
            GetUserLoginStatus.save({routePath:routePath},function(data){
                $scope.pageLoading = false;
                if(data.status=="9") {
                    window.location.href = data.redirectURL;
                }else if(data.status=="8"){
                    window.location.href = data.redirectURL+"?targeturl="+routePath;
                }
            })

            var date = new Date(+new Date()+8*3600*1000).toISOString().replace(/T/g,' ').replace(/\.[\d]{3}Z/,'');
            var myYear=date.substring(0,4);
            var myMonth=date.substring(5,7);
            var myDate=date.substring(8,10);
            /*var myYear2 = "";
            var myMonth2 = "";
           if(myMonth>6){
               myYear2=parseInt(myYear)-1;
               myMonth2=parseInt(myMonth);
           }
            else{
               myYear2=parseInt(myYear)-2;
               myMonth2=parseInt(myMonth)+6;
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
               /* maxDate: new Date(date.substring(0,4), date.substring(5,7)-1, date.substring(8,10)),*/
                onSelect: function (valueText) {
                    console.log("value",valueText);
                    if(dateDiff(valueText)=="no"){
                        $scope.info.birthday = "";
                        alert("宝宝年龄只能在1岁至3岁之间，请重新选择，谢谢！");
                    }else{
                        bir = valueText;
                        $scope.info.birthday = dateDiff(valueText);
                    }
                }
            };
            $("#birthday").mobiscroll(opt);
        });

        /**
         * 选择性别
         * @param sex
         */
        $scope.selectSex =function(sex){
            if(sex=="girl"){
                $scope.sexGirl="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Fgirl_select.png";
                $scope.sexBoy="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Fboy.png";
                $scope.info.sex = "girl";
            }
            else{
                $scope.sexGirl="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Fgirl.png";
                $scope.sexBoy="http://xiaoerke-healthplan-pic.oss-cn-beijing.aliyuncs.com/nutrition%2Fboy_select.png";
                $scope.info.sex = "boy";
            }
        };

        /**
         *保存宝宝信息
         */
        $scope.nutritionReport =function(){
            if($scope.info.sex!=""&&bir!=""&&$scope.info.height!=undefined&&$scope.info.height!=""
                &&$scope.info.weight!=undefined&&$scope.info.weight!=""){
                SaveBabyInfonutrition.save({"gender":$scope.info.sex,"birthday":bir,
                    "height":$scope.info.height,"weight":$scope.info.weight}, function (data) {
                        if(data.resultMsg=="OK"){
                            //$state.go("nutritionReport",{type:"first"});
                            var pData = {logContent:encodeURI("YYGL_TXXX")};
                            $http({method:'post',url:'util/recordLogs',params:pData});
                            window.location.href = "ntr?value=251334#/nutritionReport/first";
                        }
                });
            }else{
                alert("宝宝信息不能为空哦！");
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

