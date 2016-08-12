angular.module('controllers', ['ionic']).controller('umbrellaPayCtrl', [
    '$scope','$state','$stateParams','Goalipayment',
    function ($scope,$state,$stateParams,Goalipayment) {

        var Ip = "http://localhost/wisdom/appUmbrella#/umbrellaIndex";

        $scope.$on('$ionicView.enter', function(){

        });

        $scope.Pay = function () {
            Goalipayment.save({"totleFee":5,"body":"","describe":"保护伞","showUrl":Ip,"umbrellaid":$stateParams.id},
                function(data) {
                    
            });
        }

    }]);
