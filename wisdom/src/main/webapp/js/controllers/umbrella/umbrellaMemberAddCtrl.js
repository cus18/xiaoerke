angular.module('controllers', ['ionic']).controller('umbrellaMemberAddCtrl', [
        '$scope','$state','$stateParams','addFamily',
        function ($scope,$state,$stateParams,addFamily) {
            $scope.title="宝护伞";
            $scope.sexItem = "boy";
            $scope.parentLock = false;//判断之前登录的时候选择的是宝爸还是宝妈
            $scope.info = {}
            /*选择性别*/
            $scope.selectSex = function(sex){
                $scope.sexItem=sex;
                console.log("家庭成员"+sex);
            };
            /*生日插件*/
            $scope.selectBirthday=function(){
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
                    //startYear:1980, //开始年份
                    //endYear:currYear //结束年份
                    minDate: new Date(2002,date.substring(5,7)-1,date.substring(8,10)),
                    maxDate: new Date(date.substring(0,4), date.substring(5,7)-1, date.substring(8,10))
                    //endYear:2099 //结束年份
                };
                $("#birthday").mobiscroll(opt);
            };

            $scope.save=function(){
                var birthday=$("#birthday").val();
                var sex="0";
                var name = "ts";
                addFamily.save({"sex":sex,"name":encodeURI(name),"birthDay":birthday,"id":"120000023"},function(data){
                    console.log(data)

                });
            };

            $scope.$on('$ionicView.enter', function(){
                $scope.selectBirthday();
            });

            
    }]);