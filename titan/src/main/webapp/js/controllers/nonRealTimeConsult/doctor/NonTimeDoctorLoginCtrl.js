angular.module('controllers', []).controller('NonTimeDoctorLoginCtrl', [
    '$scope', '$state', '$timeout', '$http', 'doctorBinding',
    function ($scope, $state, $timeout, $http, doctorBinding) {
        $scope.doctorLock = false;//非系统医生提示开关
        $scope.errorLock = false;//错误提示开关
        $scope.errorRemindText ="";//错误提示内容
        $scope.info = {};
        //关闭提示
        $scope.close = function () {
            $scope.doctorLock = false;
        };

        $scope.doctorBindingAction = function () {
            doctorBinding.save({username: $scope.info.phoneNum, password: $scope.info.password}, function (data) {
                if (data.status == "failure") {
                    $scope.errorLock = true;
                    $scope.errorRemindText ="验证码错误"
                }
                else if(data.status == "notConsultDoctor"){
                    $scope.doctorLock = true;
                }else {
                    window.location.href = "http://s68.baodf.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s201.xiaork.com/keeper/wechatInfo/getDoctorWechatMenId?url=6";
                }
            });
        }


        $scope.getValidateCode = function () {
            if (countdown < 60) {
                return;
            }
            else {
                var partner = /^1[34578]\d{9}$/;
                if (!partner.exec($scope.info.phoneNum)) {
                    $scope.errorLock = true;
                    $scope.errorRemindText ="手机号格式不对"
                    return;
                }
                else {
                    var mydata = '{"userPhone":"'
                        + $scope.info.phoneNum + '"}';
                    $.ajax({
                        url: "util/user/getCode",
                        async: true,
                        type: 'post',
                        data: mydata,
                        cache: false,
                        contentType: "application/json; charset=utf-8",
                        dataType: 'json',
                        success: function () {

                        },
                        error: function () {

                        }
                    });
                    $scope.lockValidateCode();
                }
            }
        }

        $scope.lockValidateCode = function () {
            if (countdown == 0) {
                $('#validateCode').html("获取验证码");
                countdown = 60;
                return;
            } else {
                $('#validateCode').html("重新发送(" + countdown + ")");
                countdown--;
            }
            setTimeout(function () {
                    $scope.lockValidateCode()
                }
                , 1000)
        };

        var countdown = 60;
        $scope.NonTimeDoctorLoginInit=function(){
            $(".tips").css("margin-top",screen.height-380-screen.width*0.25+"px")
        }

    }]);
