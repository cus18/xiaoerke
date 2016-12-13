
angular.module('controllers', ['ionic']).controller('vaccineNotifyCtrl', [
    '$scope','GetOneVaccineInfoList','$stateParams',
    function ($scope,GetOneVaccineInfoList,$stateParams) {
        $scope.id = $stateParams.id;
        $scope.doRefresh = function () {
            GetOneVaccineInfoList.save({id:$scope.id},function(data){
                if(data.status == "success"){
                    $scope.notify = data.dataList[0];
                    console.log($scope.notify);
                }
            });
        };
    }]);

