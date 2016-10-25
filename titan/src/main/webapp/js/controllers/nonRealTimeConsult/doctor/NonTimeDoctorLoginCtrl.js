angular.module('controllers', []).controller('NonTimeDoctorLoginCtrl', [
    '$scope', '$state', '$timeout', '$http', 'doctorBinding',
    function ($scope, $state, $timeout, $http, doctorBinding) {

        $scope.prizeArray = {};

        //页面初始化
        $scope.NonTimeDoctorLoginInit = function () {
            document.title = "医生登陆"; //title
            doctorBinding.save({}, function (data) {

            });
        };

        $scope.getValidateCode = function () {
            if (countdown < 60) {
                return;
            }
            else {
                var partner = /^1[3578]\d{9}$/;
                if (!partner.exec($('#username').val())) {
                    $('#info').html("手机号格式不对");
                    return;
                }
                else {
                    var mydata = '{"userPhone":"'
                        + $('#username').val() + '"}';
                    $.ajax({
                        url: "util/user/getCode",
                        async: true,
                        type: 'post',
                        data: mydata,
                        cache: false,
                        contentType: "application/json; charset=utf-8",
                        dataType: 'json',
                        success: function (data) {

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



    }]);
