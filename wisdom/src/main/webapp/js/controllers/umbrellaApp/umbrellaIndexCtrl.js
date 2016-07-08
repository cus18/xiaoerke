angular.module('controllers', ['ionic']).controller('umbrellaIndexCtrl', [
    '$scope','$state','$stateParams','GetJoinNumber',
    function ($scope,$state,$stateParams,GetJoinNumber) {


        $scope.$on('$ionicView.beforeEnter',function () {
            GetJoinNumber.get(function (data) {
                console.log("data",data);
                $scope.joinNumber = data.mutualHelpCount;
            })
        })

        //宝大夫儿童重疾互助计划公约  60种重大疾病名称及定义 15种轻症名称及定义 名词释义
        $scope.lookProtocol = function (index) {
            $(".c-shadow").show();

            $(".protocol").eq(index).show();
        }

        //重大疾病名称及定义 展开隐藏
        $scope.lookHelpPlan = function () {
            $(".helpPlan .fold").toggleClass("show");
            $(".helpPlan .foldText").toggleClass("change");
        }

        //常见问题
        $scope.lookQuestion = function(index){
            $(".questions dt").eq(index).siblings("dt").children("a").removeClass("show");
            $(".questions dd").eq(index).siblings("dd").removeClass("change");
            $(".questions dt a").eq(index).toggleClass("show");
            $(".questions dd").eq(index).toggleClass("change");

        }

        //跳转保护伞咨询
        $scope.umbrellaConsult = function () {
            window.location.href='http://s132.baodf.com/angel/patient/consult#/patientConsultUmbrella';
        }

        /*分享好友*/
        $scope.goShare = function() {
            $(".c-shadow").show();
            $(".shadow-content.share").show();

        }

        /*关闭分享提示*/
        $scope.cancelRemind = function() {
            $(".c-shadow").hide();
            $(".shadow-content").hide();
        }

        //马上领取
        $scope.goFillInfo = function () {
            $state.go("umbrellaFillInfo");
        }

    }]);
