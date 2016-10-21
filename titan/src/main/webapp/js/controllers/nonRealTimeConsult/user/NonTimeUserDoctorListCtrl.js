angular.module('controllers', []).controller('NonTimeUserDoctorListCtrl', [
        '$scope','$state','$timeout','$http','$stateParams','DoctorListByDepartment',
        function ($scope,$state,$timeout,$http,$stateParams,DoctorListByDepartment) {
            $scope.NonTimeUserDoctorListInit = function(){
                //根据传参中的科室信息查询相应的列表页
                console.log($stateParams.department)
                DoctorListByDepartment.save({"departmentName":$stateParams.department},function (data) {
                    console.log(data)
                    $scope.doctorList = data.departmentVoList
                })
            };
            $scope.checkDoctorInformation = function(index){
                console.log($scope.doctorList[index].doctorId);
                location.href="consultDoctorHome#/consultDoctorHome/"+$scope.doctorList[index].userId
            }
    }]);
