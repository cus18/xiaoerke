angular.module('controllers', []).controller('NonTimeUserConsultListCtrl', [
        '$scope','$state','UserSessionList',
        function ($scope,$state,UserSessionList) {
            $scope.selectItem = "cur";
            $scope.selectService=function(item){
                $scope.selectItem = item;
            };
            $scope.NonTimeUserConsultListInit = function(){
                //获取当前用户会话列表
                UserSessionList.save({},function (data) {
                    if('error'==data.status ){
                        alert("请重新打开页面");
                        return;
                    }

                    $scope.curMessageList = data.sessionVoList;
                })

            }
            
            $scope.conversationPage = function (sessionid){
                $state.go('NonTimeUserConversation',{"sessionId":sessionid});
            }
    }]);
