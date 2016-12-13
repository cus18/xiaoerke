
angular.module('controllers', ['ionic']).controller('vaccineInformationCtrl', [
    '$scope','$stateParams','$state','GetOneVaccineInfoList',
    function ($scope,$stateParams,$state,GetOneVaccineInfoList) {
        $scope.doRefresh = function () {
            GetOneVaccineInfoList.save({id:$stateParams.id},function(data){
                if(data.status == "success"){
                    $scope.vaccine = data.dataList[0];
                    console.log($scope.vaccine);
                }
            });
        };
        $scope.goNotify = function () {
            //$scope.notify = $scope.vaccineList[index];
            $state.go('vaccineNotify', {id: $stateParams.id});
        };
    }]);

