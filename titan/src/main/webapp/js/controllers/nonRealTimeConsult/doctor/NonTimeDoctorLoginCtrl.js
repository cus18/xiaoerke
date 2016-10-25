angular.module('controllers', []).controller('NonTimeDoctorLoginCtrl', [
    '$scope', '$state', '$timeout', '$http', 'doctorBinding',
    function ($scope, $state, $timeout, $http, doctorBinding) {

        $scope.prizeArray = {};

        $scope.doctorBindingAction = function () {
            $scope.username = $('#username').val();
            $scope.password = $('#password').val();
            doctorBinding.save({username: $scope.username, password: $scope.password}, function (data) {
                if (data.status = "failure") {
                    alert("验证码错误！");
                } else {
                    window.location.href = "http://localhost/titan/nonRealTimeConsult#/NonTimeDoctorConversation";
                }
            });
        }


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


    }]);
