angular.module('controllers', ['ionic']).controller('insuranceAddBabyCtrl', [
    '$scope','$state','$stateParams','saveBabyInfo','GetUserLoginStatus',
    '$location',
    function ($scope,$state,$stateParams,saveBabyInfo,GetUserLoginStatus,
              $location) {
        $scope.sex = 1;
        $scope.boyLock = true;
        $scope.baby = {};
        var Ip = "s251.baodf.com";
        var type;


        $scope.$on('$ionicView.enter',function() {
            type=$stateParams.type;
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
            $scope.sex = sex;
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
                    window.location.href="http://"+Ip+"/keeper/wxPay/patientPay.do?serviceType="+type;
                    //window.location.href="/keeper/wxPay/patientPay.do?serviceType="+type;
                }
            });

        }


        //初始化时间控件
        var initDate = function () {
            var date = new Date(+new Date()+8*3600*1000).toISOString().replace(/T/g,' ').replace(/\.[\d]{3}Z/,'');
            console.log(date);
            var myYear=date.substring(0,4);
            var myMonth=date.substring(5,7);
            var myDay=date.substring(8,10);
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
                minDate: new Date(myYear-14,myMonth-1,myDay),
                maxDate: new Date(date.substring(0,4), date.substring(5,7)-1, date.substring(8,10)),
                onSelect: function (valueText) {

                },
                onCancel: function () {
                }
            };
            $("#birthday").mobiscroll(opt);
        }

    }
]);

