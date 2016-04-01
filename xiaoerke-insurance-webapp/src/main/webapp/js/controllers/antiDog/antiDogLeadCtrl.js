angular.module('controllers', ['ionic']).controller('antiDogLeadCtrl', [
    '$scope','$state','$stateParams','$http','getInsuranceRegisterServiceVisitLeadPageLogByOpenid',
    function ($scope,$state,$stateParams,$http,getInsuranceRegisterServiceVisitLeadPageLogByOpenid) {

        $scope.num = "";

        $scope.onSwipeUp = function(){
            $state.go('antiDogIndex');
        };
        $scope.startEnter = function(){
            $state.go('antiDogIndex');
        };



        $scope.$on('$ionicView.enter', function(){
        	getInsuranceRegisterServiceVisitLeadPageLogByOpenid.get(function (data){
             	if(data.log>0){
             		$state.go("antiDogIndex");
                 }else{
                    var pData = {logContent:encodeURI("FQB_YDY")};
                 	$http({method:'post',url:'ap/util/recordLogs',params:pData});
                 }
             });
           /* initLight()*/
        })
    }]);

var initLight = function(){
    var configStart = {latency: 10};
    var configStop = {latency: 10};
    $(".antiDogLead")
        .on('scrollstart', configStart, function() {
            window.location.href = "/xiaoerke-insurance-webapp/ap/insurance#/antiDogIndex";
        })

}

