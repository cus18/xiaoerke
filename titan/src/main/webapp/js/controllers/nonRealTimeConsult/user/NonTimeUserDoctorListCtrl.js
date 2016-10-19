angular.module('controllers', []).controller('NonTimeUserDoctorListCtrl', [
        '$scope','$state','$timeout','$http',
        function ($scope,$state,$timeout,$http) {
            $scope.NonTimeUserDoctorListInit = function(){

            };
            $scope.doctorList = [
                {
                    src:'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf%2Fconsult%2Fyonghumoren.png',
                    doctorName:'梁医生',
                    department:'妇科',
                    position:'副主任医师',
                    description:'擅长：婴儿湿疹，婴儿湿疹，婴儿湿疹，手足口',
                    population:'9999',
                    evaluate:'99%'
                },{
                    src:'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf%2Fconsult%2Fyonghumoren.png',
                    doctorName:'梁医生',
                    department:'妇科',
                    position:'副主任医师',
                    description:'擅长：婴儿湿疹，婴儿湿疹，婴儿湿疹，手足口',
                    population:'9999',
                    evaluate:'99%'
                },{
                    src:'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf%2Fconsult%2Fyonghumoren.png',
                    doctorName:'梁医生',
                    department:'妇科',
                    position:'副主任医师',
                    description:'擅长：婴儿湿疹，婴儿湿疹，婴儿湿疹，手足口',
                    population:'9999',
                    evaluate:'99%'
                }
            ];
            $scope.checkDoctorInformation = function(index){
                console.log($scope.doctorList[index].doctorId);
                location.href="http://localhost:8080/titan/consultDoctorHome#/consultDoctorHome/id="+$scope.doctorList[index].doctorId
            }
    }]);
