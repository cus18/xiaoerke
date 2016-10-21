angular.module('controllers', []).controller('NonTimeUserFindDoctorCtrl', [
    '$scope', '$state', '$timeout', '$http','DepartmentList','StarDoctorlist',
    function ($scope, $state, $timeout, $http,DepartmentList,StarDoctorlist) {
        $scope.NonTimeUserFindDoctorInit = function () {
            //获取科室信息
            DepartmentList.save({},function (date) {
                console.log(date)
                $scope.departmentlist = date.departmentList
            })
            //获取明星医生信息
            StarDoctorlist.save({},function (date) {
                console.log(date)
                $scope.doctorList = date.startDoctorList
            })
        };

        $scope.moreDepartments = 7;
        $scope.showDepartments = true;
        $scope.checkDepartment = function (index) {
            console.log($scope.departmentlist[index].name);
            $state.go("NonTimeUserDoctorList", {department: $scope.departmentlist[index].name});
        };
        $scope.moreDepartment = function () {
            $scope.moreDepartments = $scope.departmentlist.length;
            $scope.showDepartments = false;

        };
        $scope.checkDoctorInformation = function(index){
            console.log($scope.doctorList[index].userId);
            location.href="http://localhost:8080/titan/consultDoctorHome#/consultDoctorHome/"+$scope.doctorList[index].userId
        }
    }]);
