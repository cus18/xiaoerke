angular.module('controllers', ['ionic']).controller('consultAddBabyCtrl', [
    '$scope','$state','$stateParams','$ionicModal','GetUserLoginStatus','$location',
    'saveBabyInfo',
    function ($scope,$state,$stateParams,$ionicModal,GetUserLoginStatus,$location,
    		saveBabyInfo) {
        $scope.title0 = "添加宝宝信息";
        $scope.title = "添加宝宝信息";
        $scope.boyLock = true;
        $scope.girlLock = false;
        $scope.baby = {};

        /* 选择性别*/
        $scope.selectSex = function(sex){
            if(sex=="boy"){
                $scope.boyLock = true;
                $scope.girlLock = false;
            }
            else{
                $scope.boyLock = false;
                $scope.girlLock = true;
            }
        };
        /**
         * 添加宝宝
         */
        $scope.saveBabyInfo = function(){
            var name=$scope.baby.name;
            var birthday=$("#birthday").val();
            var sex=$scope.boyLock == true?1:0;
            if(typeof(name) == "undefined"){
                alert("姓名不能为空");
                return;
            }
            if(birthday == ""){
                alert("请选择宝宝生日");
                return;
            }
            saveBabyInfo.get({"sex":sex,"name":encodeURI(name),"birthDay":birthday}, function (data){
                if(data.resultCode=='1'){
                	$state.go("consultBabyList");
                }
            });
        }

        $scope.$on('$ionicView.enter',function() {
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

            };
            $("#birthday").mobiscroll(opt);
        });
    }])
