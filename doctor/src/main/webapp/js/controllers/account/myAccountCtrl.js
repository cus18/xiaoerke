angular.module('controllers', ['ionic']).controller('myAccountCtrl', [
    '$scope','$state','$stateParams','CheckBind','GetDoctorInfo','SettlementInfo','GetWithDrawList',
    '$location','GetUserLoginStatus','GetDayList','$http',
        function ($scope,$state,$stateParams,CheckBind,GetDoctorInfo,SettlementInfo,GetWithDrawList,
                  $location,GetUserLoginStatus,GetDayList,$http) {
            $scope.acceptLock=false;
            $scope.codeLock="false";
            $scope.fineLock="false";
            $scope.phoneConsultLock = false;
            $scope.choice = "everyDayList";
            $scope.updateAccount = true;//8:00提醒
            var saveCurrDate;



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
                });
                setLog("WXZJB_MRQD");
                /*if((moment(moment().format("YYYY-MM-DD")+" "+"20:00:00",
                        "YYYY-MM-DD HH:mm:ss").fromNow()).indexOf("ago")!=-1)
                {
                    $scope.checkAvailable = "true";
                }
                else
                {
                    $scope.checkAvailable = "false";
                }*/
                var myYear=moment().format("YYYY");
                var myMonth=moment().format("MM");
                var myDate=moment().format("DD");
                $("#dateTime").mobiscroll().date();
               // var currYear = (new Date()).getFullYear();
                $("#dateTime").mobiscroll().date(
                    {
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
                        /*startYear:1980, //开始年份
                        endYear:currYear, //结束年份*/
                        minDate: new Date(2015,0,1),
                        maxDate: new Date(myYear,myMonth-1,myDate),
                        onSelect: function (valueText) {
                            /*if(moment(valueText).format("YYYY-MM-DD")==moment().format("YYYY-MM-DD"))
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
                            }*/
                            /*$scope.pageLoading = true;
                            SettlementInfo.save({"doctorId":$scope.doctorId,"date":valueText,
                                "type":"appointment"},function(data){
                                $scope.pageLoading = false;
                                $scope.settlementInfoData = data;
                                $scope.totalPrice = data.appointmentTotal;
                            });*/
                            setLog("WXZJB_MRQD_QHRQ");
                            getphoneConsultDay(valueText);
                            saveCurrDate = valueText;
                            $scope.currWeek = getChWeek(getWeekNum(valueText));

                            if(Date.parse(valueText)<Date.parse(getCurrDate())){
                                $("#changeNext").removeClass("c2");
                            }else{
                                $("#changeNext").addClass("c2");
                            }
                    }
                });
                saveCurrDate = getCurrDate();
                $("#dateTime").val(getCurrDate());
                $scope.currWeek = getChWeek(getWeekNum(getCurrDate()));
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
                        //SettlementInfo.save({"doctorId":$scope.doctorId,"date":saveCurrDate,"type":"appointment"},function(data){
                        //    $scope.settlementInfoData = data;
                        //    $scope.totalPrice = data.appointmentTotal;
                        //})

                            getphoneConsultDay(getCurrDate());

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

            //关闭提醒
            $scope.closeNotice = function () {
                $scope.updateAccount = false;
                $(".myAccount").addClass("closeNotice");
            }
            //前一天
            $scope.goPrev = function () {
                var prevDate = moment(saveCurrDate).subtract(1,'days').format("YYYY-MM-DD");
                changePrevNext(prevDate);
                $("#changeNext").removeClass("c2");
                setLog("WXZJB_MRQD_QHRQ");
            }
            //后一天
            $scope.goNext = function () {
                if(Date.parse(saveCurrDate)<Date.parse(getCurrDate())){
                    var next = moment(saveCurrDate).add(1,'days').format("YYYY-MM-DD");
                    changePrevNext(next);
                    if(Date.parse(next)==Date.parse(getCurrDate())){
                        $("#changeNext").addClass("c2");
                    }
                    setLog("WXZJB_MRQD_QHRQ");
                }
            }
            //弹出时间选择框
            $scope.showDateTime = function () {
                $("#dateTime").mobiscroll('show');
            }

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
                if( $scope.acceptLock==false){
                    $scope.acceptLock=true;
                }
                else{
                    $scope.acceptLock=false;
                }
                if( $scope.fineLock=="false"){
                    $scope.finetLock="true";
                }
                else{
                    $scope.fineLock="false";
                }
                $scope.phoneConsultLock=false;
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
            //电话咨询
            $scope.toggle4 = function () {
                $scope.phoneConsultLock==true?$scope.phoneConsultLock=false:$scope.phoneConsultLock=true;
                $scope.acceptLock=false;
            }
            //判断日期是否是当前及时间是否在晚上8：00
            var getphoneConsultDay = function (date) {
                if(Date.parse(date)==Date.parse(getCurrDate())){
                    if(getCurrTime()>="20:00"){
                        getAllAccountList(date);
                    }else{
                        $scope.noAccount = true;
                        $scope.checkAvailable = false;
                        $scope.havephoneConsult = false;
                    }
                }else{
                    getAllAccountList(date);
                }
            }

            //从数据库得到全部订单
            var getAllAccountList = function(date){
                $scope.pageLoading = true;
                GetDayList.save({"doctorId":$scope.doctorId,"date":date}, function (data) {
                    $scope.pageLoading = false;
                    if(data.appointment.timeList.length==0){
                        $scope.checkAvailable = false;
                    }else{
                        var sum = 0;
                        $scope.checkAvailable = true;
                        $scope.acceptLock = true;
                        $scope.noAccount = false;
                        $scope.appList = data.appointment.timeList;
                        $.each(data.appointment.timeList, function (index,value) {
                            sum+=sum+value.price;
                        });
                       // $scope.appMoney = data.appointment.totalPrice;
                        $scope.appMoney = sum;
                    }
                    if(data.phoneConsult.timeList.length==0){
                        $scope.havephoneConsult = false;
                    }else{
                        $scope.havephoneConsult = true;
                        $scope.phoneConsultLock = true;
                        $scope.noAccount = false;
                        $scope.phoneList = data.phoneConsult.timeList;
                        $scope.phoneMoney = data.phoneConsult.totalPrice;
                    }
                    if(data.appointment.timeList.length==0&&data.phoneConsult.timeList.length==0){
                        $scope.noAccount = true;
                        $scope.checkAvailable = false;
                        $scope.havephoneConsult = false;
                    }
                    if(data.appointment.timeList.length!=0&&data.phoneConsult.timeList.length!=0){
                        $scope.noAccount = false;
                        $scope.checkAvailable = true;
                        $scope.havephoneConsult = true;
                        $scope.acceptLock = true;
                        $scope.phoneConsultLock = false;
                    }
                });
            }

            //选择前一天、后一天时间数据的变化
            var changePrevNext = function (date) {
                $("#dateTime").val(date);
                $scope.currWeek = getChWeek(getWeekNum(date));
                saveCurrDate = date;
                getphoneConsultDay(date);
            }
            
            //得到当前日期
            var getCurrDate = function () {
                return moment().format("YYYY-MM-DD");
            }
            //得到当前时间
            var getCurrTime = function () {
                return moment().format("HH:mm");
            }
            //得到日期是周几的数字
            var getWeekNum = function (date) {
                return moment(date).format("d");
            }

            //判断当前日期是周几
            var getChWeek = function(num){
                switch (num){
                    case "0": return "周日";
                    case "1": return "周一";
                    case "2": return "周二";
                    case "3": return "周三";
                    case "4": return "周四";
                    case "5": return "周五";
                    case "6": return "周六";
                }
            }

            //保存日志
            var setLog = function (con) {
                var pData = {logContent:encodeURI(con)};
                $http({method:'post',url:'util/recordLogs',params:pData});
            }

    }]);
