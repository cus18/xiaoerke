angular.module('controllers', []).controller('NonTimeDoctorConversationCtrl', [
        '$scope','$state','$stateParams','$timeout','$http','$upload','ConversationInfo','GetDoctorLoginStatus',
        function ($scope,$state,$stateParams,$timeout,$http,$upload,ConversationInfo,GetDoctorLoginStatus) {

            //添加表情
            $scope.getQQExpression = function () {
                $('#face').qqFace({
                    id : 'facebox', //表情盒子的ID
                    assign:'saytext', //给那个控件赋值
                    path:'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf%2Fqqface%2F'	//表情存放的路径
                });
            };
            //表情转换
            function replace_em(str){
                str = str.replace(/\</g,'&lt;');
                str = str.replace(/\>/g,'&gt;');
                str = str.replace(/\n/g,'<br/>');
                str = str.replace(/\[em_([0-9]*)\]/g,'<img src="face/$1.gif" border="0" />');
                return str;
            }

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

                    }).success(function(data, status, headers, config) {
                        // 文件上传成功处理函数
                        console.log(data);

                    }).error(function(data, status, headers, config) {
                        //失败处理函数
                        console.log('上传失败');
                    });
                };
            };

           /* $scope.onFileSelect = function($files) {
                var dataValue = {
                    "fileType": "image"
                };
                var dataJsonValue = JSON.stringify(dataValue);
                console.log('dataJsonValue 111',JSON.stringify(dataValue));
               /!* for (var i = 0; i < $files.length; i++) {
                    var file = $files[i];
                    $scope.upload = $upload.upload({
                        url: 'consult/h5/uploadMediaFile',
                        data: encodeURI(dataJsonValue),
                        file: file
                    }).progress(function(evt) {
                        console.log('show percent: ' + parseInt(100.0 * evt.loaded / evt.total));
                    }).success(function(data, status, headers, config){
                        var patientValMessage = {
                            "type": 1,
                            "content": data.showFile,
                         /!*   "dateTime": moment().format('YYYY-MM-DD HH:mm:ss'),*!/
                            "senderId": $scope.patientId,
                            "senderName": $scope.senderName,
                            "sessionId":$scope.sessionId,
                            "avatar":"http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf%2Fconsult%2Fyonghumoren.png"
                        };
                        console.log("finish " ,patientValMessage.content);
                    });
                }*!/
            };*/
            //页面初始化
            //校验是否登陆
            GetDoctorLoginStatus.save({}, function (data) {
                $scope.pageLoading = false;
                if (data.status == "failure") {
                    window.location.href = "http://127.0.0.1/titan/nonRealTimeConsult#/NonTimeDoctorLogin";
                } else {
                    $scope.NonTimeDoctorConversationInit = function(){
                        $scope.glued = true;
                        ConversationInfo.save({sessionId:$stateParams.sessionId},function (data) {
                            console.log(data)
                            $scope.pageData = data;
                        })
                    };
                }
            });

            }

    }]);
