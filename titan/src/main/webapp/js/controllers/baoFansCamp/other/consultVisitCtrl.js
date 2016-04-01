angular.module('controllers', ['ionic','ngAnimate']).controller('consultVisitCtrl', [
        '$scope','$state', '$timeout','ConsultVisit','$stateParams',
        function ($scope,$state,$timeout,ConsultVisit,$stateParams) {
            $scope.title0 = "宝大夫（400-623-7120）";
            $scope.shareLock=false;
            $scope.clickLock=true;
            $scope.getTicketLock=false;
            $scope.info = {}
            $scope.visitList = [
                { text: "我家宝宝身体倍儿棒，暂不需要", checked: true ,reason:"" },
                { text: "对宝大夫的咨询医生不满意，无法支付", checked: false ,reason:""},
                { text: "在使用其他更好的咨询平台", checked: false ,reason:""},
                { text: "不相信互联网在线咨询服务", checked: false ,reason:""},
                { text: "我儿科大牛，不用麻烦", checked: false,reason:"" }
           ]

            $scope.chooseOption = "";
            $scope.select=function(index){
                $scope.clickLock=false;
                //console.log( $scope.visitList[index].text);
                $scope.chooseOption = $scope.visitList[index].text;
            }
            $scope.getTicket=function(){
                $scope.getTicketLock=true;
                console.log($scope.chooseOption);
                console.log($scope.info.otherReason);
                ConsultVisit.save({advice:"选择:"+$scope.chooseOption+" ,补充:"+$scope.info.otherReason,"openId":$stateParams.openId},function(date){
                    $timeout(function() {
                        $scope.getTicketLock=false;
                        WeixinJSBridge.call('closeWindow');
                    }, 3000);
                });


            }
            $scope.skip = function(){
                $('html,body').animate({scrollTop:$('#').offset().top}, 0);
            }


    }])
