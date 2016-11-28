angular.module('controllers', ['ionic']).controller('babyCoinTicketPayCtrl', [
    '$scope','$state','$stateParams',
    function ($scope,$state,$stateParams) {

        $scope.commitLock=true;



        //提交评价
        $scope.startConsult = function () {
            WeixinJSBridge.call('closeWindow');

        };
        $scope.$on('$ionicView.enter', function(){


        });

        function recordLogs(val){
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
        };


    }]);
