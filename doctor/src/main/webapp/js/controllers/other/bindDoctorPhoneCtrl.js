angular.module('controllers', ['ionic']).controller('bindDoctorPhoneCtrl', [
    '$scope','$state','$stateParams','IdentifyDoctor','$timeout',
    function ($scope,$state,$stateParams,IdentifyDoctor,$timeout) {
        $scope.title0 = "快速登录"
        $scope.userGetCode = "获取验证码"
        $scope.info = {};
        $scope.key = true
        $scope.info.disable = false  //手机号无效
        $scope.info.disableMsg =false
        $scope.info.userGetCode = "获取验证码"
        $scope.info.show = false
        $scope.showIdentify = false;
        $scope.dealLock = "true";
        $scope.dealImg = 'http://xiaoerke-doctor-pic.oss-cn-beijing.aliyuncs.com/images%2Fdeal_y.png';

        var getQueryString = function(name) {
            var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
            var r = window.location.search.substr(1).match(reg);
            if (r != null) return unescape(r[2]); return null;
        }

        $scope.deal = function(){
            if($scope.dealLock == "true"){
                $scope.dealLock = "false";
                $scope.dealImg = 'http://xiaoerke-doctor-pic.oss-cn-beijing.aliyuncs.com/images%2Fdeal_y.png';
            }
            else{
                $scope.dealLock = "true";
                $scope.dealImg = 'http://xiaoerke-doctor-pic.oss-cn-beijing.aliyuncs.com/images%2Fdeal_y.png';
            }
        }

        $scope.goBack = function(){
            history.back();
        }

        $scope.UrlDecode = function(zipStr){
            var uzipStr="";
            for(var i=0;i<zipStr.length;i++){
                var chr = zipStr.charAt(i);
                if(chr == "+"){
                    uzipStr+=" ";
                }else if(chr=="%"){
                    var asc = zipStr.substring(i+1,i+3);
                    if(parseInt("0x"+asc)>0x7f){
                        uzipStr+=decodeURI("%"+asc.toString()+zipStr.substring(i+3,i+9).toString());
                        i+=8;
                    }else{
                        uzipStr+=$scope.AsciiToString(parseInt("0x"+asc));
                        i+=2;
                    }
                }else{
                    uzipStr+= chr;
                }
            }
            return uzipStr;
        }

        $scope.AsciiToString = function(asccode){
            return String.fromCharCode(asccode);
        }

        $scope.sendMsg = function(){
            var patrn=/^1[3578]\d{9}$/;
            if (!patrn.exec($scope.info.userPhoneNum)){
                $scope.info.show = true
                setTimeout(function() {
                    $scope.info.show = false
                }, 2000);
                return;
            }else{
                $scope.info.disable = true
                $scope.info.disableMsg =true
                $scope.info.userGetCode = 60;
                $scope.msgInfo = "验证码已发送到您的手机"
                $scope.info.msgShow = true
                setTimeout(function() {
                    $scope.info.msgShow = false
                }, 2000);
                var myTime = setInterval(function() {
                    $scope.info.userGetCode--;
                    $scope.$digest(); // 通知视图模型的变化
                }, 1000);
                $scope.info.disable = true
                // 倒计时10-0秒，但算上0的话就是11s
                setTimeout(function() {
                    clearInterval(myTime);
                    $scope.info.userGetCode = "再次发送";
                    $scope.info.disableMsg =false
                    $scope.$digest(); // 通知视图模型的变化
                }, 60000);
                $scope.pageLoading = true;
                IdentifyDoctor.save({userPhone:$scope.info.userPhoneNum},function(data){
                    $scope.pageLoading = false;
                    $scope.info.disable = false
                });
            }
        }

        $scope.$on('$ionicView.enter', function(){
            if($stateParams.redirect.indexOf("doctorFirst")!=-1)
            {
                $scope.info.redirectParam = "doctorFirst/," + moment().format("YYYY-MM-DD");
            }
            else
            {
                console.log($stateParams.redirect);
                $scope.info.redirectParam = $stateParams.redirect;
            }

            var uPhone =  getQueryString("username");
            if(uPhone !=null){
                $scope.info.userPhoneNum = uPhone
            }
            if(getQueryString("message") !=null){
                $scope.identifyStatus = "验证码有误"
                $scope.showIdentify = true
                var timer = $timeout(
                    function() {
                        $scope.identifyStatus='';
                    },
                    5000
                );
            }
        })
    }])
