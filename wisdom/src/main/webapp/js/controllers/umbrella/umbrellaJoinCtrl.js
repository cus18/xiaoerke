angular.module('controllers', ['ionic']).controller('umbrellaJoinCtrl', [
        '$scope','$state','$stateParams','JoinUs',
        function ($scope,$state,$stateParams,JoinUs) {
            $scope.title="宝护伞";
            $scope.shareLock=false;

            $scope.firstJoin=false;
            $scope.updateJoin=false;
            $scope.finally=false;
            $scope.umbrellaMoney=0;
            $scope.num=0;
            $scope.person=0;

            $scope.goDetail=function(){
                window.location.href = "/wisdom/firstPage/umbrella";
            };
            $scope.goActive=function(){
                $state.go("umbrellaFillInfo");
            };
            $scope.goShare=function(){
                $scope.shareLock=true;
            };
            $scope.cancelShare=function(){
                $scope.shareLock=false;
            };
            $scope.$on('$ionicView.enter', function(){
                JoinUs.save(function(data){
                    if(data.result==1){
                        $scope.firstJoin=true;
                        $scope.umbrellaMoney=20000;
                    }else if(data.result==2){
                        $scope.updateJoin=true;
                        $scope.umbrellaMoney=data.umbrella.umbrella_money;
                    }else if(data.result==3){
                        $scope.finally=true;
                        $scope.umbrellaMoney=data.umbrella.umbrella_money;
                        $scope.num=data.umbrella.id-120000000;
                    }
                    $scope.person=(400000-$scope.umbrellaMoney)/20000;
                });
            });

    }]);