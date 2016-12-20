
angular.module('controllers', ['ionic']).controller('vaccineNotifyCtrl', [
    '$scope','GetOneVaccineInfoList','$stateParams','$sce',
    function ($scope,GetOneVaccineInfoList,$stateParams,$sce) {
        $scope.doRefresh = function () {
            GetOneVaccineInfoList.save({id:$stateParams.id},function(data){
                if(data.status == "success"){
                    $scope.informedForm = $sce.trustAsHtml(data.dataList[0].informedForm);
                    $scope.informedFormTitle = data.dataList[0].informedFormTitle;
                    console.log(data.dataList[0].informedForm);
                }
            });
        };
    }]);

