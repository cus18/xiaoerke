angular.module('controllers', ['ionic']).controller('playtourEvaluateCtrl', [
    '$scope','$state','$stateParams','findCustomerEvaluation',
    function ($scope,$state,$stateParams,findCustomerEvaluation) {

        $scope.playtoutEvaluateImg =["http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/playtour%2Fbumanyi_weixuanzhong.png",//不满意
        "http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/playtour%2Fmanyiweixuanzhong.png",//满意
            "http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/playtour%2Ffeichangmanyiweixuanzhong.png"]//非常满意

        $scope.evaluation={};
		$scope.star={};
		$scope.doctorHeadImage={};

        $scope.$on('$ionicView.enter', function(){
        	var id=$stateParams.id;
        	findCustomerEvaluation.save({"id":id}, function (data){
        		$scope.evaluation=data.evaluation;
        		$scope.star=data.starInfo;
        		$scope.doctorHeadImage=data.doctorHeadImage;
             });

        });


        $scope.goShare = function () {
            $state.go("playtourShare");
        }

    }]);
