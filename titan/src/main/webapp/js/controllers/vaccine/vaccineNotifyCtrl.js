
angular.module('controllers', ['ionic']).controller('vaccineNotifyCtrl', [
    '$scope','GetOneVaccineInfoList','$stateParams',
    function ($scope,GetOneVaccineInfoList,$stateParams) {
        $scope.doRefresh = function () {
            GetOneVaccineInfoList.save({id:$stateParams.id},function(data){
                if(data.status == "success"){
                    $scope.informedForm = data.dataList[0].informedForm;
                    console.log($scope.informedForm);
                }
            });
        };
    }]);

