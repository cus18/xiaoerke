define(['app','bdfFactory'], function(app,bdfFactory){

    app.controller('stateCtrl', [
        '$scope','CheckBind','$state','$stateParams',
        function ($scope,$state) {
            $scope.info1 = "做患者的知心朋友"
            $scope.info2 = "我们正在为医患连接而鏖战，努力开发到更好!"
            $scope.usersubmit = "绑定"
    }])

})