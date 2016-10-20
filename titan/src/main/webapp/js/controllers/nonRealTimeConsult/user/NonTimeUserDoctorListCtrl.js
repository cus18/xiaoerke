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
            // $scope.doctorList = [
            //     {
            //         src:'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf%2Fconsult%2Fyonghumoren.png',
            //         doctorName:'梁医生',
            //         department:'妇科',
            //         position:'副主任医师',
            //         description:'擅长：婴儿湿疹，婴儿湿疹，婴儿湿疹，手足口',
            //         population:'9999',
            //         evaluate:'99%'
            //     },{
            //         src:'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf%2Fconsult%2Fyonghumoren.png',
            //         doctorName:'梁医生',
            //         department:'妇科',
            //         position:'副主任医师',
            //         description:'擅长：婴儿湿疹，婴儿湿疹，婴儿湿疹，手足口',
            //         population:'9999',
            //         evaluate:'99%'
            //     },{
            //         src:'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf%2Fconsult%2Fyonghumoren.png',
            //         doctorName:'梁医生',
            //         department:'妇科',
            //         position:'副主任医师',
            //         description:'擅长：婴儿湿疹，婴儿湿疹，婴儿湿疹，手足口',
            //         population:'9999',
            //         evaluate:'99%'
            //     }
            // ];
            $scope.checkDoctorInformation = function(index){
                console.log($scope.doctorList[index].doctorId);
                location.href="consultDoctorHome#/consultDoctorHome/"+$scope.doctorList[index].userId
            }
    }]);
