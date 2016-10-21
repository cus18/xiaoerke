angular.module('controllers', []).controller('NonTimeUserConversationCtrl', [
        '$scope','$state','$stateParams','$timeout','$http','ConversationInfo','UpadateRecorde',
        function ($scope,$state,$stateParams,$timeout,$http,ConversationInfo,UpadateRecorde) {
            $scope.pageData =[]

            $scope.NonTimeUserConversationInit = function(){
                ConversationInfo.save({sessionId:$stateParams.sessionId},function (data) {
                    console.log(data)
                    $scope.pageData = data
                })
            }
            $scope.glued = true;
            $scope.msgType= "text";
            $scope.content = "";

            //发送消息
            $scope.sendMsg = function(){
                var information = {
                    "sessionid":$stateParams.sessionId,
                    "content": $scope.content,
                    "msgType": $scope.msgType
                };
                UpadateRecorde.save(information,function (data) {
                    if(data.state == "error"){
                        alert("请重新打开页面提交信息");
                    }
                })
            }

            //提交图片
            $scope.uploadFiles = function($files,fileType) {
                console.log('dataJsonValue');
                for (var i = 0; i < $files.length; i++) {
                    var file = $files[i];
                    $scope.upload = $upload.upload({
                        url: 'nonRealTimeConsultUser/uploadMediaFile',
                        file: file
                    }).progress(function(evt) {
                        console.log('percent: ' + parseInt(100.0 * evt.loaded / evt.total));
                    }).success(function(data, status, headers, config){
                        $scope.msgType= "img";
                        $scope.content = data.imgPath
                        $scope.sendMsg()
                    });
                }
            };

    }]);
