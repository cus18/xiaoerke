angular.module('controllers', ['ionic','ngDialog']).controller('addGroupCtrl', [
    '$scope','$state','$stateParams','BabyEnglish',
    function ($scope,$state,$stateParams,BabyEnglish) {

        BabyEnglish.save({},function (data) {
            $scope.imgUrl = data.imgPath
        })
    }])
