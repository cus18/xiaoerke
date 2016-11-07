angular.module('controllers', []).controller('NonTimeUserFindDoctorCtrl', [
    '$scope', '$state','DepartmentList','StarDoctorList',
    function ($scope,$state,DepartmentList,StarDoctorList) {

        var recordLogs = function(val){
            $.ajax({
                url:"util/recordLogs",// 跳转到 action
                async:true,
                type:'get',
                data:{logContent:encodeURI(val)},
                cache:false,
                dataType:'json',
                success:function(data) {
                },
                error : function() {
                }
            });
        }

        recordLogs("FSS_YHD_RK1");
        $scope.NonTimeUserFindDoctorInit = function () {
            //获取科室信息
            DepartmentList.save({},function (date) {
                console.log(date);
                $scope.departmentlist = date.departmentList;
            });
            //获取明星医生信息
            StarDoctorList.save({},function (date) {
                console.log(date);
                $scope.doctorList = date.startDoctorList;
            });
        };

        $scope.moreDepartments = 7;
        $scope.showDepartments = true;
        $scope.checkDepartment = function (index) {
            console.log("FSS_YHD_KS_GD"+$scope.departmentlist[index].name);
            recordLogs($scope.departmentlist[index].name);
            $state.go("NonTimeUserDoctorList", {department: $scope.departmentlist[index].name});
        };
        $scope.moreDepartment = function () {
            recordLogs("FSS_YHD_KS_GD");
            $scope.moreDepartments = $scope.departmentlist.length;
            $scope.showDepartments = false;

        };
        $scope.checkDoctorInformation = function(index){
            console.log($scope.doctorList[index].userId);
            recordLogs("FSS_YHD_MXYS"+$scope.doctorList[index].userId);
            location.href="consultDoctorHome#/consultDoctorHome/"+$scope.doctorList[index].userId
        }
    }]);
