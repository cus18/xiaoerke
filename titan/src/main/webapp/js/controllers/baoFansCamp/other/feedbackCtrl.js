angular.module('controllers', ['ionic']).controller('feedbackCtrl', [
        '$scope','$state','$stateParams','$timeout','saveFeedBack',
        function ($scope,$state,$stateParams,$timeout,saveFeedBack) {
            $scope.title = "意见反馈";
            $scope.info = {
                produce:true,
                error:false,
                contact : "提供产品反馈"
            };
            $scope.lock='false';

            var recordLogs = function(val){
                $.ajax({
                    url:"util/recordLogs",// 跳转到 action
                    async:true,
                    type:'get',
                    data:{logContent:encodeURI(val)},
                    cache:false,
                    dataType:'json',
                    success:function(data) {
                    },
                    error : function() {
                    }
                });
            }
            recordLogs("YJFK_YMDK");

            $scope.checked = function (flag) {
                if(flag == 'produce'){
                    $scope.info.produce = true;
                    $scope.info.error = false;
                    $scope.info.contact = "提供产品反馈"
                }else{
                    $scope.info.produce = false;
                    $scope.info.error = true;
                    $scope.info.contact = "错误报告"
                }
            };
            $scope.appointmentFirst = function () {
                window.location.href="firstPage/appoint";
            };

           $scope.submitAdvice = function () {
               recordLogs("YJFK_ANDJ");
               $scope.lock='true';
               saveFeedBack.save({advice: $scope.info.feedback, contact: $scope.info.contact}, function (data) {
                       $scope.lock='false';
                       wx.closeWindow();

            })
        };
    }]);