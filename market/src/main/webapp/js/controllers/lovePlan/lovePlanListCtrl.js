angular.module('controllers', ['ionic']).controller('lovePlanListCtrl', [
    '$scope','$state','$stateParams','PhotoWall','$filter',
    function ($scope,$state,$stateParams,PhotoWall,$filter) {
        PhotoWall.save({
            openid:'"o3_NPwvfAE_v-LfjpER1V4L3ZUIE"'
        }, function (data) {
            console.log(data);
            $scope.user = data.donationList;
            $scope.nincName = data.myMap.wechatName;
            $scope.money = data.myMap.sumMoney;
            $scope.time = data.myMap.lastTime;
            if(data.myMap.headImgUrl != ''){
                $scope.headImg = data.myMap.headImgUrl;
            }else{
                $scope.headImg = 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf%2Fpic%2Fa_04.png';
            }
        });
        $scope.transformDate = function(dateTime){
            var angularDay = $filter('date')(dateTime,"yyyy-MM-dd HH:mm:ss");
            var dateValue = moment(angularDay).startOf('hour').fromNow();
            return dateValue;
        };
        $scope.title ="宝妈爱心接力";
        $scope.goContribute = function () {
            window.location.href="http://localhost:8080/keeper/wxPay/patientPay.do?serviceType=lovePlanPay"
        };
        $scope.$on('$ionicView.enter', function(){

        })
    }]);

