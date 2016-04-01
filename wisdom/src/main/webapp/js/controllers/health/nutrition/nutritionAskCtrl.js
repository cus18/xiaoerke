angular.module('controllers', ['ionic']).controller('nutritionAskCtrl', [
    '$scope','$state','$stateParams','SaveAppraisal',
    function ($scope,$state,$stateParams,SaveAppraisal) {


        $scope.$on('$ionicView.enter', function(){
            $scope.info = {};
        });

        $scope.save = function () {
            if($scope.info.content!=undefined&&$scope.info.content!=""){
                SaveAppraisal.save({"planTemplateId":2,"appraisal":$scope.info.content}, function (data){
                    if(data.resultCode == 0){
                        $state.go("nutritionAskSuccess");
                    }else{
                        alert("保存评论失败！");
                    }
                });
            }else{
                alert("提问内容不能为空！")
            }
        }



    }]);
