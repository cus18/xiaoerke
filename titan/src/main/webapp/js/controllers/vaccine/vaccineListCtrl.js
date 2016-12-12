
angular.module('controllers', ['ionic']).controller('vaccineListCtrl', [
    '$scope',
    function ($scope) {
        $scope.info = {
            shoeFree:false,
            showCharge:true
        };
        $scope.vaccineList = [
            {
                vaccine:'乙肝疫苗',
                age:'月龄',
                num:'第1/3针'
            },
            {
                vaccine:'甲肝疫苗',
                age:'月龄',
                num:'第1/3针'
            },
            {
                vaccine:'乙肝疫苗',
                age:'月龄',
                num:'第1/3针'
            }
        ];
        $scope.doRefresh = function(){

        }
    }]);

