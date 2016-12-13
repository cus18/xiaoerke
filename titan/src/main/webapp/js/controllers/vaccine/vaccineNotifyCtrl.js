
angular.module('controllers', ['ionic']).controller('vaccineNotifyCtrl', [
    '$scope','GetVaccineCodeList','$stateParams',
    function ($scope,GetVaccineCodeList,$stateParams) {
        $scope.id = $stateParams.id;
        $scope.doRefresh = function () {
            GetVaccineCodeList.save({id:$scope.id},function(data){
                if(data.status == "success"){
                    $scope.notify = data.dataList[0];
                    console.log($scope.notify);
                }
            });
        };
    }]);

