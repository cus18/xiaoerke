angular.module('controllers', ['ionic']).controller('phoneConsultFirstCtrl', [
    '$scope','$ionicPopup','$state','$stateParams','$location','GetUserLoginStatus',
    function ($scope,$ionicPopup,$state,$stateParams,$location,GetUserLoginStatus) {
        $scope.title = "电话咨询";
        $scope.week = ["日","一","二","三","四","五","六"];
        $scope.date = ["13","14","15","16","17","18","19"];
        $scope.prevWeekDisable = false;
        $scope.nextWeekDisable = false;
        $scope.consultN = false;//没有号源
        $scope.consultHave = true;//有号源
        $scope.consultList = [{begin_time:"8:00",
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



    }]);

