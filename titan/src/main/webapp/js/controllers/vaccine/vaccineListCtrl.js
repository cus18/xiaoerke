
angular.module('controllers', ['ionic']).controller('vaccineListCtrl', [
    '$scope','GetVaccineCodeList','$state',
    function ($scope,GetVaccineCodeList,$state) {
        $scope.info = {
            shoeFree:false,
            showCharge:true
        };
        $scope.vaccineList = [];
        $scope.doRefresh = function(){
            GetVaccineCodeList.save({},function(data){
                if(data.status == "success"){
                    $scope.vaccineList = data.dataList;
                    console.log($scope.vaccineList);
                }
            });
        };
        $scope.goInformation = function(id){
            $state.go('vaccineInformation', {id: $scope.id});
        };
    }]);

