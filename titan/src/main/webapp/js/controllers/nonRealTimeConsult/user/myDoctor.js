angular.module('controllers', []).controller('myDoctor', [
    '$scope','$stateParams','recentTimeList',
    function ($scope,$stateParams,recentTimeList) {
        $scope.title='我的医生';
        $scope.doctorInfo = {};

        // 我咨询记录的数据
        recentTimeList.save({},function (data) {
            if("success" == data.status){
                $scope.doctorInfo = data.departmentVoList;
                console.log(data)
            }else{
                alert("请重新打开页面");
            }

        })
    }]);
