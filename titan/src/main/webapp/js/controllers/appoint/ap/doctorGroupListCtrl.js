angular.module('controllers', ['ionic']).controller('DoctorGroupListCtrl', ['$rootScope',
    '$scope','$state','$stateParams','DoctorGroupInfo','DoctorListInDoctorGroup','RecordLogs',
    function ($rootScope,$scope,$state,$stateParams,DoctorGroupInfo,DoctorListInDoctorGroup,RecordLogs) {

        $scope.title = "医生集团";
        $scope.isBlank = false;
        $scope.openImg = "http://xiaoerke-common-pic.oss-cn-beijing.aliyuncs.com/common%2Fopen_down.png";
        $scope.openMoreLock = true;

        $scope.openMore=function(){
            if($scope.openMoreLock){
                $scope.openMoreLock = false;
                $scope.openImg = "http://xiaoerke-common-pic.oss-cn-beijing.aliyuncs.com/common%2Fopen_up.png";
            }
            else{
                $scope.openMoreLock = true;
                $scope.openImg = "http://xiaoerke-common-pic.oss-cn-beijing.aliyuncs.com/common%2Fopen_down.png";
            }
        }
        $scope.$on('$ionicView.enter', function(){
            RecordLogs.get({"logContent": encodeURI("doctorGroupList")}, function () {})

            DoctorGroupInfo.get({doctorGroupId:$stateParams.groupId},function(data){
                $scope.doctorGroupName = data.doctorGroupName;
                $scope.description = data.description;
                $scope.doctorId = data.doctorId;
                $scope.expertise = data.expertise.split(";");
               /* $scope.expertise = _.chunk($scope.expertise,2);*/
                var expertiseVal = "";
                $.each($scope.expertise,function(index,value){
                    if(value.length>6){
                        expertiseVal = expertiseVal + '<a> ' + value.substr(0, 5)+'...'+'</a>';
                    }
                    else{
                        expertiseVal = expertiseVal + '<a> ' + value+'</a>';
                    }
                   /* $.each(value,function(index,val){
                        expertiseVal = expertiseVal + '<a>'+val+'</a>';
                    })
                    expertiseVal = expertiseVal + "<br/>";*/
                })
                $("#expertise").html(expertiseVal);
            })

            DoctorListInDoctorGroup.save({doctorGroupId:$stateParams.groupId,
                pageNo:"1",pageSize:"100",orderBy:"0"},function(data){
                if(data.doctorDataVo!=null||data.doctorDataVo!=undefined){
                    $scope.isBlank = false;
                    $scope.doctorData = data.doctorDataVo;
                }else{
                    $scope.isBlank = true;
                }
            });
        })

        $scope.doctorAppointment = function(doctorId,available_time,hospitalName,location,position,location_id){
            if(available_time>=0&&available_time<7)
            {
                $state.go("doctorAppointment", {
                    doctorId: doctorId, available_time: available_time,
                    hospitalName: hospitalName, location: location, position: position,
                    mark:"dateAvailable",location_id:location_id,attentionDoctorId:""
                });
            }
            else
            {
                $state.go("doctorAppointment", {
                    doctorId: doctorId, available_time: available_time,
                    hospitalName: hospitalName, location: location, position: position,
                    mark:"dateNoAvailable",location_id:location_id,attentionDoctorId:""
                });
            }
        }
    }])