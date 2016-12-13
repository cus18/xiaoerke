
angular.module('controllers', ['ionic']).controller('vaccineListCtrl', [
    '$scope','GetVaccineCodeList',
    function ($scope,GetVaccineCodeList) {
        $scope.info = {
            shoeFree:false,
            showCharge:true
        };
        $scope.vaccineList = [];
        $scope.doRefresh = function(){
            GetVaccineCodeList.save({},function(data){
                console.log(data.dataList);
                if(data.status == "success"){
                    $scope.vaccineList.push(data.dataList);
                }
            });
        }
    }]);

