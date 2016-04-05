angular.module('controllers', ['ionic']).controller('playtourEvaluateCtrl', [
    '$scope','$state','$stateParams','findCustomerEvaluation','$http',
    function ($scope,$state,$stateParams,findCustomerEvaluation,$http) {

        $scope.playtoutEvaluateImg =["","http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/playtour%2Fbumanyi_weixuanzhong.png",//不满意
            "","http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/playtour%2Fmanyiweixuanzhong.png",//满意
            "","http://xiaoerke-wxapp-pic.oss-cn-hangzhou.aliyuncs.com/playtour%2Ffeichangmanyiweixuanzhong.png"]//非常满意

        $scope.evaluation={};
        $scope.star={};
        $scope.doctorHeadImage={};
        $scope.dissatisfied="";

        $scope.$on('$ionicView.enter', function(){
            var id=$stateParams.id;
            findCustomerEvaluation.save({"id":id}, function (data){
                $scope.evaluation=data.evaluation;
                $scope.star=data.starInfo;
                var goodNum=$scope.star.startNum+"";
                var dissatisfied=$scope.evaluation.dissatisfied;
                dissatisfied=dissatisfied.split(",");
                for(var i=0;i<dissatisfied.length;i++){
                    if(dissatisfied[i]=="0"){
                        $scope.dissatisfied+="服务态度       ";
                    }
                    if(dissatisfied[i]=="1"){
                        $scope.dissatisfied+="专业水平       ";
                    }
                    if(dissatisfied[i]=="2"){
                        $scope.dissatisfied+="响应速度       ";
                    }
                    if(dissatisfied[i]=="3"){
                        $scope.dissatisfied+="其他       ";
                    }
                }
                $scope.star.goodNum=goodNum.split(".")[0];
                $scope.doctorHeadImage=data.doctorHeadImage;
            });

        });


        $scope.goShare = function () {
            var pData = {logContent:encodeURI("ZXPJWC_FX")};
            $http({method:'post',url:'util/recordLogs',params:pData});
            $state.go("playtourShare",{id:1});
        }






    }]);
