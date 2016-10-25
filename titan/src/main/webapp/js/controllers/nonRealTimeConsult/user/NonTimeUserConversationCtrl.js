var app = angular.module('controllers', ['ngFileUpload']).controller('NonTimeUserConversationCtrl', [
        '$scope','$state','$stateParams','$upload','ConversationInfo','UpdateReCode',
        function ($scope,$state,$stateParams,$upload,ConversationInfo,UpdateReCode) {
            $scope.glued = true;
            $scope.msgType= "text";
            $scope.content = "";
            $scope.info = [];
            $scope.info.content = "";
            $scope.messageList = [];
            $scope.NonTimeUserConversationInit = function(){
                $scope.getQQExpression();
                $scope.getQQExpression();
                ConversationInfo.save({sessionId:$stateParams.sessionId},function (data) {
                    $scope.pageData = data;
                    $scope.messageList = $scope.pageData.messageList;
                })
            };
            //发送消息
            $scope.sendMsg = function(messageType,content){
                var information = {
                    "sessionId":$stateParams.sessionId,
                    "content": content,
                    "msgType": messageType
                };
                UpdateReCode.save(information,function (data) {
                    if(data.state == "error"){
                        alert("请重新打开页面提交信息");
                    }
                    if(data.state == "success"){
                        $scope.messageList.push(data.conversationData);
                        $scope.info.content = "";
                    }
                });
            };
            //再次咨询
            $scope.againConsulting = function(){
                $state.go("NonTimeUserFirstConsult",{"doctorId":$scope.pageData.doctorId});
            };
            //送心意
            $scope.giveMind = function(){
                //$state.go("NonTimeUserFirstConsult",{"doctorId":$scope.pageData.doctorId});
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
                        $scope.sendMsg("img",data.imgPath);
                        //
                        //
                        // $scope.messageList.push(data.conversationData);
                    });
                }
            };

            //发送消息
            $scope.sendTextMsg = function(){
                $scope.info.content =  $('#saytext').val();
                $scope.sendMsg("text",$scope.info.content);
            };
            //发送表情
            $scope.getQQExpression = function () {
                $('#face').qqFace({
                    id: 'facebox',
                    assign: 'saytext',
                    path: 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf%2Fqqface%2F'
                });
            };
        }]);
