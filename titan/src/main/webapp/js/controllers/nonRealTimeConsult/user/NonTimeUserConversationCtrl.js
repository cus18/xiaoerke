angular.module('controllers', []).controller('NonTimeUserConversationCtrl', [
        '$scope','$state','$stateParams','$timeout','$http','ConversationInfo',
        function ($scope,$state,$stateParams,$timeout,$http,ConversationInfo) {
            $scope.pageData =[]

            $scope.NonTimeUserConversationInit = function(){
                ConversationInfo.save({sessionId:$stateParams.sessionId},function (data) {
                    console.log(data)
                    $scope.pageData = data
                })
            }
            $scope.glued = true;
    }]);
