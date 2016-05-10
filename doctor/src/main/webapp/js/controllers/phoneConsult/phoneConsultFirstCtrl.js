angular.module('controllers', ['ionic']).controller('phoneConsultFirstCtrl', [
    '$scope','$ionicPopup','$state','$stateParams','$location','GetUserLoginStatus','GetConsultInfo','GetConsultTime','$http',
    function ($scope,$ionicPopup,$state,$stateParams,$location,GetUserLoginStatus,GetConsultInfo,GetConsultTime,$http) {
        $scope.title = "电话咨询";
        $scope.week = ["日","一","二","三","四","五","六"];
        $scope.prevWeekDisable = true;
        $scope.nextWeekDisable = false;
        $scope.consultN = false;//没有号源
        $scope.consultHave = true;//有号源
        var doctorId = "";
        var num ;//判断上下周次数
        var saveSunday ;//保存每周周日的日期
        var saveSaturday ;//保存每周周六的日期
        //var saveCurrDate ;//记录当前选中的日期

        $scope.$on('$ionicView.enter', function(){
            $scope.pageLoading = true;
            var routePath = "/phoneConsultDoctorBBBBBB" + $location.path();
            GetUserLoginStatus.save({routePath:routePath},function(data){
                $scope.pageLoading = false;
                if(data.status=="9") {
                    window.location.href = data.redirectURL;
                }else if(data.status=="8"){
                    window.location.href = data.redirectURL+"?targeturl="+routePath;
                }else{
                    var pData = {logContent:encodeURI("WXZJB_CD_DHZX")};
                    $http({method:'post',url:'util/recordLogs',params:pData});
                    var changeDate ;
                    if($stateParams.date==""&&sessionStorage.getItem("curr")==undefined){
                        changeDate = getCurrDate();
                    }else if(sessionStorage.getItem("curr")!=undefined){
                        changeDate = sessionStorage.getItem("curr");
                    }else if($stateParams.date!=""&&sessionStorage.getItem("curr")==undefined){
                        changeDate = $stateParams.date;
                    }
                    num = 0;
                    $("button").eq(0).addClass("changWeekCom");
                    $scope.cusultList = [];
                    getDateList(changeDate);
                    $scope.selectItem = getWeekNum(changeDate);
                    getDateWeek($scope.selectItem);
                    getConsultDateList(changeDate);

                }
            });
        });

        //上、下周选择
        $scope.selectWeek = function (index) {
            $scope.cusultList = [];
            if(num==0){
                if("next"==index){
                    num++;
                    getNextDate();
                    setPrevNext(false,false);
                    $("button").eq(0).removeClass("changWeekCom");
                }
            }else{
                if("prev"==index){
                    num--;
                    if(num==0){
                        $("button").eq(0).addClass("changWeekCom");
                        $("button").eq(1).removeClass("changWeekCom");
                        setPrevNext(true,false);
                        getPrevDate();
                    }else{
                        $("button").eq(0).removeClass("changWeekCom");
                        $("button").eq(1).removeClass("changWeekCom");
                        getPrevDate();
                    }
                }
                if("next"==index){
                    num++
                    if(num==3){
                        $("button").eq(1).addClass("changWeekCom");
                        $("button").eq(0).removeClass("changWeekCom");
                        setPrevNext(false,true);
                        getNextDate();
                    }else{
                        getNextDate();
                    }
                }
            }
        }

        //选择号源日期
        $scope.selectTime = function (index) {
            $scope.selectItem = index;
            getDateWeek(index);
            if($scope.consultFlag[index]==true){
                getConsultTime($scope.currDate);
            }else{
                $scope.consultN = true;//没有号源
                $scope.consultHave = false;//有号源
            }
        }

        //跳转到病情详情页
        $scope.goDetails = function (index) {
            if($scope.consultDetalisList[index].state=="1"){
                sessionStorage.setItem("curr",$scope.currDate);
                $state.go("phoneConsultDetails",{id:$scope.consultDetalisList[index].id,doctorId:doctorId});
            }
        }



        //获取本周有号源的日期
        var getConsultDateList = function(date){
            $scope.consultFlag = [];//保存本周有号源的日期
            GetConsultInfo.save({"date":$scope.cusultList[0].date},function(data){
                doctorId = data.doctorId;
                for(var i=0;i<$scope.cusultList.length;i++){
                    var flag = 0;
                    for(var j=0;j<data.dateList.length;j++){
                        if(data.dateList[j].date==$scope.cusultList[i].date){
                            flag = 1;
                            break ;
                        }
                    }
                    if(flag==1){
                        $scope.consultFlag.push(true);
                    }else{
                        $scope.consultFlag.push(false);
                    }
                }
                getConsultTime(date);//获取当前日期的预约情况

            });
        }


        //获取该日期下的所有号源信息内容
        var getConsultTime = function(date){
            GetConsultTime.save({"doctorId":doctorId,"date":date},function(data){
                if(data.consultPhoneTimeList.length==0){
                    $scope.consultN = true;//没有号源
                    $scope.consultHave = false;//有号源
                }else{
                    $scope.consultN = false;//没有号源
                    $scope.consultHave = true;//有号源
                    $scope.consultDetalisList = data.consultPhoneTimeList;
                }
            });
        }

        var getDateList = function(cu){
            var cun = getWeekNum(cu);
            $scope.cusultList = [];
            if(cun=="0"){
                for(var i=0;i<7;i++){
                    $scope.cusultList.push(addDate(cu,i));
                }
                saveSunday = addDate(cu,6).date;
                saveSaturday = cu;
            }else if(cun=="1"){
                $scope.cusultList.push(subDate(cu,1));
                for(var i=0;i<6;i++){
                    $scope.cusultList.push(addDate(cu,i));
                }
                saveSunday = addDate(cu,5).date;
                saveSaturday = subDate(cu,1).date;
            }else if(cun=="2"){
                for(var i=2;i>0;i--){
                    $scope.cusultList.push(subDate(cu,i));
                }
                for(var j=0;j<5;j++){
                    $scope.cusultList.push(addDate(cu,j));
                }
                saveSunday = addDate(cu,4).date;
                saveSaturday = subDate(cu,2).date;
            }else if(cun=="3"){
                for(var i=3;i>0;i--){
                    $scope.cusultList.push(subDate(cu,i));
                }
                for(var j=0;j<4;j++){
                    $scope.cusultList.push(addDate(cu,j));
                }
                saveSunday = addDate(cu,3).date;
                saveSaturday = subDate(cu,3).date;
            }else if(cun=="4"){
                for(var i=4;i>0;i--){
                    $scope.cusultList.push(subDate(cu,i));
                }
                for(var j=0;j<3;j++){
                    $scope.cusultList.push(addDate(cu,j));
                }
                saveSunday = addDate(cu,2).date;
                saveSaturday = subDate(cu,4).date;
            }else if(cun=="5"){
                for(var i=5;i>0;i--){
                    $scope.cusultList.push(subDate(cu,i));
                }
                for(var j=0;j<2;j++){
                    $scope.cusultList.push(addDate(cu,j));
                }
                saveSunday = addDate(cu,1).date;
                saveSaturday = subDate(cu,5).date;
            }else if(cun=="6"){
                saveSunday = cu;
                saveSaturday = subDate(cu,6).date;
                for(var i=0;i<7;i++){
                    $scope.cusultList.push(addDate(saveSaturday,i));
                }
            }


        }
        //对日期进行加
        var addDate = function(cu,i){
            var cudObject = {};
            cudObject.date = moment(cu).add(i,'days').format("YYYY-MM-DD");
            cudObject.da = moment(cudObject.date).format('DD');
            cudObject.week =getChWeek(getWeekNum(cudObject.date));
            return cudObject;
        }

        //对日期进行减
        var subDate = function (cu,i) {
            var cudObject = {};
            cudObject.date = moment(cu).subtract(i,'days').format("YYYY-MM-DD");
            cudObject.da = moment(cudObject.date).format('DD');
            cudObject.week = getChWeek(getWeekNum(cudObject.date));
            return cudObject;
        }

        //得到当前日期
        var getCurrDate = function () {
            return moment().format("YYYY-MM-DD");
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
        //得到选中的日期和周几
        var getDateWeek = function (index) {
            $scope.currDate = $scope.cusultList[index].date;
            $scope.currWeek = $scope.cusultList[index].week;
        }
        //设置上下周点击状态
        var setPrevNext = function (boolean1,boolean2) {
            $scope.prevWeekDisable = boolean1;
            $scope.nextWeekDisable = boolean2;
        }
        //选择下周时，获取下周时间列表
        var getNextDate = function(){
            getDateList(addDate(saveSunday,1).date);
            getConsultDateList($scope.cusultList[$scope.selectItem].date);
            getDateWeek($scope.selectItem);
        }
        //选择上周时，获取上周时间列表
        var getPrevDate = function () {
            getDateList(subDate(saveSaturday,1).date);
            getConsultDateList($scope.cusultList[$scope.selectItem].date);
            getDateWeek($scope.selectItem);
        }
    }]);

