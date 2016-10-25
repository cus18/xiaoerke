angular.module('controllers', ['ngFileUpload']).controller('NonTimeUserConversationCtrl', [
        '$scope','$state','$stateParams','$upload','ConversationInfo','UpdateReCode',
        function ($scope,$state,$stateParams,$upload,ConversationInfo,UpdateReCode) {
            $scope.glued = true;
            $scope.msgType= "text";
            $scope.content = "";
            $scope.info = [];
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
            var emotionSendFilter = function(val){
                val = val.replace(/\[em_1\]/g, '/::)');val = val.replace(/\[em_2\]/g, '/::~');val = val.replace(/\[em_3\]/g, '/::B');val = val.replace(/\[em_4\]/g, '/::|');
                val = val.replace(/\[em_5\]/g, '/:8-)');val = val.replace(/\[em_6\]/g, '/::<');val = val.replace(/\[em_7\]/g, '/::X');val = val.replace(/\[em_8\]/g, '/::Z');
                val = val.replace(/\[em_9\]/g, '/::<');val = val.replace(/\[em_10\]/g, '/::-|');val = val.replace(/\[em_11\]/g, '/::@');val = val.replace(/\[em_12\]/g, '/::P');
                val = val.replace(/\[em_13\]/g, '/::D');val = val.replace(/\[em_14\]/g, '/::O');val = val.replace(/\[em_15\]/g, '/::(');val = val.replace(/\[em_16\]/g, '/:--b');
                val = val.replace(/\[em_17\]/g, '/::Q');val = val.replace(/\[em_18\]/g, '/::T');val = val.replace(/\[em_19\]/g, '/:,@P');val = val.replace(/\[em_20\]/g, '/:,@-D');
                val = val.replace(/\[em_21\]/g, '/::d');val = val.replace(/\[em_22\]/g, '/:,@-o');val = val.replace(/\[em_23\]/g, '/::g');val = val.replace(/\[em_24\]/g, '/:|-');
                val = val.replace(/\[em_25\]/g, '/::!');val = val.replace(/\[em_26\]/g, '/::L');val = val.replace(/\[em_27\]/g, '/::>');val = val.replace(/\[em_28\]/g, '/::,@');
                val = val.replace(/\[em_29\]/g, '/:,@f');val = val.replace(/\[em_30\]/g, '/::-S');val = val.replace(/\[em_31\]/g, '/:?');val = val.replace(/\[em_32\]/g, '/:,@x');
                val = val.replace(/\[em_33\]/g, '/:,@@');val = val.replace(/\[em_34\]/g, '/::8');val = val.replace(/\[em_35\]/g, '/:,@!');val = val.replace(/\[em_36\]/g, '/:xx');
                val = val.replace(/\[em_37\]/g, '/:bye');val = val.replace(/\[em_38\]/g, '/:wipe');val = val.replace(/\[em_39\]/g, '/:dig');val = val.replace(/\[em_40\]/g, '/:&-(');
                val = val.replace(/\[em_41\]/g, '/:B-)');val = val.replace(/\[em_42\]/g, '/:<@');val = val.replace(/\[em_43\]/g, '/:@>');val = val.replace(/\[em_44\]/g, '/::-O');
                val = val.replace(/\[em_45\]/g, '/:<-|');val = val.replace(/\[em_46\]/g, '/:P-(');val = val.replace(/\[em_47\]/g, '/::"|');val = val.replace(/\[em_48\]/g, '/:X-)');
                val = val.replace(/\[em_49\]/g, '/::*');val = val.replace(/\[em_50\]/g, '/:@x');val = val.replace(/\[em_51\]/g, '/:8*');val = val.replace(/\[em_52\]/g, '/:hug');
                val = val.replace(/\[em_53\]/g, '/:moon');val = val.replace(/\[em_54\]/g, '/:sun');val = val.replace(/\[em_55\]/g, '/:bome');val = val.replace(/\[em_56\]/g, '/:!!!');
                val = val.replace(/\[em_57\]/g, '/:pd');val = val.replace(/\[em_58\]/g, '/:pig');val = val.replace(/\[em_59\]/g, '/:<W>');val = val.replace(/\[em_60\]/g, '/:coffee');
                val = val.replace(/\[em_61\]/g, '/:eat');val = val.replace(/\[em_62\]/g, '/:heart');val = val.replace(/\[em_63\]/g, '/:strong');val = val.replace(/\[em_64\]/g, '/:weak');
                val = val.replace(/\[em_65\]/g, '/:share');val = val.replace(/\[em_66\]/g, '/:v');val = val.replace(/\[em_67\]/g, '/:@)');val = val.replace(/\[em_68\]/g, '/:jj');
                val = val.replace(/\[em_69\]/g, '/:ok');val = val.replace(/\[em_70\]/g, '/:no');val = val.replace(/\[em_71\]/g, '/:rose');val = val.replace(/\[em_72\]/g, '/:fade');
                val = val.replace(/\[em_73\]/g, '/:showlove');val = val.replace(/\[em_74\]/g, '/:love');val = val.replace(/\[em_75\]/g, '/<L>');
                return val;
            };

        }]);
