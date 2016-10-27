angular.module('controllers', ['ngFileUpload']).controller('NonTimeDoctorConversationCtrl', [
    '$scope','$state','$stateParams','$timeout','$http','$upload','ConversationDoctorInfo','GetDoctorLoginStatus','UpdateReCode',
    function ($scope,$state,$stateParams,$timeout,$http,$upload,ConversationDoctorInfo,GetDoctorLoginStatus,UpdateReCode) {
        $scope.info = {};
        $scope.msgType= "text";
        $scope.content = "";

        //添加表情
        $scope.getQQExpression = function () {
            $('#face').qqFace({
                id : 'facebox', //表情盒子的ID
                assign:'saytext', //给那个控件赋值
                path:'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf%2Fqqface%2F'	//表情存放的路径
            });
        };


        //发送图片
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
                    console.log("上传图片成功");

                });
            }
        };

        //发送消息
        $scope.sendTextMsg = function(){
            $scope.info.content =  $('#saytext').val();
            $scope.sendMsg("text",$scope.info.content);
        };
        //发送消息
        $scope.sendMsg = function(messageType,content){
            var information = {
                "sessionId":$stateParams.sessionId,
                "content": content,
                "msgType": messageType,
                "source":"doctor",
                "doctorId":$scope.doctorId
            };
            UpdateReCode.save(information,function (data) {
                if(data.state == "error"){
                    alert("请重新打开页面提交信息");
                }
                if(data.state == "success"){
                    console.log("更新会话信息",data);
                    $scope.messageList.push(data.conversationData);
                    $scope.info.content = "";
                }
            });
        };


        //页面初始化
        $scope.NonTimeDoctorConversationInit = function(){
            $scope.glued = true;
            //校验是否登陆
            GetDoctorLoginStatus.save({}, function (data) {
                $scope.pageLoading = false;
                if(data.status == "failure"){
                    window.location.href = "http://s201.xiaork.com/titan/nonRealTimeConsult#/NonTimeDoctorLogin";
                }
                else{
                    ConversationDoctorInfo.save({sessionId:$stateParams.sessionId,doctorId:$scope.doctorId},function (data) {
                        console.log("会话信息列表",data)
                        $scope.pageData = data;
                        $scope.messageList = data.messageList;
                        $scope.doctorId = $scope.pageData.doctorId;
                    })
                }
            })
            }
    }]);
