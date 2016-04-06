angular.module('controllers', ['ionic']).controller('myAccountCtrl', [
    '$scope','$state','$stateParams','CheckBind','GetDoctorInfo','SettlementInfo','GetWithDrawList',
    '$location','GetUserLoginStatus',
        function ($scope,$state,$stateParams,CheckBind,GetDoctorInfo,SettlementInfo,GetWithDrawList,
                  $location,GetUserLoginStatus) {
            $scope.acceptLock="true";
            $scope.codeLock="false";
            $scope.fineLock="false";
            $scope.choice = "everyDayList";

            $scope.withDrawls = function(){
                $state.go("withDrawls");
            }
            $scope.everyDayList = function(){
                $scope.choice = "everyDayList";
            }
            $scope.withDrawlsList = function(){
                $scope.choice = "withDrawlsList";
            }
            $scope.loadMoreWithDrawlsList = function(){
                if ($scope.scrollLoading) {
                    $scope.pageLoading = true;
                    GetWithDrawList.get({"pageNo":$scope.pageNo,"pageSize":$scope.pageSize},function(data){
                        $scope.pageLoading = false;
                        $scope.scrollLoading = $scope.pageNo < data.totalNum;
                        if($scope.scrollLoading==true)
                        {
                            $scope.pageNo = $scope.pageNo + 1;
                        }
                        $scope.withDrawList = $scope.withDrawList.concat(data.withDrawList || []);
                    })
                }
            }
            $scope.toggle1 = function(){
                if( $scope.acceptLock=="false"){
                    $scope.acceptLock="true";
                }
                else{
                    $scope.acceptLock="false";
                }
                if( $scope.fineLock=="false"){
                    $scope.finetLock="true";
                }
                else{
                    $scope.fineLock="false";
                }
            }
            $scope.toggle2 = function(){
                if( $scope.codeLock=="false"){
                    $scope.codeLock="true";
                }
                else{
                    $scope.codeLock="false";
                }
            }
            $scope.toggle3 = function(){
                if( $scope.fineLock=="false"){
                    $scope.fineLock="true";
                }
                else{
                    $scope.fineLock="false";
                }
            }

            $scope.$on('$ionicView.enter', function(){
                $scope.pageLoading = true;
                var routePath = "/doctorBBBBBB" + $location.path();
                GetUserLoginStatus.save({routePath:routePath},function(data){
                    $scope.pageLoading = false;
                    if(data.status=="9") {
                        window.location.href = data.redirectURL;
                    }else if(data.status=="8"){
                        window.location.href = data.redirectURL+"?targeturl="+routePath;
                    }
                })

                if((moment(moment().format("YYYY-MM-DD")+" "+"20:00:00",
                        "YYYY-MM-DD HH:mm:ss").fromNow()).indexOf("ago")!=-1)
                {
                    $scope.checkAvailable = "true";
                }
                else
                {
                    $scope.checkAvailable = "false";
                }
                $("#dateTime").mobiscroll().date();
                var currYear = (new Date()).getFullYear();
                $("#dateTime").mobiscroll().date(
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
                        startYear:1980, //开始年份
                        endYear:currYear, //结束年份
                        onSelect: function (valueText) {
                            if(moment(valueText).format("YYYY-MM-DD")==moment().format("YYYY-MM-DD"))
                            {
                                if((moment(moment(valueText).format("YYYY-MM-DD")+" "+"20:00:00",
                                        "YYYY-MM-DD HH:mm:ss").fromNow()).indexOf("ago")!=-1)
                                {
                                    $scope.checkAvailable = "true";
                                }
                                else
                                {
                                    $scope.checkAvailable = "false";
                                }
                            }
                            else
                            {
                                $scope.checkAvailable = "true";
                            }
                            $scope.pageLoading = true;
                            SettlementInfo.save({"doctorId":$scope.doctorId,"date":valueText,
                                "type":"appointment"},function(data){
                                $scope.pageLoading = false;
                                $scope.settlementInfoData = data;
                                $scope.totalPrice = data.appointmentTotal;
                            })
                    }
                });

                $scope.settlementDate = moment().format('YYYY-MM-DD');

                /**
                 * 判断医生是否绑定
                 */
                $scope.pageLoading = true;
                GetDoctorInfo.save({},function(data){
                    $scope.pageLoading = false;
                    if(data.doctorId!=""){
                        $scope.doctorId = data.doctorId;
                        $scope.balance = data.balance;
                        $scope.choice = "everyDayList";
                        SettlementInfo.save({"doctorId":$scope.doctorId,"date":$scope.settlementDate,"type":"appointment"},function(data){
                            $scope.settlementInfoData = data;
                            $scope.totalPrice = data.appointmentTotal;
                        })

                        $scope.pageNo = 1;
                        $scope.pageSize = 10;
                        $scope.withDrawList = "";
                        GetWithDrawList.get({"pageNo":$scope.pageNo,"pageSize":$scope.pageSize},function(data){
                            $scope.scrollLoading = $scope.pageNo < data.totalNum;
                            if($scope.scrollLoading==true)
                            {
                                $scope.pageNo = $scope.pageNo + 1;
                            }
                            $scope.withDrawList = $scope.withDrawList.concat(data.withDrawList || []);
                        })
                    }
                });
            });
    }])
