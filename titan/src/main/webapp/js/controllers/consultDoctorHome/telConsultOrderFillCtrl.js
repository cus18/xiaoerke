angular.module('controllers', ['ionic']).controller('telConsultOrderFillCtrl', [
    '$scope','$state','$stateParams','CreateConnect',
    function ($scope,$state,$stateParams,CreateConnect) {

        $scope.info = {};

        // 点击 提交订单
        $scope.submitOrder=function(){
            CreateConnect.save({"babyName":$scope.info.userName,"phoneNum":$scope.info.phoneNum,"illnessDesc":$scope.info.illness,"doctorId":$stateParams.doctorid},function (data) {
                window.location.href="http://s201.xiaork.com/keeper/wechatInfo/fieldwork/wechat/author?url=http://s201.xiaork.com/keeper/wechatInfo/getUserWechatMenId?url=59,"+data.phonePayPrice
            })
        };



        $scope.$on('$ionicView.beforeEnter',function() {
            console.log("医生电话咨询价格 ",$stateParams.price);
            $scope.phonePayPrice=$stateParams.price;
        })

    }]);

