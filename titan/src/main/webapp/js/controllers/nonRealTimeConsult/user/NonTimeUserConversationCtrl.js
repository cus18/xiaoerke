angular.module('controllers', ['ngFileUpload']).controller('NonTimeUserConversationCtrl', [
        '$scope','$state','$stateParams','$upload','ConversationInfo','UpdateReCode',
        function ($scope,$state,$stateParams,$upload,ConversationInfo,UpdateReCode) {
            $scope.glued = true;
            $scope.msgType= "text";
            $scope.content = "";
            $scope.info = [];
            $scope.messageList = [];
            $scope.NonTimeUserConversationInit = function(){
                ConversationInfo.save({sessionId:$stateParams.sessionId},function (data) {
                    $scope.pageData = data;
                    $scope.messageList = $scope.pageData.messageList;
                    console.log("$scope.pageData",$scope.pageData.messageList);
                    console.log("$scope.messageList ",$scope.messageList[0].imgPath);

                })
            };
            //发送消息
            $scope.sendMsg = function(){
                var conversation = {
                    message:$scope.info.content,
                    messageTime:"",
                    messageType:"send",
                    source:"user"
                };

                var information = {
                    "sessionId":$stateParams.sessionId,
                    "content": $scope.info.content,
                    "msgType": $scope.msgType
                };
                UpdateReCode.save(information,function (data) {
                    if(data.state == "error"){
                        alert("请重新打开页面提交信息");
                    }
                    if(data.state == "success"){
                        $scope.messageList.push(data.conversationData);
                    }
                });
            };
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
                        $scope.photoList.push(data.imgPath)
                    });
                }
            };

    }]);
