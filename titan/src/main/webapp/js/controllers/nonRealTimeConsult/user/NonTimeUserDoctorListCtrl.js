angular.module('controllers', []).controller('NonTimeUserDoctorListCtrl', [
        '$scope','$stateParams','DoctorListByDepartment','SearchDoctorInfoByName',
        function ($scope,$stateParams,DoctorListByDepartment,SearchDoctorInfoByName) {
            $scope.NonTimeUserDoctorListInit = function(){
                //根据传参中的科室信息查询相应的列表页
                console.log($stateParams.department);
                if($stateParams.department != ""){
                    DoctorListByDepartment.save({"departmentName":$stateParams.department},function (data) {
                        console.log(data);
                        $scope.doctorList = data.departmentVoList;
                    })
                }else{
                    SearchDoctorInfoByName.save({"doctorName":$stateParams.doctorName},function (data) {
                        console.log(data);
                        $scope.doctorList = data.departmentVoList;
                    })
                }

            };
            $scope.consulted = true;
            $scope.checkDoctorInformation = function(index){
                console.log($scope.doctorList[index].doctorId);
                location.href="consultDoctorHome#/consultDoctorHome/"+$scope.doctorList[index].userId;
            }
    }]);
