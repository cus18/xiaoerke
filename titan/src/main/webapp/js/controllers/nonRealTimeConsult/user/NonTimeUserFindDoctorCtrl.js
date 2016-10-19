angular.module('controllers', []).controller('NonTimeUserFindDoctorCtrl', [
    '$scope', '$state', '$timeout', '$http',
    function ($scope, $state, $timeout, $http) {
        $scope.NonTimeUserFindDoctorInit = function () {

        };

        $scope.moreDepartments = 7;
        $scope.showDepartments = true;
        $scope.departmentlist = [
            {
                departmentName: "儿内科",
                src: "http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/nonRealTimeConsult%2Fuser%2Fa_21.png"
            },
            {
                departmentName: "小儿呼吸科",
                src: "http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/nonRealTimeConsult%2Fuser%2Fa_21.png"
            },
            {
                departmentName: "小儿消化科",
                src: "http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/nonRealTimeConsult%2Fuser%2Fa_21.png"
            },
            {
                departmentName: "小儿皮肤科",
                src: "http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/nonRealTimeConsult%2Fuser%2Fa_21.png"
            },
            {
                departmentName: "小儿保健科",
                src: "http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/nonRealTimeConsult%2Fuser%2Fa_21.png"
            },
            {
                departmentName: "妇产科",
                src: "http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/nonRealTimeConsult%2Fuser%2Fa_21.png"
            },
            {
                departmentName: "小儿耳鼻喉科",
                src: "http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/nonRealTimeConsult%2Fuser%2Fa_21.png"
            },
            {
                departmentName: "小儿外科",
                src: "http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/nonRealTimeConsult%2Fuser%2Fa_21.png"
            },
            {
                departmentName: "小儿眼科",
                src: "http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/nonRealTimeConsult%2Fuser%2Fa_21.png"
            },
            {
                departmentName: "小儿口腔科",
                src: "http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/nonRealTimeConsult%2Fuser%2Fa_21.png"
            },
            {
                departmentName: "小儿中医科",
                src: "http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/nonRealTimeConsult%2Fuser%2Fa_39.png"
            },
            {
                departmentName: "预防接种科",
                src: "http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/nonRealTimeConsult%2Fuser%2Fa_46.png"
            }
        ];
        $scope.doctorList = [
            {
                src: 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf%2Fconsult%2Fyonghumoren.png',
                doctorName: '梁医生',
                department: '妇科',
                position: '副主任医师',
                description: '擅长：婴儿湿疹，婴儿湿疹，婴儿湿疹，手足口',
                population: '9999',
                evaluate: '99%'
            }, {
                src: 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf%2Fconsult%2Fyonghumoren.png',
                doctorName: '梁医生',
                department: '妇科',
                position: '副主任医师',
                description: '擅长：婴儿湿疹，婴儿湿疹，婴儿湿疹，手足口',
                population: '9999',
                evaluate: '99%'
            }, {
                src: 'http://xiaoerke-pc-baodf-pic.oss-cn-beijing.aliyuncs.com/dkf%2Fconsult%2Fyonghumoren.png',
                doctorName: '梁医生',
                department: '妇科',
                position: '副主任医师',
                description: '擅长：婴儿湿疹，婴儿湿疹，婴儿湿疹，手足口',
                population: '9999',
                evaluate: '99%'
            }
        ];
        $scope.checkDepartment = function (index) {
            console.log($scope.departmentlist[index].departmentName);
            $state.go("NonTimeUserDoctorList", {department: $scope.departmentlist[index].departmentName});
        };
        $scope.moreDepartment = function () {
            $scope.moreDepartments = $scope.departmentlist.length;
            $scope.showDepartments = false;

        }
    }]);
