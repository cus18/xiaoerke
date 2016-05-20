angular.module('controllers', ['ionic']).controller('handfootmouthAddBabyCtrl', [
    '$scope','$state','$stateParams','saveBabyInfo','GetUserLoginStatus',
    '$location',
    function ($scope,$state,$stateParams,saveBabyInfo,GetUserLoginStatus,
              $location) {
        $scope.title = "添加宝宝";
        $scope.title0 = "添加宝宝";
        $scope.sex = 1;
        $scope.boyLock = true;
        $scope.girlLock = false;
        $scope.titleLock = true;// 隐藏顶部菜单
        $scope.baby = {};
        var Ip = "s251.baodf.com";


        $scope.$on('$ionicView.enter',function() {
            var routePath = "/insuranceBBBBBB" + $location.path();
            GetUserLoginStatus.save({routePath:routePath},function(data){
                if(data.status=="9") {
                    window.location.href = data.redirectURL;
                } else if(data.status=="8"){
                    window.location.href = ata.redirectURL+"?targeturl="+routePath;
                }else {
                    initDate();
                }
            });
        });

        /* 选择性别*/
        $scope.selectSex = function(sex){
            if(sex=="boy"){
                $scope.sex = 1;
            }
            else{
                $scope.sex = 0;
            }
        };
        /**
         * 添加宝宝
         */
        $scope.saveBabyInfo = function(){

            if($scope.baby.name==undefined||$scope.baby.name==""){
                alert("姓名不能为空");
                return;
            }
            if($("#birthday").val() == ""||$("#birthday").val()==undefined){
                alert("请选择宝宝生日");
                return;
            }
            saveBabyInfo.get({"sex":$scope.sex.toString(),"name":encodeURI($scope.baby.name),"birthDay":$("#birthday").val()}, function (data){
                if(data.resultCode=='1'){
                    window.location.href="http://"+Ip+"/keeper/wxPay/patientPay.do?serviceType=handfootmouth";
                }
            });

        }


        //初始化时间控件
        var initDate = function () {
            var date = new Date(+new Date()+8*3600*1000).toISOString().replace(/T/g,' ').replace(/\.[\d]{3}Z/,'');
            $("#birthday").mobiscroll().date();
            //初始化日期控件
            var opt = {
                preset: 'date', //日期，可选：date\datetime\time\tree_list\image_text\select
                theme: 'default', //皮肤样式，可选：default\android\android-ics light\android-ics\ios\jqm\sense-ui\wp light\wp
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
                // startYear:1980, //开始年份
                // endYear:currYear //结束年份
                minDate: new Date(1980,0,1),
                maxDate: new Date(date.substring(0,4), date.substring(5,7)-1, date.substring(8,10)),
                onSelect: function (valueText) {
                    var day = compareDate(valueText,moment().format("YYYY-MM-DD"));
                    if(day>365*14){
                        alert("目前还只服务于0-14岁的宝宝哦~");
                        $("#birthday").val("");
                    }
                },
                onCancel: function () {
                }
            };
            $("#birthday").mobiscroll(opt);
        }

        //计算两个日期的时间间隔
        var compareDate = function (start,end){
            if(start==null||start.length==0||end==null||end.length==0){
                return 0;
            }

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

    }
]);

