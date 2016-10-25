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

                        $scope.messageList.push(data.conversationData);
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

            //过滤媒体数据
            var filterMediaData = function (val) {
                if(val.senderId==$scope.patientId){
                    if (val.type == "0") {
                        val.content = $sce.trustAsHtml(replace_em(emotionReceiveFilter(angular.copy(val.content))));
                    }
                }else{
                    if (val.type == "2"||val.type == "3") {
                        val.content = $sce.trustAsResourceUrl(angular.copy(val.content));
                    }else if(val.type == "0"){
                        val.content = $sce.trustAsHtml(replace_em(emotionReceiveFilter(angular.copy(val.content))));
                    }
                }
            };
            //查看结果
            var replace_em = function (str) {
                str = str.replace(/\</g,'&lt;');
                str = str.replace(/\>/g,'&gt;');
                str = str.replace(/\n/g,'<br/>');
                str = str.replace(/\[em_([0-9]*)\]/g,'<img src="http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf%2Fqqface%2F$1.gif" border="0" />');
                return str;
            };
            var emotionReceiveFilter = function(val){
                val = val.replace(/\/::\)/g, '[em_1]');val = val.replace(/\/::~/g, '[em_2]');val = val.replace(/\/::B/g, '[em_3]');val = val.replace(/\/::\|/g, '[em_4]');
                val = val.replace(/\/:8-\)/g, '[em_5]');val = val.replace(/\/::</g, '[em_6]');val = val.replace(/\/::X/g, '[em_7]');val = val.replace(/\/::Z/g, '[em_8]');
                val = val.replace(/\/::</g, '[em_9]');val = val.replace(/\/::-\|/g, '[em_10]');val = val.replace(/\/::@/g, '[em_11]');val = val.replace(/\/::P/g, '[em_12]');
                val = val.replace(/\/::D/g, '[em_13]');val = val.replace(/\/::O/g, '[em_14]');val = val.replace(/\/::\(/g, '[em_15]');val = val.replace(/\/:--b/g, '[em_16]');
                val = val.replace(/\/::Q/g, '[em_17]');val = val.replace(/\/::T/g, '[em_18]');val = val.replace(/\/:,@P/g, '[em_19]');val = val.replace(/\/:,@-D/g, '[em_20]');
                val = val.replace(/\/::d/g, '[em_21]');val = val.replace(/\/:,@-o/g, '[em_22]');val = val.replace(/\/::g/g, '[em_23]');val = val.replace(/\/:\|-\)/g, '[em_24]');
                val = val.replace(/\/::!/g, '[em_25]');val = val.replace(/\/::L/g, '[em_26]');val = val.replace(/\/::>/g, '[em_27]');val = val.replace(/\/::,@/g, '[em_28]');
                val = val.replace(/\/:,@f/g, '[em_29]');val = val.replace(/\/::-S/g, '[em_30]');val = val.replace(/\/:\?/g, '[em_31]');val = val.replace(/\/:,@x/g, '[em_32]');
                val = val.replace(/\/:,@@/g, '[em_33]');val = val.replace(/\/::8/g, '[em_34]');val = val.replace(/\/:,@!/g, '[em_35]');val = val.replace(/\/:xx/g, '[em_36]');
                val = val.replace(/\/:bye/g, '[em_37]');val = val.replace(/\/:wipe/g, '[em_38]');val = val.replace(/\/:dig/g, '[em_39]');val = val.replace(/\/:&-\(/g, '[em_40]');
                val = val.replace(/\/:B-\)/g, '[em_41]');val = val.replace(/\/:<@/g, '[em_42]');val = val.replace(/\/:@>/g, '[em_43]');val = val.replace(/\/::-O/g, '[em_44]');
                val = val.replace(/\/:>-\|/g, '[em_45]');val = val.replace(/\/:P-\(/g, '[em_46]');val = val.replace(/\/::'\|/g, '[em_47]');val = val.replace(/\/:X-\)/g, '[em_48]');
                val = val.replace(/\/::\*/g, '[em_49]');val = val.replace(/\/:@x/g, '[em_50]');val = val.replace(/\/:8\*/g, '[em_51]');val = val.replace(/\/:hug/g, '[em_52]');
                val = val.replace(/\/:moon/g, '[em_53]');val = val.replace(/\/:sun/g, '[em_54]');val = val.replace(/\/:bome/g, '[em_55]');val = val.replace(/\/:!!!/g, '[em_56]');
                val = val.replace(/\/:pd/g, '[em_57]');val = val.replace(/\/:pig/g, '[em_58]');val = val.replace(/\/:<W>/g, '[em_59]');val = val.replace(/\/:coffee/g, '[em_60]');
                val = val.replace(/\/:eat/g, '[em_61]');val = val.replace(/\/:heart/g, '[em_62]');val = val.replace(/\/:strong/g, '[em_63]');val = val.replace(/\/:weak/g, '[em_64]');
                val = val.replace(/\/:share/g, '[em_65]');val = val.replace(/\/:v/g, '[em_66]');val = val.replace(/\/:@\)/g, '[em_67]');val = val.replace(/\/:jj/g, '[em_68]');
                val = val.replace(/\/:ok/g, '[em_69]');val = val.replace(/\/:no/g, '[em_70]');val = val.replace(/\/:rose/g, '[em_71]');val = val.replace(/\/:fade/g, '[em_72]');
                val = val.replace(/\/:showlove/g, '[em_73]');val = val.replace(/\/:love/g, '[em_74]');val = val.replace(/\/:<L>/g, '[em_75]');
                return val;
            };

        }]);
