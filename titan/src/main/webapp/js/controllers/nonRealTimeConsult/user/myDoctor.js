﻿angular.module('controllers', []).controller('myDoctor', [
    '$scope','$stateParams','$state','recentTimeList',
    function ($scope,$stateParams,$state,recentTimeList) {
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

        $scope.checkDoctorInformation = function(index){
            console.log($scope.doctorInfo[index].doctorId);
            location.href="consultDoctorHome#/consultDoctorHome/"+$scope.doctorInfo[index].userId;
        }
        $scope.nonRealTimeOngoing = function (index) {
            $state.go("NonTimeUserConversation",{"sessionId":index});
        }

        $scope.nonRealTimeConsult = function (userid) {
            $state.go("NonTimeUserFirstConsult",{"doctorId":userid});
        }
    }]);
