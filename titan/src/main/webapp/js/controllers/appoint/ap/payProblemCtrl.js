angular.module('controllers', ['ionic']).controller('payProblemCtrl',[
    '$scope','$state','$stateParams','$rootScope','$ionicBackdrop','$timeout','WxPayAdvice',
    function ($scope,$state,$stateParams,$rootScope,$ionicBackdrop,$timeout,WxPayAdvice) {
        $scope.pageLoading = true;
        $scope.title0 = "宝大夫"
        $scope.title = "支付建议"
        $scope.isAppointment = false;
        $scope.submitLock=false;
        $scope.payList = [
            { text: "不熟悉微信支付方法", checked: true ,reason:"" },
            { text: "微信未绑定银行卡，无法支付", checked: false ,reason:""},
            { text: "点击微信支付后无响应", checked: false ,reason:""},
            { text: "感觉价格不合理", checked: false ,reason:""},
            { text: "其他（请填写具体困难或建议）", checked: false,reason:"" }
        ];

        $scope.payAdvise=function(){
            $scope.submitLock=true;
            $timeout(function() {    //默认让它1秒后消失
                $ionicBackdrop.release();
                $scope.submitLock=false;
            }, 2000);
            WxPayAdvice.save({payList:$scope.payList},function(data){
                history.back();
            })
        }
        $scope.skip = function(){
            $('html,body').animate({scrollTop:$('this').offset().top}, 0);
        }
    }])
