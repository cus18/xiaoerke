
angular.module('controllers', ['ionic']).controller('vaccineInformationCtrl', [
    '$scope','$stateParams','$state','GetVaccineCodeList',
    function ($scope,$stateParams,$state,GetVaccineCodeList) {
        $scope.id = $stateParams.id;
        $scope.doRefresh = function () {
            GetVaccineCodeList.save({id:$scope.id},function(data){
                if(data.status == "success"){
                    $scope.vaccine = data.dataList[0];
                    console.log($scope.vaccine);
                }
            });
        };
        $scope.goNotify = function () {
            //$scope.notify = $scope.vaccineList[index];
            $state.go('vaccineNotify', {id: $scope.id});
        };
    }]);

