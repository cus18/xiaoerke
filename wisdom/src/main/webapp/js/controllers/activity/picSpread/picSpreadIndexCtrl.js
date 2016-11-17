angular.module('controllers', ['ionic']).controller('picSpreadIndexCtrl', [
    '$scope','$state','$stateParams',
    function ($scope,$state,$stateParams) {

        $scope.title ="童言趣语小故事";
        $scope.info = {};
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
        };
        recordLogs('TPCB_HDY');

        $scope.makePic = function(){

            if($scope.info.name == ""){
                alert("请输入宝宝姓名");
            }
            $state.go("picSpreadResult",{"name":$scope.info.name});
        };

        $scope.$on('$ionicView.enter', function() {
            $(".main-wrap").css("height",screen.height+"px")

        });
    }]);

