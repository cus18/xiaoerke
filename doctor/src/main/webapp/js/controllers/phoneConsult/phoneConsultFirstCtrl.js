angular.module('controllers', ['ionic']).controller('phoneConsultFirstCtrl', [
    '$scope','$ionicPopup','$state','$stateParams','$location','GetUserLoginStatus','GetConsultInfo','GetConsultTime',
    function ($scope,$ionicPopup,$state,$stateParams,$location,GetUserLoginStatus,GetConsultInfo,GetConsultTime) {
        $scope.title = "电话咨询";
        $scope.week = ["日","一","二","三","四","五","六"];
        $scope.date = ["13","14","15","16","17","18","19"];
        $scope.prevWeekDisable = false;
        $scope.nextWeekDisable = false;
        $scope.consultN = false;//没有号源
        $scope.consultHave = true;//有号源
        $scope.consultList1 = [{begin_time:"8:00",
                                state:"0",
                                repart:"0"},
                                {begin_time:"10:00",
                                    state:"1",
                                    repart:"1"},
                                {begin_time:"16:30",
                                    state:"0",
                                    repart:"0"}];


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
                    getDateList();
                    GetConsultInfo.save({"date":$scope.cusultList[0].date},function(data){
                        console.log("data",data);
                    });
                }
            })
        });
        
        $scope.selectWeek = function (index) {
            if("prev"==index){
                $scope.prevWeek = true;
            }else{
                $("button").eq(1).addClass("changWeekCom");
            }
        }

        //选择号源日期
        $scope.selectTime = function (index) {
            $scope.selectItem = index;
        }
        
        //
        $scope.goDetails = function (index) {
            $state.go("phoneConsultDetails");
        }


        var getDateList = function(){
            var cu = getCurrDate();
            var cun = getWeekNum(cu);
            $scope.cusultList = [];
            if(cun==0){
                for(var i=0;i<7;i++){
                    $scope.cusultList.push(addDate(cu,i));
                }
            }else if(cun==1){
                $scope.cusultList.push(subDate(cu,1));
                for(var i=0;i<6;i++){
                    $scope.cusultList.push(addDate(cu,i));
                }
            }else if(cun==2){
                for(var i=2;i>0;i--){
                    $scope.cusultList.push(subDate(cu,i));
                }
                for(var j=0;j<5;j++){
                    $scope.cusultList.push(addDate(cu,j));
                }
            }else if(cun==3){
                for(var i=3;i>0;i--){
                    $scope.cusultList.push(subDate(cu,i));
                }
                for(var j=0;j<4;j++){
                    $scope.cusultList.push(addDate(cu,j));
                }
            }else if(cun==4){
                for(var i=4;i>0;i--){
                    $scope.cusultList.push(subDate(cu,i));
                }
                for(var j=0;j<3;j++){
                    $scope.cusultList.push(addDate(cu,j));
                }
            }else if(cun==5){
                for(var i=5;i>0;i--){
                    $scope.cusultList.push(subDate(cu,i));
                }
                for(var j=0;j<2;j++){
                    $scope.cusultList.push(addDate(cu,j));
                }
            }else if(cun==6){
                for(var i=7;i>0;i--){
                    $scope.cusultList.push(subDate(cu,i));
                }
            }
            console.log("$scope.cusultList",$scope.cusultList);

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


    }]);

