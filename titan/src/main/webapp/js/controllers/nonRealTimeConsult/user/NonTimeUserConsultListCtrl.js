angular.module('controllers', []).controller('NonTimeUserConsultListCtrl', [
        '$scope','$state','$http','UserSessionList',
        function ($scope,$state,$http,UserSessionList) {
            $scope.selectItem = "cur";
            $scope.selectService=function(item){
                $scope.selectItem = item;
            };
            $scope.NonTimeUserConsultListInit = function(){
                //获取当前用户会话列表
                UserSessionList.save({},function (data) {
                    console.log(data)
                    $scope.curMessageList = data.sessionVoList
                })

            }
    }]);
